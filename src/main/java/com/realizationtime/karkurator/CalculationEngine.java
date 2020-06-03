package com.realizationtime.karkurator;

import java.util.function.Consumer;

public interface CalculationEngine {

  int MAX_PRECISION = 15;
  String ERROR_MESSAGE = "Error";

  void resetState();
  void addOutputConsumer(Consumer<String> consumer);
  void consumeInput(Character c);
  default void consumeInput(String input) {
    input.chars()
      .forEach(c -> consumeInput((char)c));
  }

}
