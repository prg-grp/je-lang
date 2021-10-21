#!/bin/bash
set -e

# Specify the JE files directory as the first command line argument when executing this file from the terminal.

# TODO: Make sure that Java 8 is in your system path.
# TODO: On the next line, set the path of Jif home in the variable JIF_HOME.
JIF_HOME=/Library/jif-3.5.0

if [ $# -eq 0 ]; then
    echo "No arguments provided, path the JE directory should be the first argument"
    exit 1
fi

JIFC_BIN=${JIF_HOME}/bin/
JIFC_PRINCIPALS=${JIF_HOME}/tests/jif/principals/

JE_PATH=$1
mkdir ${JE_PATH}/generated-jif
GENERATED_JIF_PATH=${JE_PATH}/generated-jif/
mkdir ${JE_PATH}/generated-java
GENERATED_JAVA_PATH=${JE_PATH}/generated-java/

java -jar ./je-to-jiff-compiler/target/je-to-jiff-compiler-jar-with-dependencies.jar $JE_PATH $GENERATED_JIF_PATH $GENERATED_JAVA_PATH

echo "Compiling using Jif compiler..."
eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/*
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/PasswordChecker.jif
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/DeclassifyInReturn.jif
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/Guess.jif ${GENERATED_JIF_PATH}/Grid.jif
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/StatRecord.jif ${GENERATED_JIF_PATH}/EncRecord.jif ${GENERATED_JIF_PATH}/StatUtil.jif
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/EncInt.jif ${GENERATED_JIF_PATH}/FilterNode.jif ${GENERATED_JIF_PATH}/EncEvent.jif ${GENERATED_JIF_PATH}/EncIntEvent.jif ${GENERATED_JIF_PATH}/GreaterPredicate.jif
#eval "${JIFC_BIN}/jifc" -robust -nooutput -classpath ${JIFC_PRINCIPALS} ${GENERATED_JIF_PATH}/Task.jif ${GENERATED_JIF_PATH}/TaskProcessor.jif