package com.realizationtime.karkurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class KarkuratorMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    GridPane mainPanel = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
    GridPane digitPane = (GridPane) mainPanel.lookup("#digit-pane");
    addDigitButtons(digitPane);
    Scene scene = new Scene(mainPanel);
    scene.getStylesheets().add("/style.css");
    primaryStage.setTitle("Karkurator");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private void addDigitButtons(GridPane digitPane) {
    int digit = 1;
    for (int row = 2; row >= 0; row--) {
      for (int column = 0; column < 3; column++) {
        int currentDigit = digit++;
        addDigitButton(digitPane, row, column, currentDigit);
      }
    }
    Button zero = addDigitButton(digitPane, 3, 0, 0);
    zero.getStyleClass().add("zeroButton");
    GridPane.setColumnSpan(zero, 2);

    Button comma = new Button(".");
    comma.setId("bComma");
    digitPane.add(comma, 2, 3);
    comma.getStyleClass().add("digitButton");
    GridPane.setHalignment(comma, HPos.CENTER);
  }

  private Button addDigitButton(GridPane digitPane, int row, int column, int digit) {
    Button b = new Button("" + digit);
    b.setId("button" + digit);
    b.getStyleClass().add("digitButton");
    digitPane.add(b, column, row);
    GridPane.setHalignment(b, HPos.CENTER);
    return b;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
