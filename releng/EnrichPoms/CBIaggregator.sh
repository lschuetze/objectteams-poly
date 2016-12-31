#!/bin/bash
#*******************************************************************************
# Copyright (c) 2016 GK Software AG and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Stephan Herrmann - initial API and implementation
#********************************************************************************

#================================================================================
#   Parameters we might want to externalize into a properties file
#================================================================================

# ECLIPSE:
DROPS4=/home/data/httpd/archive.eclipse.org/eclipse/downloads/drops4
FILE_ECLIPSE=${DROPS4}/R-4.6.1-201609071200/eclipse-SDK-4.6.1-linux-gtk-x86_64.tar.gz
APP_NAME_P2DIRECTOR=org.eclipse.equinox.p2.director

# QUESTION: set tmpdir? (-vmargs not accepted by director application?)

# AGGREGATOR:
IU_AGG_PRODUCT=org.eclipse.cbi.p2repo.cli.product
URL_AGG_UPDATES=http://download.eclipse.org/cbi/updates/aggregator/headless/4.6/

FILE_SDK_AGGR=${WORKSPACE}/SDK4Mvn.aggr

# ENRICH POMS tool:
ENRICH_POMS_JAR=${WORKSPACE}/../../pomEnricher/workspace/EnrichPoms.jar

# LOCAL TOOLS:
LOCAL_TOOLS=${WORKSPACE}/tools
DIR_AGGREGATOR=aggregator
AGGREGATOR=${LOCAL_TOOLS}/${DIR_AGGREGATOR}/cbiAggr
ECLIPSE=${LOCAL_TOOLS}/eclipse/eclipse

#================================================================================
# Util functions
#================================================================================
function require_executable() {
	if [ -x ${1} ]
	then
		echo "Successfully installed: ${1}"
	else
		echo "not executable: ${1}"
		/bin/ls -l ${1}
		exit 1
	fi
}

# -------- fetch .aggr file **TEMP** will eventually move to the releng git: ------------
git archive --remote=file://localhost/gitroot/objectteams/org.eclipse.objectteams.git \
	master releng/EnrichPoms/SDK4Mvn.aggr \
	| tar xv
/bin/mv releng/EnrichPoms/SDK4Mvn.aggr ${WORKSPACE}/
/bin/rmdir -p releng/EnrichPoms

#================================================================================
#   (1) Install and run the CBI aggregator
#================================================================================
if [ ! -d ${LOCAL_TOOLS} ]
then
	/bin/mkdir ${LOCAL_TOOLS}
fi

if [ ! -x ${ECLIPSE} ]
then
	cd ${LOCAL_TOOLS}
	tar xf ${FILE_ECLIPSE}
	cd ${WORKSPACE}
fi
require_executable ${ECLIPSE}

if [ ! -x ${AGGREGATOR} ]
then
	${ECLIPSE} -application ${APP_NAME_P2DIRECTOR} \
		-r ${URL_AGG_UPDATES} \
		-d ${LOCAL_TOOLS}/${DIR_AGGREGATOR} -p CBIProfile \
		-installIU ${IU_AGG_PRODUCT}
fi
require_executable ${AGGREGATOR}

RepoRaw=${WORKSPACE}/reporaw-${BUILD_NUMBER}
Repo=${WORKSPACE}/repo-${BUILD_NUMBER}
/bin/mkdir ${RepoRaw}

${AGGREGATOR} aggregate --buildModel ${FILE_SDK_AGGR} --action CLEAN_BUILD --buildRoot ${RepoRaw}
if [ "$?" != "0" ]
then
    echo "FAILURE $?"
    exit 1
fi
/bin/mv ${RepoRaw}/final ${Repo}
/bin/rm -rf ${RepoRaw}

echo "========== Repo created: =========="
/usr/bin/du -sc ${Repo}/*
/usr/bin/du -sc ${Repo}/org/*
/usr/bin/du -sc ${Repo}/org/eclipse/*
echo "==================================="


#================================================================================
#   (2) Remove irrelevant stuff
#================================================================================
# Removes from the build output of cbiAggregator everything that is not relevant for maven.
# All removed directories / files will be logged to .logs/removed.txt

echo "==== Remove irrelevant stuff ===="

cd ${Repo}

if [ ! -d .logs ]
then
	/bin/mkdir .logs
elif [ -f .logs/removed.txt ]
then
	/bin/rm .logs/removed.txt
fi

#==== remove the p2 repository (not logged): ====

/bin/rm -r p2.index p2.packed content.jar artifacts.jar

#==== remove -sources artifacts, misplaced due to quirk from https://bugs.eclipse.org/508910: ====

echo "== Misplaced -sources artifacts: ==" | tee >> .logs/removed.txt

# works only outside org/eclipse/{platform,jdt,pde}:

/usr/bin/find -type d -name platform -prune -o -name jdt -prune -o -name pde -prune -o \
	-name \*-sources.jar\* -print -exec /bin/rm {} \; >> .logs/removed.txt

#==== remove features: ====

echo "== Features: ==" | tee >> .logs/removed.txt

/usr/bin/find * -type d -name \*feature.group -print -exec /bin/rm -rf {} \; -prune >> .logs/removed.txt
/usr/bin/find * -type d -name \*feature.jar -print -exec /bin/rm -rf {} \; -prune >> .logs/removed.txt

#==== remove eclipse test plug-ins: ====

echo "== Test plugins: ==" | tee >> .logs/removed.txt

ls -d org/eclipse/*/*test* >> .logs/removed.txt
/bin/rm -r org/eclipse/*/*test*

#==== remove other non-artifacts: ====

echo "== Other non-artifacts: ==" | tee >> .logs/removed.txt

/usr/bin/find tooling -type d >> .logs/removed.txt
/bin/rm -r tooling*

# ... folders that contain only 1.2.3/foo-1.2.3.pom but no corresponding 1.2.3/foo-1.2.3.jar:
function hasPomButNoJar() {
		cd ${1}
		# expect only one sub-directory, starting with a digit, plus maven-metadata.xml*:
		other=`ls -d [!0-9]* 2> /dev/null`
        if `echo "${other}" | egrep "^maven-metadata.xml\s*maven-metadata.xml.md5\s*maven-metadata.xml.sha1$"`
        then
        	exit 1
        fi
        # scan all *.pom inside the version sub-directory
        r=1
        for pom in `ls [0-9]*/*.pom 2> /dev/null`
        do
                jar=`echo ${pom} | sed -e "s|\(.*\)\.pom|\1.jar|"`
                if [ -f ${jar} ]
                then
                		# jar found, so keep it
                        exit 1
                fi
                # pom without jar found, let's answer true below
                r=0
        done
        exit $r
}
export -f hasPomButNoJar

/usr/bin/find org/eclipse/{jdt,pde,platform} -type d \
	-exec /bin/bash -c 'hasPomButNoJar "$@"' bash {} \; \
	-print -exec /bin/rm -rf {} \; -prune >> .logs/removed.txt
# second "bash" is used as $0 in the function

cd ${WORKSPACE}

echo "========== Repo reduced: =========="
/usr/bin/du -sc ${Repo}/*
/usr/bin/du -sc ${Repo}/org/*
/usr/bin/du -sc ${Repo}/org/eclipse/*
echo "==================================="

#================================================================================
#   (2) Garbage Collector
#================================================================================
# Removes from the build output of cbiAggregator everything that is not referenced 
# from any pom below org/eclipse/{platform,jdt,pde}
#
# Log output:
#  .logs/removedGarbage.txt	all directories during garbage collection 
#  .logs/gc.log 			incoming dependencies of retained artifacts
#  .logs/empty-dirs.txt		removed empty directories 

echo "==== Garbage Collector ===="

cd ${Repo}

#==== function gc_bundle(): ====
# Test if pom ${1} is referenced in any other pom.
# If not, append the containing directory to the file "toremove.txt"
function gc_bundle {
        AID=`echo ${1} | sed -e "s|.*/\(.*\)[_-].*|\1|"`
        DIR=`echo ${1} | sed -e "s|\(.*\)/[0-9].*|\1|"`
        POM=`basename ${1}`

        ANSWER=`find org/eclipse/{platform,jdt,pde} -name \*.pom \! -name ${POM} \
        		 -exec /bin/grep -q "<artifactId>${AID}</artifactId>" {} \; -print -quit`

        if [ "$ANSWER" == "" ]
        then
                echo "Will remove $DIR"
                echo $DIR >> toremove.txt
        else
                echo "$1 is used by $ANSWER"
        fi
}
export -f gc_bundle

#==== run the garbage collector: ====
# iterate (max 5 times) in case artifacts were used only from garbage:
for iteration in 1 2 3 4 5
do
	echo "== GC iteration ${iteration} =="

	# look for garbage only outside platform, jdt or pde folders:
	find -name platform -prune -o -name jdt -prune -o -name pde -prune -o \
		 -name \*.pom -exec /bin/bash -c 'gc_bundle "$@"' bash {} \; \
		 > gc-${iteration}.log
	# second "bash" is used as $0 in the function
	
	if [ ! -f toremove.txt ]
	then
		# no more garbage found
		break
	fi
	cat toremove.txt >> .logs/removedGarbage.txt
	for d in `cat toremove.txt`; do /bin/rm -r $d; done	
	/bin/rm toremove.txt
done

# merge gc logs:
cat gc-*.log | sort --unique > .logs/gc.log
/bin/rm gc-*.log

#==== remove all directories that have become empty: ====
for iteration in 1 2 3 4 5 ; do find -type d -empty -print \
 	-exec /bin/rmdir {} \; -prune; done \
 	>> .logs/empty-dirs.txt

echo "========== Repo reduced: =========="
/usr/bin/du -sc ${Repo}/*
/usr/bin/du -sc ${Repo}/org/*
/usr/bin/du -sc ${Repo}/org/eclipse/*
echo "==================================="

cd ${WORKSPACE}

#================================================================================
#   (3) Enrich POMs
#================================================================================
# Add some required information to the generated poms:
# - dynamic content (retrieved mostly from MANIFEST.MF):
#   - name
#   - url
#   - scm connection and tag
# - static content
#   - license
#   - organization
#   - issue management


echo "==== Enrich POMs ===="

cd ${Repo}

echo "platform"
java -jar ${ENRICH_POMS_JAR} `pwd`/org/eclipse/platform &> .logs/enrich-platform.txt
echo "jdt"
java -jar ${ENRICH_POMS_JAR} `pwd`/org/eclipse/jdt &> .logs/enrich-jdt.txt
echo "pde"
java -jar ${ENRICH_POMS_JAR} `pwd`/org/eclipse/pde &> .logs/enrich-pde.txt

echo "== updated checksums =="

function updateCheckSums() {
        /usr/bin/md5sum ${1} | cut -d " " -f 1 > ${1}.md5
        /usr/bin/sha1sum ${1} | cut -d " " -f 1 > ${1}.sha1
}

for pom in org/eclipse/{platform,jdt,pde}/*/*/*.pom
do
        updateCheckSums ${pom}
done

echo "==== Add Javadoc Stubs ===="

# (groupSimpleName, javadocArtifactGA)
function createJavadocs() {
	group=${1}
	jar="${1}-javadoc.jar"
	artifact=${2}
	if [ -r ${jar} ]
	then
		/bin/rm ${jar}
	fi
	echo "Corresponding javadoc can be found in artifact ${artifact}\n" > README.txt
	jar cf ${jar} README.txt
	for pom in org/eclipse/${group}/*/*/*.pom
	do
		javadoc=`echo ${pom} | sed -e "s|\(.*\)\.pom|\1-javadoc.jar|"`
		/bin/cp ${jar} ${javadoc}
	done	
}

createJavadocs platform org.eclipse.platform:org.eclipse.platform.doc.isv
createJavadocs jdt org.eclipse.jdt:org.eclipse.jdt.doc.isv
createJavadocs pde org.eclipse.pde:org.eclipse.pde.doc.user

echo "========== Repo completed ========="

cd ${WORKSPACE}
