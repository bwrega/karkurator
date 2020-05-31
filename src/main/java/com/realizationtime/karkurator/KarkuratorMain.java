package com.realizationtime.karkurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
        Button b = new Button("" + currentDigit);
        b.setId("b" + currentDigit);
        digitPane.add(b, column, row);
      }
    }
    Button zero = new Button("0");
    zero.setId("b0");
    digitPane.add(zero, 0, 3, 1, 2);
    Button comma = new Button(".");
    comma.setId("bComma");
    digitPane.add(comma, 2, 3);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
