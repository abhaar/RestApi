#!/bin/bash

if [ $# -ne 1 ]
    then
    echo "Please provide path of the input file. Only 1 argument is allowed."
    exit 1
fi

cd target/universal/restapi-1.0/bin

chmod u+x restapi

./restapi -Dplay.http.secret.key=prod -Dfilename=$1