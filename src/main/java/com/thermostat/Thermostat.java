package com.thermostat;

import java.io.*;

//Programmable Thermostat
public class Thermostat {

    private int curTemp;          // current temperature reading
    private int thresholdDiff = 3;    // temp difference until we turn heater on

    private boolean heaterOn;     // output of turnHeaterOn - whether to run
    private Period period;        // morning, day, evening, or night
    private DayType day;          // week day or weekend day
    private final ProcessBuilder processBuilder = new ProcessBuilder();

    public Thermostat() {
        processBuilder.redirectErrorStream(true);
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
    public boolean setHeaterOn(int pSet) throws DeviceNotFound {
//        int dTemp = pSet.getSetting (period, day);

        if (curTemp < pSet - thresholdDiff) {  // Turn on the heater
            setHeaterOn(true);
            return (true);
        } else {
            setHeaterOn(false);
            return (false);
        }
    } // End turnHeaterOn

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
    public int getCurrentTemp() throws IOException, DeviceNotFound {

        return parser(runCommand(new String[]{"getTemp", "F"}));
    }

    public void testCom() throws DeviceNotFound, IOException {

        runCommand(new String[]{"testCom"});
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
    public void setHeaterOn(boolean set) throws DeviceNotFound {

        if (set) {
            try {
                runCommand(new String[]{"turnOn"});
            } catch (IOException e) {
                System.out.println("error turning on heater");
            }
        }
        else {
            try {
                runCommand(new String[]{"turnOff"});
            } catch (IOException e) {
                System.out.println("error turning on heater");
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
    public int getHumidity() throws IOException, DeviceNotFound {
        return parser(runCommand(new String[]{"getHumidity"}));
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
    private String runCommand(String[] args) throws IOException, DeviceNotFound {

        switch (args[0]) {
            case "getTemp" ->
                    processBuilder.command("python", "SensorController.py", "getTemp", args[1]);
            case "getHumidity" ->
                    processBuilder.command("python", "SensorController.py", "getHumidity");
            case "turnOn" ->
                    processBuilder.command("python", "PlugController.py", "turnOn");
            case "turnOff" ->
                    processBuilder.command("python", "PlugController.py", "turnOff");
            case "testCom" ->
                    processBuilder.command("python", "PlugController.py", "testCom");
        }

        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String msg = reader.readLine();

        if (msg == null)
            return null;
        else {
            String[] temp = msg.split(" ");
            if (temp[0].equals("Unable")) {
                throw new DeviceNotFound("Device not found");
            }
        }

        return msg;
    }

    private int parser(String tempString) {
        try {
            return Integer.parseInt(tempString);

        } catch (NumberFormatException e) {
            System.out.println("error getting temperature");
        }

        return -1;
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
    public void setThresholdDiff(int delta) {
        thresholdDiff = delta;
    }

    // for the ProgrammedSettings
    public void setDay(DayType curDay) {
        day = curDay;
    }

    public void setPeriod(Period curPeriod) {
        period = curPeriod;
    }


} // End Thermostat class