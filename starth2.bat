@echo OFF
SET DB_PATH=./data/server
SET H2_BINARY=%DB_PATH%/h2-2.2.224.jar

REM starting the actual server with web administration and tcp listener
REM all databases resists in the data folder and can be created from the
REM web console
java -cp %H2_BINARY% org.h2.tools.Server -web -tcp -ifNotExists -baseDir ./data/
