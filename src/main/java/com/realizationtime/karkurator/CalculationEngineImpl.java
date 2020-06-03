package com.realizationtime.karkurator;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

  public record State(String accumulator, Optional<Character>operator, String operand, Optional<Operation> previousOperation) {

    public static State INITIAL_STATE = new State("", Optional.empty(), "", Optional.empty());

    public State addOperandCharacter(char digit) {
      if (wasLastOperationError()) {
        return this;
      }
      if (!accumulator.isEmpty() && operator.isEmpty()) {
        return INITIAL_STATE.addOperandCharacter(digit);
      }
      if (operand.contains(".") && digit == '.') {
        return this;
      }
      if (operand.isEmpty() && digit == '0') {
        return new State(accumulator, operator, "0", previousOperation);
      }
      String operandNormalized = operand.equals("0") ? "" : operand;
      if (operandNormalized.isEmpty() && digit == '.') {
        return new State(accumulator, operator, "0.", previousOperation);
      }
      return new State(accumulator, operator, operandNormalized + digit, previousOperation);
    }

    private String getOperandString() {
      if (operand.isEmpty()) {
        return "0";
      } else {
        return operand;
      }
    }

    public String getOutputString() {
      if (wasLastOperationError()) {
        return ERROR_MESSAGE;
      }
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
      if (wasLastOperationError()) {
        return this;
      }
      if (accumulator.isEmpty()) {
        return new State(getOperandString(), Optional.of(c), "", Optional.empty());
      }
      if (operand.isEmpty()) {
        return new State(accumulator, Optional.of(c), "", Optional.empty());
      }
      State operationResult = this.commitOperation();
      return new State(operationResult.accumulator, Optional.of(c), "", Optional.empty());
    }

    public State commitOperation() {
      if (wasLastOperationError()) {
        return this;
      }
      if (operator.isEmpty() && previousOperation.isEmpty()) {
        return this;
      }
      boolean shouldRepeatPreviousOperation = operator.isEmpty() && operand.isEmpty();
      BigDecimal accumulator = new BigDecimal(this.accumulator);
      String operandStr = shouldRepeatPreviousOperation ?
        previousOperation.get().operand
        : this.getOperandString();
      BigDecimal operand = new BigDecimal(operandStr);
      BigDecimal newAccumulator = null;
      char operator = shouldRepeatPreviousOperation ?
        previousOperation.get().operator
        : this.operator.get();
      if (operator == '+') {
        newAccumulator = processResult(accumulator.add(operand));
      }
      if (operator == '-') {
        newAccumulator = processResult(accumulator.subtract(operand));
      }
      if (operator == '*') {
        newAccumulator = processResult(accumulator.multiply(operand));
      }
      if (operator == '/') {
        try {
          newAccumulator = processResult(accumulator.divide(operand, MAX_PRECISION, RoundingMode.HALF_UP));
        } catch (ArithmeticException ex) {
          return new State(this.accumulator, Optional.empty(), "", Optional.of(new Operation(operator, operandStr, true)));
        }
      }
      Operation newPreviousOperation = new Operation(operator, operandStr, false);
      return new State(newAccumulator.toPlainString(), Optional.empty(), "", Optional.of(newPreviousOperation));
    }

    private BigDecimal processResult(BigDecimal rawResult) {
      return rawResult.stripTrailingZeros();
    }

    private boolean wasLastOperationError() {
      return previousOperation.map(Operation::error).orElse(false);
    }

    private record Operation(char operator, String operand, boolean error) {}
  }



}
