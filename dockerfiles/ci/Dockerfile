FROM ubuntu:focal

RUN apt-get update \
  && DEBIAN_FRONTEND=noninteractive apt-get install -y \
    default-jre \
    npm \
    wget \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /var/cache/apt/*

WORKDIR /app

COPY shadow-cljs.edn .
COPY package* ./

RUN npm install

RUN npx shadow-cljs npm-deps app
