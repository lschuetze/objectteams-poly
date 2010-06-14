#!/bin/sh

ROOT=/shared/tools/objectteams
LIB=${ROOT}/lib

OTRE_MIN=${LIB}/otre_min.jar
OTRE_AGENT=${LIB}/otre_agent.jar
OTRE_JAR=${LIB}/otre.jar
BCEL_JAR=${LIB}/org.apache.bcel.jar

SVN_ROOT=svn://build/svnroot/tools/org.eclipse.objectteams/
OTRE_SRC=trunk/othersrc/OTRE

# EXPORT & PREPARE:
# svn export ${SVN_ROOT}/${OTRE_SRC}
# mkdir OTRE/bin

# UPDATE FROM SVN:
cd ${ROOT}/OTRE
svn up

# COMPILE
cd ${ROOT}/OTRE/src
javac -g -d ../bin -classpath ${BCEL_JAR}:. `find . -name \*.java`

cd ${ROOT}/OTRE/bin

# PACKAGE otre_min.jar
jar cvf ${OTRE_MIN} org/objectteams

# PACKAGE otre_agent.jar
jar cvfm ${OTRE_AGENT} ../MANIFEST.MF org/eclipse/objectteams/otre/jplis/otreAgent.class

# PACKAGE otre.jar
jar cvf ${OTRE_JAR} org
cd ${ROOT}/OTRE/src
zip ${OTRE_JAR} `find . -name \*.java`
