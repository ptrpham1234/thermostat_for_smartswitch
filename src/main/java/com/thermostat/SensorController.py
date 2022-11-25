import time
import sys
import adafruit_dht
import board

# --------- User Settings ---------
SENSOR_LOCATION_NAME = "Office"
MINUTES_BETWEEN_READS = 10
# ---------------------------------

dhtSensor = adafruit_dht.DHT22(board.D4)


def main(args):
    if args[1] == "getTemp":
        if args[2] == "F":
            getTemp()
        else:
            getTemp("C")

    elif args[1] == "getHumidity":
        getHumidity()


def getTemp(unit="F"):
    try:
        temp_c = dhtSensor.temperature
    except RuntimeError:
        print("RuntimeError, trying again...")

    if unit == "C":
        print(temp_c)
    else:
        temp_f = format(temp_c * 9.0 / 5.0 + 32.0, ".2f")
        print(temp_f)


def getHumidity():
    print(dhtSensor.humidity)


if __name__ == "__main__":
    main(sys.argv)
