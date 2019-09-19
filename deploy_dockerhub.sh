#!/bin/bash

docker login -u "$DOCKER_USER" -p "$DOCKER_PW"
docker push darakay/marcc-rest