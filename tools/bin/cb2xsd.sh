#!/bin/bash
export CB2XSD_HOME=/home/makoto/java/mule-transport-cicsctg/trunk/tools

cp /dev/null  cb2xml.properties 
java -cp ${CB2XSD_HOME}/target/mule-cics-tools-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/target/mule-cics-tools-1.0-SNAPSHOT.jar org.mule.providers.cics.tools.cb2xsd.Cb2Xsd $*

