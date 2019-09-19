#!/bin/bash +x

# THIS FILE IS EXECUTED AS AN INLINE SHELL JOB ON JENKINS

BASE=`pwd`
ECJ=${BASE}/testrun/ecj
ECOTJ_IN=`ls ${ECJ}/*-${buildIdECJ}/ecotj-*-${buildIdECJ}.jar`
ECOTJ_OUT="ecotj-${version}-${buildIdECJ}.jar"

echo "=== using ecotj from : ${ECOTJ_IN} ==="

# ABSOLUTE PATHS:
export STAGINGBASE=/opt/public/download-staging.priv/tools/objectteams
export DOWNLOAD_BASE=/home/data/httpd/download.eclipse.org/objectteams

if [ ! -d ${BASE}/metadata/*${buildId} ]
then
    echo "No metadata for buildId ${buildId} found among these:"
    ls ${BASE}/metadata
    exit 1
fi

cd ${ECJ}

echo "=== create signed variant of ecotj: ==="
if [ -d tmp ]
then
    rm -r tmp
fi
mkdir tmp
cd tmp
cp ${ECOTJ_IN} ${ECOTJ_OUT}
mkdir META-INF
cat > META-INF/eclipse.inf << HERE
jarprocessor.exclude.sign=false
jarprocessor.exclude.children=true
jarprocessor.exclude.pack=true
HERE
zip ${ECOTJ_OUT} META-INF/eclipse.inf
curl -o ecotj-${version}-${buildIdECJ}-signed.jar -F file=@${ECOTJ_OUT} http://build.eclipse.org:31338/sign

mv ${ECOTJ_OUT} ${ECOTJ_OUT}-unsigned
ls -l ${ECOTJ_OUT}*

echo "=== promote ecotj: ==="
cd ${ECJ}
cp ${ECOTJ_IN} ${DOWNLOAD_BASE}/ecotj/ecotj-${version}-${buildIdECJ}.jar
cp tmp/ecotj-${version}-${buildIdECJ}-signed.jar ${DOWNLOAD_BASE}/ecotj/
ls -latr ${DOWNLOAD_BASE}/ecotj/

echo "=== archive metadata ==="
cd ${BASE}/metadata
FULLVERSION=`ls -d *${buildId}`
cp -pr ${FULLVERSION} ${DOWNLOAD_BASE}/metadata
ls -latr ${DOWNLOAD_BASE}/metadata

echo "=== archive test results ==="
cd ${BASE}/testrun/test-root/eclipse/results
zip -r ${DOWNLOAD_BASE}/results/${FULLVERSION}.zip *
ls -latr ${DOWNLOAD_BASE}/results
