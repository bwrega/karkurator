package com.realizationtime.karkurator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.net.URL;

public class KarkuratorMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Class<? extends KarkuratorMain> aClass = getClass();
    URL resource = aClass.getResource("/MainView.fxml");
    URL resource2 = getClass().getClassLoader().getResource("/MainView.fxml");
    String mainPanel = FXMLLoader.load(resource2);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
