#!/bin/bash

echo "deploying application ..."

sbt dist

cd target/universal

unzip restapi-1.0.zip

echo "Application is ready! Run start.sh <path of input file>"