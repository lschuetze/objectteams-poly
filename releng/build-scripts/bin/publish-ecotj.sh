# MANDATORY VARIABLE:
#   BUILDID
# OPTIONAL VARIABLES TO BE SUPPLIED VIA ENV:
# 	SIGN (unset or nosign)
# 	PROMOTE (unset or false or target directory)

BASE=`pwd`

# ABSOLUTE PATHS:
export ECOTJ_BASE=/home/data/httpd/download.eclipse.org/objectteams/ecotj


OTDTVERSION=`cat ${BASE}/testrun/build-root/src/finalFeaturesVersions.properties|grep "objectteams.otdt="|cut -d '=' -f 2`
if [ "${OTDTVERSION}" == "" ]
then
	echo "finalFeaturesVersions.properties not found, maybe build hasn't run successfully?"
	exit 3
fi 
echo "OTDTVERSION is $OTDTVERSION"

ECOTJ_DIR=${BASE}/testrun/ecj/${BUILDID}

echo "====Step 1: request signing ===="
cd ${ECOTJ_DIR}
ECOTJ_JAR=`ls ecotj-*[0-9].jar`
echo "ecotj is ${ECOTJ_JAR}"

ECOTJ_NAME="ecotj-${BUILDTYPE}-${OTDTVERSION}"
echo "ecotj name = ${ECOTJ_NAME}"

curl -o ${ECOTJ_NAME}-signed.jar -F file=@${ECOTJ_JAR} http://build.eclipse.org:31338/sign

echo "Signing completed"


echo "====Step 2: upload ===="

if [ "${PROMOTE}" != "false" ]
then
	cp ${ECOTJ_JAR} ${ECOTJ_BASE}/${ECOTJ_NAME}.jar
	cp ${ECOTJ_NAME}-signed.jar ${ECOTJ_BASE}/
	ls -latr ${ECOTJ_BASE}
fi
echo "====DONE===="
