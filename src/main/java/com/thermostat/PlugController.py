import asyncio
from kasa import SmartPlug
import sys

async def main(args):

    # if args[1] == "testCom":
    #     p = SmartPlug("192.168.254.155")


    # else:
    try:
        p = SmartPlug("192.168.254.157")

        await p.update()  # Request the update

        if args[1] == "turnOn":
            await p.turn_on()  # Turn the device on
        elif args[1] == "turnOff":
            await p.turn_off()
    except IndexError:
        print("done")
    except:
        print("Unable to find device")

if __name__ == "__main__":
    asyncio.set_event_loop_policy(asyncio.WindowsSelectorEventLoopPolicy())
    asyncio.run(main(sys.argv))