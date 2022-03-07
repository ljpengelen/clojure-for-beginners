# Clojure for Beginners

This is a small collection of Clojure exercises created for the presentation [Clojure for Beginners](https://github.com/ljpengelen/clojure-for-beginners-presentation).

Try it online: https://cfb.cofx.nl.

## Requirements

- [Java 8+](https://adoptium.net/)
- [Node.js and NPM](https://nodejs.org/)

## Development

Run `npm install` before you start development for the first time and each time you add a new JavaScript dependency.

If you're a beginner to Clojure and don't have a favorite setup yet, I recommend using [Visual Studio Code](https://code.visualstudio.com/) in combination with the [Calva extension](https://calva.io/).

Once you've installed Java, Node.js, NPM, Visual Studio Code, and Calva, [connect Calva to the project](https://calva.io/connect/) and start developing.

## Creating a release build

Execute the following command to create a release build:

```
npx shadow-cljs release app
```

The result can be found in `/dist`.
