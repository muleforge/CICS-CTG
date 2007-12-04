#!/bin/bash
export CB2XSD_HOME=/home/makoto/java/seriola-esb/cb2xsd
java -cp ${CB2XSD_HOME}/target/cb2xsd-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/lib/poi-2.5.1.jar jp.co.ogis_ri.seriola.esb.tools.xls2xml.Xls2Xml $*

