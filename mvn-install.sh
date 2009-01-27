HOME=/home/mule
mvn install:install-file -DgroupId=cb2xml -DartifactId=cb2xml -Dversion=0.93 -Dpackaging=jar -Dfile=$HOME/jar/cb2xml.jar

#mvn install:install-file -DgroupId=javax.resource -DartifactId=connector-api \
#    -Dversion=1.3.1 -Dpackaging=jar -Dfile=$HOME/jar/connector-api.jar

mvn install:install-file -DgroupId=seriola -DartifactId=header \
    -Dversion=1.0 -Dpackaging=jar -Dfile=$HOME/jar/header.jar

mvn install:install-file -DgroupId=com.ibm -DartifactId=cicseci \
    -Dversion=1.3.0 -Dpackaging=jar -Dfile=$HOME/jar/cicseci.jar

mvn install:install-file -DgroupId=com.ibm -DartifactId=ctgclient \
    -Dversion=1.3.0 -Dpackaging=jar -Dfile=$HOME/jar/ctgclient.jar

mvn install:install-file -DgroupId=com.ibm -DartifactId=cicsframe \
    -Dversion=1.3.0 -Dpackaging=jar -Dfile=$HOME/jar/cicsframe.jar


