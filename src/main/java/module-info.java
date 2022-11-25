module com.thermostat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.thermostat to javafx.fxml;
    exports com.thermostat;
}