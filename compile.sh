#!/bin/bash

#TODO replace the following JAVA_HOME, JIFC_PATH and JIFC_PRINCIPLES_PATH paths according to your system.
export JAVA_HOME="~/Aditya/Phd_Related/jvms/jdk1.8.0_241"
export JIFC_PATH=~/Softwares/Jif/jif-3.5.0/bin/
export JIFC_PRINCIPLES_PATH=~/Softwares/Jif/jif-3.5.0/tests/jif/principals/


export JE_PATH="./test-cases/src/main/je/de/tuda/stg/encryptor"

export JIF_PATH="./test-cases/src/main/generated-jif/de/tuda/stg/encryptor"

export JAVA_GENERATED_PATH="./test-cases/src/main/generatedJava/de/tuda/stg/encryptor"

Number_of_files=$(ls -1q *.je | wc -l)
echo "Number of JE files = $Number_of_files"
echo "Translating JE files..."

java -jar ./je-to-jiff-compiler/target/je-to-jiff-compiler-jar-with-dependencies.jar $JE_PATH $JIF_PATH

echo "Compiling using Jif compiler..."
eval "${JIFC_PATH}jifc" -classpath ${JIFC_PRINCIPLES_PATH} ${JIF_PATH}/Encryptor.jif