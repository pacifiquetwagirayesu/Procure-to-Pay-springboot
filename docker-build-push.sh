#!/bin/zsh

docker buildx build --platform linux/amd64,linux/arm64  -t procure-to-pay .
docker tag procure-to-pay paccy/procure-to-pay
docker push paccy/procure-to-pay