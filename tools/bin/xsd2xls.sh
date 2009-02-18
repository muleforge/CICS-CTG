#!/bin/bash

export CB2XSD_HOME=/home/makoto/java/mule-transport-cicsctg/trunk/tools

java -cp ${CB2XSD_HOME}/target/mule-cics-tools-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/lib/poi-2.5.1.jar:${CB2XSD_HOME}/lib/xbean.jar:${CB2XSD_HOME}/lib/jsr173_1.0_api.jar org.mule.providers.cics.tools.xsd2xls.Xsd2Xls $*
