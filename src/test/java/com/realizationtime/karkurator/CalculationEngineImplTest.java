package com.realizationtime.karkurator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculationEngineImplTest {

  private final CalculationEngine engine = new CalculationEngineImpl();
  private String output;

  @BeforeEach
  void setUp() {
    engine.addOutputConsumer(o -> output = o);
  }

  @Test
  void shouldDisplayZeroAfterReset() {
    // when
    engine.resetState();

    // then
    assertThat(output).isEqualTo("0");
  }

  @Test
  void afterTypingDigitsTheyShouldBeDisplayed() {
    // when
    engine.consumeInput("1234");

    // then
    assertThat(output).isEqualTo("1234");

    // given
    engine.resetState();

    // when
    engine.consumeInput("00001234");

    // then
    assertThat(output).isEqualTo("1234");

    // given
    engine.resetState();

    // when
    engine.consumeInput("1234000");

    // then
    assertThat(output).isEqualTo("1234000");
  }

  @Test
  void shouldHandleComma() {
    // when
    engine.consumeInput("56.02");

    // then
    assertThat(output).isEqualTo("56.02");

    // given
    engine.resetState();

    // when
    engine.consumeInput(".02");

    // then
    assertThat(output).isEqualTo("0.02");
  }

  @Test
  void shouldIgnoreSecondComma() {
    // when
    engine.consumeInput("56.02.5");

    // then
    assertThat(output).isEqualTo("56.025");
  }

  @Test
  void shouldDisplayNewOperandForNewOperation() {
    // when
    engine.consumeInput("78+23");

    // then
    assertThat(output).isEqualTo("+ 23");
  }

  @Test
  void shouldDisplayAccumulatorAfterPressingOperator() {
    // when
    engine.consumeInput("78+");

    // then
    assertThat(output).isEqualTo("78");
  }

  @Test
  void shouldAddNumbers() {
    // when
    engine.consumeInput("78+23=");

    // then
    assertThat(output).isEqualTo("101");

    // given
    engine.resetState();

    // when
    engine.consumeInput("78+23+15=");

    // then
    assertThat(output).isEqualTo("116");
  }

  @Test
  void shouldSubtractNumber() {
    // then
    engine.consumeInput("89.07-4=");

    // then
    assertThat(output).isEqualTo("85.07");
  }

  @Test
  void shouldMultiplyNumbers() {
    // then
    engine.consumeInput("9.07*2=");

    // then
    assertThat(output).isEqualTo("18.14");

    // given
    engine.resetState();

    // then
    engine.consumeInput("*5+");

    // then
    assertThat(output).isEqualTo("0");
  }

  @Test
  void shouldDivideNumbers() {
    // then
    engine.consumeInput("9.07/2=");

    // then
    assertThat(output).isEqualTo("4.535");

    // given
    engine.resetState();

    // then
    engine.consumeInput("7/2=");

    // then
    assertThat(output).isEqualTo("3.5");
  }

  @Test
  void shouldChangeOperator() {
    // then
    engine.consumeInput("89.07+-4=");

    // then
    assertThat(output).isEqualTo("85.07");

    // given
    engine.resetState();

    // then
    engine.consumeInput("89.07-+4=");

    // then
    assertThat(output).isEqualTo("93.07");

    // given
    engine.resetState();

    // then
    engine.consumeInput("89.07*+4=");

    // then
    assertThat(output).isEqualTo("93.07");
  }

  @Test
  void shouldReset() {
    // then
    engine.consumeInput("89.07+4+9C");

    // then
    assertThat(output).isEqualTo("0");
  }

}