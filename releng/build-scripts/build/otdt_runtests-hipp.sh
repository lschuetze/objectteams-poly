#! /bin/bash
# Copyright (c) 2010 Stephan Herrmann.
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

# =============================================================================
# MAIN BUILD AND TEST SCRIPT FOR THE OBJECT TEAMS DEVELOPMENT TOOLING (OTDT)
# =============================================================================
# INPUT: Variables from otdt_prerequisites:
# -----------------------------------------------------------------------------
# TMPDIR                for log output
# TEST_TMPDIR           for temp test files
# OT_TESTSUITE_DIR      root directory for building and testing
# METADATA				directory for metadata from previous builds
# SDK_QUALIFIER			build qualifier of the base eclipse SDK
# ECLIPSE_SDK_TGZ       archive file of the base eclipse SDK build (full path)
# ECLIPSE_TESTLIB_ZIP   archive file of the eclipse test framework (full path)
# PUBLISHED_UPDATES 	directory of previously published plugins&features
# FETCH_CACHE_LOCATION	git working area holding caches for fetch
# MAP_FILE_PATH			path to the otdt.map file (original location of otdt.map.in)
# ANT_PROFILE           configure the ant process
# SIGN					"nosign" or empty
# =============================================================================
# OUTPUT: Variables passed to the toplevel ant script
# -----------------------------------------------------------------------------
## As Environment Variables:
##      ANT_OPTS                configure Ant
## As Ant Arguments (from ANT_PROFILE):
##      -verbose                configure Ant
## As Java Properties:
##      -Declipse-app.tgz       path of eclipse SDK
##      -Declipse.tests.zip     path of eclipse test framework
##		-Dpublished.updates		path to previously published things
##      -Ddo.run.tests          true|false: should test be run?
##    	-DfetchCacheLocation    git working area holding caches for fetch
##		-Dmap.file.path			path to the otdt.map file (original location of otdt.map.in)
##		-D_hasSaxon.jar			to prevent copying of saxon8.jar (was needed on build.eclipse.org)
# =============================================================================

# CONSTANTS (FOR NOW):
# option baseRepo is currently broken
baseRepo=none
export UPDATE_SITE_BASE=ot2.8
export OT_VERSION=2.8.2
# during the build we always publish to 'staging':
export PROMOTE=staging

usage()
{
	echo "Usage:"
	echo "$0 [-b|-nobuild]"
        echo "  -b:         build OTDT only, no testing."
        echo "  -nobuild:   don't build OTDT, directly invoke testing."
}

cleanup()
{
	echo "cleanup(): Currently no cleanup is configured"
}

_prefix=`dirname $0`
_prefix=`readlink -f $_prefix`
. "${_prefix}/otdt_prerequisites-hipp.sh"

echo "=== Sourced otdt_prerequisites-hipp.sh ==="
env
echo "====================================="

#LOCAL: log file:
OT_SUITE_LOG=$TMPDIR/ot-testsuite.log

# LOCAL: the initial ant build file:
BUILDFILE="${_prefix}/run.xml"

#LOCAL: main ant target:
MAIN_TARGET=${MAIN_TARGET:="ot-junit-all"}

#LOCAL: should the tests be run?
DO_RUN="true"

case ${MAIN_TARGET} in
	"ot-compiler-build")
		;&
	"ot-junit-build")
		DO_RUN="false"
		;;
esac

# start working:

test -d "$TMPDIR" || mkdir -p "$TMPDIR"
test -d "$OT_TESTSUITE_DIR" || mkdir -p "$OT_TESTSUITE_DIR"
cd "$OT_TESTSUITE_DIR"


# preload metadata for appending:
if [ -f "${METADATA}/content.xml" ]
then
    mkdir -p metadata
    cp ${METADATA}/*.xml metadata
fi

trap "echo Aborting by SIGTERM; cleanup; exit 130" INT

# Assemble the Ant call:
ANT_OPTIONS="${ANT_PROFILE} \
    -Declipse-app.tgz=${ECLIPSE_SDK_TGZ} \
    -Declipse.tests.zip=${ECLIPSE_TESTLIB_ZIP} \
    -Declipse.sdk.qualifier=${SDK_QUALIFIER} \
    -Dpublished.updates=${PUBLISHED_UPDATES} \
    -Ddo.run.tests=${DO_RUN} \
    -Dtest.tmpDir=${TEST_TMPDIR} \
    -DfetchCacheLocation=${FETCH_CACHE_LOCATION} \
    -Dmap.file.path=${MAP_FILE_PATH} \
    -D_hasSaxon.jar=true"

ANT_OPTS="-Xmx1024m"
export ANT_OPTS

# 1. build OTDT
# 2. create & publish the update site
# 3. run tests using (1)
ant -f ${BUILDFILE} ${ANT_OPTIONS} createOTDTEclipse &&  \
	( cd .. ; ./releng/build-scripts/bin/createRepository-hipp.sh ${baseRepo} ${UPDATE_SITE_BASE} ${OT_VERSION} ) && \
	ant -f ${BUILDFILE} ${ANT_OPTIONS} ${MAIN_TARGET}

trap - INT


