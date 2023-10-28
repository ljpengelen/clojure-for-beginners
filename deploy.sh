#! /bin/sh

rm -rf dist
git clone https://github.com/ljpengelen/clojure-for-beginners.git -b deploy dist
cd dist && rm -rf * && cd ..

npx shadow-cljs release app

cd dist && git add . && git commit -am "Deploy" && git push && cd ..
