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
SDK_BUILD_DIR=S-4.8RC4a-201806110500
SDK_VERSION=4.8RC4a
FILE_ECLIPSE=${DROPS4}/${SDK_BUILD_DIR}/eclipse-SDK-${SDK_VERSION}-linux-gtk-x86_64.tar.gz

# JDT / ECJ:
ECJ_VERSION=3.14.0

# AGGREGATOR:
IU_AGG_PRODUCT=org.eclipse.cbi.p2repo.cli.product
URL_AGG_UPDATES=http://download.eclipse.org/cbi/updates/aggregator/headless/4.8/I20180518-0759

# LOCAL TOOLS:
LOCAL_TOOLS=${WORKSPACE}/tools
DIR_AGGREGATOR=aggregator
AGGREGATOR=${LOCAL_TOOLS}/${DIR_AGGREGATOR}/cbiAggr
ECLIPSE=${LOCAL_TOOLS}/eclipse/eclipse

# ENRICH POMS tool:
ENRICH_POMS_JAR=${WORKSPACE}/../../pomEnricher/workspace/EnrichPoms.jar

# AGGREGATION MODEL:
FILE_SDK_AGGR=${WORKSPACE}/SDK4Mvn.aggr
