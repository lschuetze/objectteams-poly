#!/bin/sh
#*******************************************************************************
# Copyright (c) 2017 GK Software AG and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Stephan Herrmann - initial API and implementation
#********************************************************************************

REPO_BASE=${WORKSPACE}/../../CBIaggregator/workspace
REPO=${REPO_BASE}/repo-${REPO_ID}
PDE=org/eclipse/pde

# load versions from the baseline (to avoid illegal double-upload):
source ${WORKSPACE}/baseline.txt

if [ ! -d ${REPO} ]
then
	echo "No repo at ${REPO}"
	exit 1
fi

echo "==== Copy artifacts from ${REPO}/${PDE} ===="

if [ -d ${PDE} ]
then
	/bin/rm -r ${PDE}/*
else
	mkdir -p ${PDE}
fi
cp -r ${REPO}/${PDE}/* ${PDE}/


echo "==== UPLOAD ===="

URL=https://oss.sonatype.org/service/local/staging/deploy/maven2/
REPO=ossrh
SETTINGS=/opt/public/hipp/homes/genie.releng/.m2/settings-deploy-ossrh-pde.xml
MVN=/shared/common/apache-maven-latest/bin/mvn

/bin/mkdir .log

function same_as_baseline() {
	simple=`basename $1`
	name=`echo $simple | sed -e "s|\(.*\)-.*|\1|" | tr '.' '_'`
	version=`echo $simple | sed -e "s|.*-\(.*\).pom|\1|"`
	base_version=`eval echo \\${VERSION_$name}`
	if [[ $base_version =~ $version ]]
	then
		return 0
	else
		echo "different versions for ${name}: ${version} is not in ${base_version}"
		return 1
	fi
}

for pomFile in org/eclipse/pde/*/*/*.pom
do
  if same_as_baseline $pomFile
  then
	  echo "Skipping file $pomFile which is already present in the baseline"
  else
	  file=`echo $pomFile | sed -e "s|\(.*\)\.pom|\1.jar|"`
	  sourcesFile=`echo $pomFile | sed -e "s|\(.*\)\.pom|\1-sources.jar|"`
	  javadocFile=`echo $pomFile | sed -e "s|\(.*\)\.pom|\1-javadoc.jar|"`
	
	  echo "${MVN} -f pde-pom.xml -s ${SETTINGS} gpg:sign-and-deploy-file -Durl=${URL} -DrepositoryId=${REPO} -Dfile=${file} -DpomFile=${pomFile}"
	  
	  ${MVN} -f pde-pom.xml -s ${SETTINGS} gpg:sign-and-deploy-file \
	     -Durl=${URL} -DrepositoryId=${REPO} \
	     -Dfile=${file} -DpomFile=${pomFile} \
	     >> .log/artifact-upload.txt
	     
	  echo -e "\t${sourcesFile}"
	  ${MVN} -f pde-pom.xml -s ${SETTINGS} gpg:sign-and-deploy-file \
	     -Durl=${URL} -DrepositoryId=${REPO} \
	     -Dfile=${sourcesFile} -DpomFile=${pomFile} -Dclassifier=sources \
	     >> .log/sources-upload.txt
	  
	  echo -e "\t${javadocFile}"
	  ${MVN} -f pde-pom.xml -s ${SETTINGS} gpg:sign-and-deploy-file \
	     -Durl=${URL} -DrepositoryId=${REPO} \
	     -Dfile=${javadocFile} -DpomFile=${pomFile} -Dclassifier=javadoc \
	     >> .log/javadoc-upload.txt
  fi
done

/bin/ls -la .log

/bin/grep -i fail .log/*

