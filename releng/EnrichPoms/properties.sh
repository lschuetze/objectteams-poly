#*******************************************************************************
# Copyright (c) 2016, 2017 GK Software AG and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Stephan Herrmann - initial API and implementation
#********************************************************************************

# ECLIPSE:
APP_NAME_P2DIRECTOR=org.eclipse.equinox.p2.director
DROPS4=/home/data/httpd/download.eclipse.org/eclipse/downloads/drops4
SDK_BUILD_DIR=I20181112-1800
SDK_VERSION=I20181112-1800
FILE_ECLIPSE=${DROPS4}/${SDK_BUILD_DIR}/eclipse-SDK-${SDK_VERSION}-linux-gtk-x86_64.tar.gz

# JDT / ECJ:
ECJ_VERSION=3.16.0-SNAPSHOT-I20181112-1800

# AGGREGATOR:
IU_AGG_PRODUCT=org.eclipse.cbi.p2repo.cli.product
URL_AGG_UPDATES=https://ci.eclipse.org/cbi/job/cbi.p2repo.aggregator_Gerrit/ws/org.eclipse.cbi.p2repo.aggregator/org.eclipse.cbi.p2repo.cli.product/target/repository/

# LOCAL TOOLS:
LOCAL_TOOLS=${WORKSPACE}/tools
DIR_AGGREGATOR=aggregator
AGGREGATOR=${LOCAL_TOOLS}/${DIR_AGGREGATOR}/cbiAggr
ECLIPSE=${LOCAL_TOOLS}/eclipse/eclipse

# ENRICH POMS tool:
ENRICH_POMS_JAR=${WORKSPACE}/work/EnrichPoms.jar
ENRICH_POMS_PACKAGE=org.eclipse.platform.releng.maven.pom

# AGGREGATION MODEL:
FILE_SDK_AGGR=${WORKSPACE}/work/SDK4Mvn.aggr
