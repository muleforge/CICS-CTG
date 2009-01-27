#!/bin/bash
export CB2XSD_HOME=/home/makoto/java/seriola-esb/cb2xsd
cp /dev/null  cb2xml.properties 
java -cp ${CB2XSD_HOME}/target/cb2xsd-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/lib/cb2xml.jar package org.mule.providers.cics.tools.cb2xsd.Cb2Xsd $*

