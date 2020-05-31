package com.realizationtime.karkurator;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class CalculationEngineImpl implements CalculationEngine {

  private static final Set<Character> MATH_OPERATORS = Set.of('+', '-', '/', '*');

  @Getter
  private State state = State.INITIAL_STATE;

  private final List<Consumer<String>> outputConsumers = new ArrayList<>();

  @Override
  public void resetState() {
    state = State.INITIAL_STATE;
    displayState();
  }

  private void displayState() {
    display(state.getOutputString());
  }

  private void display(String output) {
    outputConsumers.forEach(consumer -> consumer.accept(output));
  }

  @Override
  public void addOutputConsumer(Consumer<String> consumer) {
    this.outputConsumers.add(consumer);
  }

  @Override
  public void consumeInput(Character c) {
    if (c>='0' && c<='9' || c == '.') {
      state = state.addOperandCharacter(c);
      displayState();
    } else if (MATH_OPERATORS.contains(c)) {
      state = state.addOperator(c);
      displayState();
    } else if (c == '=') {
      state = state.commitOperation();
      displayState();
    } else if (c == 'C') {
      resetState();
    }

  }

  @Override
  public void consumeInput(String input) {
    input.chars()
      .forEach(c -> consumeInput((char)c));
  }

  public record State(String accumulator, Optional<Character>operator, String operand) {
    public static State INITIAL_STATE = new State("", Optional.empty(), "");

    public State addOperandCharacter(char digit) {
      if (operand.contains(".") && digit == '.') {
        return this;
      }
      if (operand.isEmpty() && digit == '0') {
        return this;
      }
      if (operand.isEmpty() && digit == '.') {
        return new State(accumulator, operator, "0.");
      }
      return new State(accumulator, operator, operand + digit);
    }

    private String getOperandString() {
      if (operand.isEmpty()) {
        return "0";
      } else {
        return operand;
      }
    }

    public String getOutputString() {
      if (operand.isEmpty() && !accumulator.isEmpty()) {
        return accumulator;
      }
      if (operator.isEmpty()) {
        return getOperandString();
      } else {
        return "%s %s".formatted(operator.get(), getOperandString());
      }
    }

    public State addOperator(Character c) {
      if (accumulator.isEmpty()) {
        return new State(getOperandString(), Optional.of(c), "");
      }
      if (operand.isEmpty()) {
        return new State(accumulator, Optional.of(c), "");
      }
      State operationResult = this.commitOperation();
      return new State(operationResult.accumulator, Optional.of(c), "");
    }

    public State commitOperation() {
      if (operator.isEmpty()) {
        return this;
      }
      BigDecimal accumulator = new BigDecimal(this.accumulator);
      BigDecimal operand = new BigDecimal(this.getOperandString());
      BigDecimal newAccumulator = null;
      if (operator.get() == '+') {
        newAccumulator = accumulator.add(operand);
      }
      if (operator.get() == '-') {
        newAccumulator = accumulator.subtract(operand);
      }
      if (operator.get() == '*') {
        newAccumulator = accumulator.multiply(operand);
      }
      if (operator.get() == '/') {
        newAccumulator = accumulator.divide(operand);
      }
      return new State(newAccumulator.toString(), Optional.empty(), "");
    }
  }

}
