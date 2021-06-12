###############################################################################
# Copyright (c) 2010, 2016 Stephan Herrmann and others.
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Stephan Herrmann - initial API and implementation
###############################################################################

# Environment variables for the script otdt_runtests.sh

# EXPORT: Root location on build.eclipse.org:
BASEDIR=`pwd`

# EXPORT: root directory for building and testing:
OT_TESTSUITE_DIR=${BASEDIR}/testrun

# EXPORT: tmp directory for logging:
TMPDIR="${BASEDIR}/tmp"

# EXPORT: tmp directory for test files:
#TEST_TMPDIR="${HOME}/tmp"
TEST_TMPDIR=${JAVA_TMPDIR}

# EXPORT: directory for metadata from previous builds:
METADATA="${BASEDIR}/metadata"

# EXPORT:
FETCH_CACHE_LOCATION=${HOME}/gitCache

# EXPORT:
MAP_FILE_PATH=${BASEDIR}/releng/map/otdt.map

# Base dir for finding previous platform build:
DROPS4=http://download.eclipse.org/eclipse/downloads/drops4

# Configure ANT:
ANT_HOME=/shared/common/apache-ant-1.10.5/
PATH=${ANT_HOME}/bin:${PATH}

# Configure Java:
JAVA_HOME=${JAVA_HOME:="/shared/common/jdk-9_x64-latest"}
PATH=${JAVA_HOME}/bin:${PATH}

# EXPORT: additional arguments to pass to Ant:
ANT_PROFILE="-verbose"
#ANT_PROFILE=""

# EXPORT: Nice-level for the Ant process:
NICE="10"

# Architecture (as used by OSGi):
ARCH=`arch`

# VERSIONS:
# Eclipse SDK build identifier (used for substitution in otdt.map.in etc.):
SDK_QUALIFIER=${SDK_QUALIFIER:="I20210611-1600"}

# used only locally (components of the ECLIPSE_SDK_TGZ path):
EVERSION=${EVERSION:="I20210611-1600"}
DROP=${DROPS4}/${DROP:="I20210611-1600"}

# EXPORT: archive file of the base eclipse SDK build:
ECLIPSE_SDK_TGZ=${DROP}/eclipse-SDK-${EVERSION}-linux-gtk-${ARCH}.tar.gz

# EXPORT: archive file of the eclipse test framework:
ECLIPSE_TESTLIB_ZIP=${DROP}/eclipse-test-framework-${EVERSION}.zip

# EXPORT: where to find previously published plugins&features:
PUBLISHED_UPDATES=${HOME}/downloads/objectteams/updates/ot2.7/201812061254

