package com.thermostat;

public class DeviceNotFound extends Exception {

    public DeviceNotFound(String str) {super(str);}

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
