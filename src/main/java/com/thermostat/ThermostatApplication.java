package com.thermostat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ThermostatApplication extends Application {

    static ThermostatMainController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ThermostatApplication.class.getResource("temp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();
        stage.setTitle("Thermostat");
        stage.setScene(scene);
        stage.show();
        controller.testCom();

    }


    public static void main(String[] args) {
        launch();
        controller.clean();
    }
}