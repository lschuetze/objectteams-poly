#!/bin/sh

# OPTIONAL VARIABLES TO BE SUPPLIED VIA ENV:
# 	SIGN (unset or nosign)
# 	PROMOTE (unset or false or target directory)

BASE=`pwd`

# ABSOLUTE PATHS:
export UPDATES_BASE=/home/data/httpd/download.eclipse.org/objectteams/updates
export JAVA8=/shared/common/jdk1.8.0_x64-latest

# RELATIVE PATHS:
BUILD=${BASE}/releng/build-scripts/build
METADATA=${BASE}/metadata

# Find the master repository to build upon:
if [ "$1" == "none" ]
then
        MASTER="none"
        echo "Generating fresh new repository"
else
        MASTER=${UPDATES_BASE}/$1
        if [ -r ${MASTER}/features ]
        then
            echo "Generating Repository based on ${MASTER}"
        else
            MASTER=${HOME}/shared/baseRepos/$1
            if [ -r ${MASTER}/features ]
            then
                echo "Generating Repository based on ${MASTER}"
            else
                echo "No such repository ${MASTER}"
                echo "Usage: $0 updateMasterRelativePath [ -nosign ] [ statsRepoId statsVersionId ]"
                exit 1
            fi
        fi
fi

# Analyze the version number of the JDT feature as needed for patching content.xml later:
JDTFEATURE=`ls -d ${BASE}/testrun/build-root/eclipse/features/org.eclipse.jdt_*`
if echo $JDTFEATURE | grep "\.r"
then
        JDTVERSION="`echo ${JDTFEATURE} | cut -d '_' -f 2`_`echo ${JDTFEATURE} | cut -d '_' -f 3`"
else
        JDTVERSION=`echo ${JDTFEATURE} | cut -d '_' -f 2`
fi
JDTVERSIONA=`echo ${JDTVERSION} | cut -d '-' -f 1`
JDTVERSIONB=`echo ${JDTVERSION} | cut -d '-' -f 2`
echo "after first split: ${JDTVERSIONA} and ${JDTVERSIONB}"
case ${JDTVERSIONB} in
        ????)
                #A=v20110813 B=0800
                JDTVERSIONB2=`expr $JDTVERSIONB + 1`
                JDTVERSIONB2=`printf "%04d" ${JDTVERSIONB2}`
                JDTVERSION=${JDTVERSIONA}-${JDTVERSIONB}
                JDTVERSIONNEXT=${JDTVERSIONA}-${JDTVERSIONB2}
                ;;
        *)
                #A=3.8.0.v20110813 B=someunspeakablelonghashid
                JDTVERSIONC1=`echo ${JDTVERSIONA} | cut -d 'v' -f 1`
                JDTVERSIONC2=`echo ${JDTVERSIONA} | cut -d 'v' -f 2`
                JDTVERSIONC3=`expr $JDTVERSIONC2 + 1`
                JDTVERSION=${JDTVERSIONC1}v${JDTVERSIONC2}
                JDTVERSIONNEXT=${JDTVERSIONC1}v${JDTVERSIONC3}
                ;;
esac
# hardcode when unable to compute
#JDTVERSION=${JDTVERSIONA}
#JDTVERSIONNEXT=3.8.0.v20110728
echo "JDT feature is ${JDTVERSION}"
echo "Next           ${JDTVERSIONNEXT}"
if [ ! -r ${BASE}/testrun/build-root/eclipse/features/org.eclipse.jdt_${JDTVERSION} ]
then
    echo "JDT feature not correctly found in ${BASE}/testrun/build-root/eclipse/features"
    exit 2
fi
OTDTVERSION=`cat ${BASE}/testrun/build-root/src/finalFeaturesVersions.properties|grep "objectteams.otdt="|cut -d '=' -f 2`
if [ "${OTDTVERSION}" == "" ]
then
	echo "finalFeaturesVersions.properties not found, maybe build hasn't run successfully?"
	exit 3
fi 
echo "OTDTVERSION is $OTDTVERSION"

# Configure for calling various p2 applications:
LAUNCHER=`grep equinox.launcher_jar= ${BUILD}/run.properties | cut -d '=' -f 2`
LAUNCHER_PATH=${BASE}/testrun/build-root/eclipse/plugins/${LAUNCHER}
FABPUB=org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher
CATPUB=org.eclipse.equinox.p2.publisher.CategoryPublisher
JARPROCESSOR=`ls ${BASE}/testrun/build-root/eclipse/plugins/org.eclipse.equinox.p2.jarprocessor_*.jar`
NAME="Object Teams"

echo "LAUNCHER_PATH = ${LAUNCHER_PATH}"
echo "NAME          = ${NAME}"

echo "====Step 0: condition jars ===="
CONDITIONED=${BASE}/testrun/updateSiteRepack
if [ ! -d  ${CONDITIONED} ]
then
	mkdir ${CONDITIONED} 
else
	/bin/rm -r ${CONDITIONED}/*
fi

for dir in features plugins
do
        find ${BASE}/testrun/updateSite/${dir} -type f -name \*.jar -exec \
                ${JAVA8}/bin/java -jar ${JARPROCESSOR} -verbose -processAll -repack -outputDir ${CONDITIONED}/${dir} {} \;
done
# not conditioned, but must not be skipped!
cp ${BASE}/testrun/updateSite/plugins/org.eclipse.jdt.core_* ${CONDITIONED}/plugins/

echo "====Step 1: request signing and zip===="
cd ${CONDITIONED}
JARS=`find . -type f -name \*.jar`
OTDTJAR=${BASE}/testrun/otdt.jar
if [ "${SIGN}" == "nosign" ]
then
	/bin/rm ${OTDTJAR}
	zip ${OTDTJAR} ${JARS}
    echo "SKIPPING SIGNING"
else
    SIGNED=${BASE}/testrun/updateSiteSigned
	if [ ! -d ${SIGNED} ]
	then
		mkdir ${SIGNED}
	else
		/bin/rm -r ${SIGNED}/*
	fi
	for JAR in ${JARS}
	do
		DIR=`dirname $JAR`
		if [ ! -d ${SIGNED}/${DIR} ]
		then
			mkdir -p ${SIGNED}/${DIR}
		fi
		curl -o ${SIGNED}/${JAR} -F file=@${JAR} http://build.eclipse.org:31338/sign
	done
	if [ -f ${OTDTJAR} ]
	then
	    /bin/rm ${OTDTJAR}
	fi
	cd ${SIGNED}
	zip ${OTDTJAR} ${JARS}
	echo "Signing completed"
fi


echo "====Step 2: fill new repository===="
if [ -r ${BASE}/stagingRepo ]
then
    /bin/rm -rf ${BASE}/stagingRepo
fi
mkdir ${BASE}/stagingRepo
cd ${BASE}/stagingRepo
if [ "$MASTER" != "none" ]
then
        mkdir features
        (cd features; ln -s ${MASTER}/features/* .)
        mkdir plugins
        (cd plugins; ln -s ${MASTER}/plugins/* .)
else
        mkdir plugins
        cp ${BASE}/testrun/updateSite/plugins/org.apache.bcel* plugins/
fi
unzip -n ${OTDTJAR}
#/bin/rm ${OTDTJAR}

LOCATION=${BASE}/stagingRepo
echo "LOCATION  = ${LOCATION}"
cd ${LOCATION}

echo "====Step 3: pack jars (again) ===="
for dir in ${LOCATION}/features ${LOCATION}/plugins
do
        find ${dir} -type f -name \*.jar -exec \
                ${JAVA8}/bin/java -jar ${JARPROCESSOR} -verbose -pack -outputDir ${dir} {} \;
done


echo "====Step 4: generate metadata===="
java -jar ${LAUNCHER_PATH} -consoleLog -application ${FABPUB} \
    -source ${LOCATION} \
    -metadataRepository file:${LOCATION} \
    -artifactRepository file:${LOCATION} \
    -metadataRepositoryName "${NAME} Updates" \
    -artifactRepositoryName "${NAME} Artifacts" \
    -reusePack200Files -publishArtifacts
ls -ltr *\.*


echo "====Step 5: patch content for feature inclusion version range===="
mv content.xml content.xml-orig
xsltproc  -o content.xml --stringparam version ${JDTVERSION} \
    --stringparam versionnext ${JDTVERSIONNEXT} \
    ${BUILD}/patch-content-xml.xsl content.xml-orig
ls -ltr *\.*

echo "====Step 6: archive raw meta data===="
mkdir -p ${METADATA}/$OTDTVERSION
cp *.xml ${METADATA}/$OTDTVERSION
ls -ltr ${METADATA}/$OTDTVERSION/*.xml

echo "====Step 7: generate category===="
CATEGORYARGS="-categoryDefinition file:${BASE}/testrun/build-root/src/features/org.eclipse.objectteams.otdt/category.xml"
echo "CATEGORYARGS  = ${CATEGORYARGS}"
java -jar ${LAUNCHER_PATH} -consoleLog -application ${CATPUB} \
    -source ${LOCATION} \
    -metadataRepository file:${LOCATION} \
    ${CATEGORYARGS}
ls -ltr *\.*


echo "====Step 8: add download stats capability===="
XSLT_FILE=${BASE}/releng/build-scripts/bin/addDownloadStats.xsl

if [ $# == 3 ]; then
        mv artifacts.xml artifacts.xml.original
        if grep p2.statsURI artifacts.xml.original ; then echo "p2.statsURI already defined: exiting"; exit 1; fi
        xsltproc -o artifacts.xml --stringparam repo "http://download.eclipse.org/stats/objectteams/${2}" --stringparam version $3 $XSLT_FILE artifacts.xml.original
fi

echo "====Step 9: jar-up metadata===="
jar cf content.jar content.xml
jar cf artifacts.jar artifacts.xml
#/bin/rm *.xml*
ls -ltr *\.*

echo "====Step 10: cleanup: remove symbolic links===="
find . -type l -exec /bin/rm {} \;

if [ "${PROMOTE}" != "false" ]
then
	BUILDID=`echo $OTDTVERSION | cut -d '.' -f 4`
	if [ "${PROMOTE}" != "" ]
	then
        DEST=${UPDATES_BASE}/${2}/${PROMOTE}
        /bin/rm -rf ${DEST}
    else
        DEST=${UPDATES_BASE}/${2}/${BUILDID}
    fi
	echo "====Step 11: promote to ${DEST}===="
	if [ -d ${UPDATES_BASE}/${2} ]
	then
		mkdir ${DEST} && \
			cp -pr ${UPDATES_BASE}/${1}/* ${DEST}/ && \
			cp -pr * ${DEST}/ && \
			chmod -R g+w ${DEST} && \
			find ${DEST} -type d -exec /bin/ls -ld {} \;
		ls -latr ${UPDATES_BASE}/${2}
	else
		echo "${UPDATES_BASE}/${2} not found or not a directory"
	fi
fi
echo "====DONE===="
