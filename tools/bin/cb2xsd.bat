@echo off
echo "" > cb2xml.properties
java -cp ../lib/cb2xsd-1.0-SNAPSHOT.jar;../lib/cb2xml.jar jp.co.ogis_ri.seriola.esb.tools.cb2xsd.Cb2Xsd %1 %2 %3 %4 %5 %6


