package com.realizationtime.karkurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class KarkuratorMain extends Application {

  private static final String WINDOW_TITLE = "Karkurator";

  private final CalculationEngine calculationEngine = new CalculationEngineImpl();
  private GridPane mainPanel;

  @Override
  public void start(Stage primaryStage) throws IOException {
    mainPanel = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
    addDigitButtons();
    connectOperatorButtons();
    setupDisplay();
    setupStage(primaryStage);
  }

  private void addDigitButtons() {
    GridPane digitPane = (GridPane) mainPanel.lookup("#digit-pane");
    addDigitButtons(digitPane, calculationEngine::consumeInput);
  }

  private void connectOperatorButtons() {
    connectOperatorButton("#buttonAdd", '+');
    // uncomment these lines to enable subtraction and multiplication
//    connectOperatorButton("#buttonSubtract", '-');
//    connectOperatorButton("#buttonMultiply", '*');
    connectOperatorButton("#buttonDivide", '/');
    connectOperatorButton("#buttonEquals", '=');
    connectOperatorButton("#buttonClear", 'C');
  }

  private void connectOperatorButton(String buttonId, char operator) {
    Button button = (Button) mainPanel.lookup(buttonId);
    button.setOnAction(event -> calculationEngine.consumeInput(operator));
  }

  private void setupDisplay() {
    TextField display = (TextField) mainPanel.lookup(".display");
    calculationEngine.addOutputConsumer(display::setText);
  }

  private void setupStage(Stage primaryStage) {
    Scene scene = new Scene(mainPanel);
    scene.getStylesheets().add("/style.css");
    primaryStage.setTitle(WINDOW_TITLE);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private void addDigitButtons(GridPane digitPane, Consumer<Character> characterConsumer) {
    int digit = 1;
    for (int row = 2; row >= 0; row--) {
      for (int column = 0; column < 3; column++) {
        int currentDigit = digit++;
        addDigitButton(digitPane, row, column, currentDigit, characterConsumer);
      }
    }
    Button zero = addDigitButton(digitPane, 3, 0, 0, characterConsumer);
    zero.getStyleClass().add("zeroButton");
    GridPane.setColumnSpan(zero, 2);

    Button comma = new Button(".");
    comma.setId("bComma");
    digitPane.add(comma, 2, 3);
    comma.getStyleClass().add("digitButton");
    GridPane.setHalignment(comma, HPos.CENTER);
    comma.setOnAction(event -> characterConsumer.accept('.'));
  }

  private Button addDigitButton(GridPane digitPane, int row, int column, int digit, Consumer<Character> characterConsumer) {
    Button b = new Button("" + digit);
    b.setId("button" + digit);
    b.getStyleClass().add("digitButton");
    digitPane.add(b, column, row);
    GridPane.setHalignment(b, HPos.CENTER);
    b.setOnAction(actionEvent -> characterConsumer.accept((char) ('0' + digit)));
    return b;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
