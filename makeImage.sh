#!/bin/bash
VERSION=1.0.1
mvn clean package
docker build . -f Dockerfile -t audit-consumer-server:${VERSION}
docker image tag audit-consumer-server:${VERSION} repo.sadad.co.ir/repository/baam-docker-registry/audit-consumer-server:${VERSION}
docker push repo.sadad.co.ir/repository/baam-docker-registry/audit-consumer-server:${VERSION}