package com.realizationtime.karkurator;

import java.util.function.Consumer;

public interface CalculationEngine {

  void resetState();
  void addOutputConsumer(Consumer<String> consumer);
  void consumeInput(Character c);
  void consumeInput(String input);

}
