package com.thermostat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class ThermostatMainController implements Initializable {
    @FXML
    private Label labelSetTemp;
    @FXML
    private Label labelCurrTemp;
    @FXML
    private Label labelHumidity;
    @FXML
    private AnchorPane scenePane;
    @FXML
    private Button buttonIncr;
    @FXML
    private Button buttonDecr;
    @FXML
    private Button buttonSchedule;

    private static final Thermostat thermostat = new Thermostat();
    private ThermostatMainController controller;
    private Timer tempTimer;
    private Timer humTimer;
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.tempTimer = new Timer();
        this.humTimer = new Timer();
        tempTimer.scheduleAtFixedRate(new TempUpdateThread(), 0, 10 * 1000);
        humTimer.scheduleAtFixedRate(new HumUpdateThread(), 0 , 500 * 1000);
    }

    public void testCom() {
        try {
            thermostat.testCom();
        } catch (DeviceNotFound | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("An error has occurred while trying to connect.");
            alert.setContentText("Make sure to change the ip address in PlugController.py");
            alert.showAndWait();
            this.stage = (Stage) scenePane.getScene().getWindow();
            this.stage.close();
        }
    }

    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    @FXML
    protected void onUpClick() {
        int currSetTemp = Integer.parseInt(labelSetTemp.getText());
        currSetTemp += 1;
        labelSetTemp.setText(Integer.toString(currSetTemp));
    }

    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    @FXML
    public void onDownClick() throws IOException {
        int currSetTemp = Integer.parseInt(labelSetTemp.getText());
        currSetTemp -= 1;
        labelSetTemp.setText(Integer.toString(currSetTemp));
    }


    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    public void clean() {
        tempTimer.cancel();
        tempTimer.purge();
        humTimer.cancel();
        humTimer.purge();
    }

    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    class TempUpdateThread extends TimerTask {
        @Override
        public void run() {
            try {
                int temp = thermostat.getCurrentTemp();
                labelCurrTemp.setText(Integer.toString(temp));
                System.out.println("curr temp: " + temp);
            } catch (IOException | DeviceNotFound e) {
                throw new RuntimeException(e);
            }

        }
    }

    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    class HumUpdateThread extends TimerTask {
        @Override
        public void run() {
            try {
                int temp = thermostat.getHumidity();
                labelHumidity.setText(Integer.toString(temp));
                System.out.println("Humidity: " + temp);
            } catch (IOException | DeviceNotFound e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*****************************************************************************************************************
     * Functions:           onSetSelect
     * Author:              Peter Pham
     * Date Started:        11/19/2022
     * Description:
     * Decide whether to turn the heater on
     * Parameters:
     *      e         ActionEvent      Specified the details of the
     *     pSet         ProgrammedSettings      specifies the schedule
     *****************************************************************************************************************/
    class HeaterControllerThread extends TimerTask {
        @Override
        public void run() {
            int difThreshold = 2;
            int temp = Integer.parseInt(labelCurrTemp.getText());
            int setTemp = Integer.parseInt(labelSetTemp.getText());

            if (temp <= (setTemp - difThreshold)) {
                try {
                    thermostat.setHeaterOn(true);
                } catch (DeviceNotFound e) {
                    throw new RuntimeException(e);
                }
            }
            // once the temperature reaches the desired temp, wait 30 seconds and then stop the heater
            else if (temp == setTemp) {
                try {
                    Thread.sleep(30 * 1000);
                    thermostat.setHeaterOn(false);
                } catch (InterruptedException e) {
                    try {
                        thermostat.setHeaterOn(false);
                    } catch (DeviceNotFound ex) {
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                } catch (DeviceNotFound e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}