module com.realizationtime.karkurator {
  opens com.realizationtime.karkurator to javafx.graphics;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;
  requires static lombok;
}