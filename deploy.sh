#!/bin/bash

if [ -z "$1" ] 
    then 
        echo "Specify release version as first argument"
	exit 0
fi

RELEASEVERSION=${1}

echo "Releasing with version: $RELEASEVERSION"

# mvn versions:set -DnewVersion=$RELEASEVERSION
mvn clean dependency:unpack package deploy -Dmaven.test.skip=true
# cd jsondoc-ui-webjar/
# mvn clean dependency:unpack package deploy -Dmaven.test.skip=true
# mvn versions:commit





