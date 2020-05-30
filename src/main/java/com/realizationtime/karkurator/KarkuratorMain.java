package com.realizationtime.karkurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class KarkuratorMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    GridPane mainPanel = FXMLLoader.load(getClass().getResource("/MainView.fxml"));
    Scene scene = new Scene(mainPanel);
    primaryStage.setTitle("Karkurator");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
