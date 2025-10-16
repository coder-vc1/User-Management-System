#!/bin/bash

# Download Maven
cd /tmp
curl -O https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xzf apache-maven-3.9.6-bin.tar.gz
export PATH=/tmp/apache-maven-3.9.6/bin:$PATH

# Build the project
cd /Users/ichankabir/Desktop/vicky-ps/backend
mvn clean compile -q