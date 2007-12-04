#!/bin/bash
export CB2XSD_HOME=/home/makoto/java/seriola-esb/cb2xsd
java -cp ${CB2XSD_HOME}/target/cb2xsd-1.0-SNAPSHOT.jar:${CB2XSD_HOME}/lib/poi-2.5.1.jar:${CB2XSD_HOME}/lib/xbean.jar:${CB2XSD_HOME}/lib/jsr173_1.0_api.jar jp.co.ogis_ri.seriola.esb.tools.xsd2xls.Xsd2Xls $*

