#!/bin/bash
export CB2XSD_HOME=/home/makoto/java/mule-transport-cicsctg/trunk/tools
echo $CB2XSD_HOME
java -cp ${CB2XSD_HOME}/target/mule-cics-tools-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/lib/poi-2.5.1.jar org.mule.providers.cics.tools.xls2xml.Xls2Xml $*

