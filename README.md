# snake-game

This is my solution to a Java assignment. The assignment was to implement a
snake game using MVC and make it multithreaded. There should be 3 threads:
UI thread, game thread, and "food" thread (the food thread is responsible
for generating food spawn locations).

## Dependencies & Java Version

This is a Maven / JavaFX project built with Java 19.

## Overview

The entry point for this project is the `com.github.mfnsvrtm.snakegame.App`
class. It does one thing: loads and sets the primary (and the only) scene.
This scene is defined by the `game.fxml` markup file as well as the
`style.css` style file (look for them in the `/src/main/resources` folder).
Finally, this scene has an associated `GameController` controller.

There are 4 top level packages: `controller`, `logic`, `model`, and
`threading`.

- `controller` contains the `GameController` class mentioned above.
- `logic` contains the basic, core implementation of the Snake game.
  It's UI agnostic and single threaded. `Game` is the main class.
- `model` contains immutable classes that used to describe game state.
  `Game` uses these classes as part of its public API.
- `threading` contains multithreading related classes. `ThreadedGame`
  being the primary one. It's a wrapper around `Game` that sets up 2
  threads (game thread and food thread) and manages communication between
  these and the main thread.
