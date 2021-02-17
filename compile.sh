#!/bin/bash

# Specify the JE files directory as the first command line argument when executing this file from the terminal.
#TODO: Set the paths of Java home and Jif home in the variables JAVA_HOME and JIFC_HOME respectively.
JAVA_HOME=<java-home-directory>
JIFC_HOME=<jif-home>


JIFC_BIN=${JIFC_HOME}/bin/
JIFC_PRINCIPALS=${JIFC_HOME}/tests/jif/principals/

JE_PATH=$1
mkdir ${JE_PATH}/generated-jif
JIF_PATH=${JE_PATH}/generated-jif
mkdir ${JE_PATH}/generated-java
JAVA_GENERATED_PATH=${JE_PATH}/generated-java

java -jar ./je-to-jiff-compiler/target/je-to-jiff-compiler-jar-with-dependencies.jar $JE_PATH $JIF_PATH $JAVA_GENERATED_PATH

echo "Compiling using Jif compiler..."
eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${JIF_PATH}/Encryptor.jif