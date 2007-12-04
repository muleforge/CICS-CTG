@echo off
java -cp .;..\lib\cb2xsd-1.0-SNAPSHOT.jar;..\lib\poi-2.5.1.jar;..\lib\xbean.jar;..\lib\jsr173_1.0_api.jar jp.co.ogis_ri.seriola.esb.tools.xsd2xls.Xsd2Xls %1 
