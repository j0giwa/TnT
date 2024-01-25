#!/bin/sh
# set -x

export DATABASE_DIR=$(pwd)/data
export DATABASE_BINARY=${DATABASE_DIR}/server/h2-2.2.224.jar

echo "Starting H2 in TCP mode"
echo "Open http://localhost:8082/ in your browser"
java -cp ${DATABASE_BINARY} org.h2.tools.Server -web -tcp -ifNotExists -baseDir ${DATABASE_DIR}
