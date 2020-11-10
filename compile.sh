#!/bin/bash

#TODO replace the following JAVA_HOME, JIFC_BIN and JIFC_PRINCIPALS paths according to your system.
export JAVA_HOME="~/Aditya/Phd_Related/jvms/jdk1.8.0_241"
export JIFC_BIN=~/Softwares/Jif/jif-3.5.0/bin/
export JIFC_PRINCIPALS=~/Softwares/Jif/jif-3.5.0/tests/jif/principals/


export JE_PATH="./test-cases/src/main/je/de/tuda/prg/encryptor/"
export JIF_PATH="./test-cases/src/main/generated-jif/de/tuda/prg/encryptor/"
export JAVA_GENERATED_PATH="./test-cases/src/main/generatedjava/de/tuda/prg/encryptor/"

Number_of_files=$(ls -1q *.je | wc -l)
echo "Number of JE files = $Number_of_files"
echo "Translating JE files..."

java -jar ./je-to-jiff-compiler/target/je-to-jiff-compiler-jar-with-dependencies.jar $JE_PATH $JIF_PATH $JAVA_GENERATED_PATH

echo "Compiling using Jif compiler..."
eval "${JIFC_BIN}jifc" -classpath ${JIFC_PRINCIPALS} ${JIF_PATH}/Encryptor.jif