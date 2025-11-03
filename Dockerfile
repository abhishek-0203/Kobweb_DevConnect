FROM ubuntu:latest
LABEL authors="abhishek"

ENTRYPOINT ["top", "-b"]