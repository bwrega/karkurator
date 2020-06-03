# Karkurator

A sample project that helps people learn basics of Java programming -
a calculator with JavaFX GUI.

## Building application

```shell script
./gradlew clean build
```

or under Windows:

```shell script
./gradlew.bat clean build
```

JDK 14 (or newer) is required under JAVA_HOME environment variable.

### Running application

```shell script
./gradlew run
```

### Running application under Intellij Idea

Set
`File | Settings | Build, Execution, Deployment | Build Tools | Gradle`

`Build and run using` and `Run tests using ` to `Intellij Idea`.

Add to run configuration:
```
--patch-module com.realizationtime.karkurator=out/production/resources
```

## How to use this project for learning programming

You can learn how the program works and extend it.

Here are some ideas for what can you do here:

### Implement subtraction and multiplication

See how does adding and division work - and implement subtraction and multiplication.

Pr0 tip: actually, it's implemented. All you have to do is to uncomment some code.

### Change display font

Change font used for "display" to something that looks like an LCD display.

### Implement keyboard

Implement handling calculator using keyboard. Especially - numerical keyboard.

### Reimplement CalculationEngine

Provide your own implementation of CalculationEngine interface and use it in KarkuratorMain.

### Random colors on button press

Implement such behaviour, that when any button is clicked - all elements get random colors.
Those random colors may be selected from a pre selected palette.

### Implement memory

Implement calculator memory (buttons M+, MR, MC on a normal calculator).
