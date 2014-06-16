package moppydesk;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 *
 * @author Sammy1Am
 */
public class MoppyPlayer implements Receiver {

    /**
     * Current period of each MIDI channel (zero is off) as set 
     * by the NOTE ON message; for pitch-bending.
     */
    private int[] currentPeriod = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    MoppyBridge mb;

    public MoppyPlayer(MoppyBridge newMb) {
        mb = newMb;
    }

    public void close() {
        mb.close();
    }

    //Is called by Java MIDI libraries for each MIDI message encountered.
    public void send(MidiMessage message, long timeStamp) {
        if (message.getStatus() > 127 && message.getStatus() < 144) { // Note OFF
            //Convert the MIDI channel being used to the controller pin on the
            //Arduino by multipying by 2 and handles the offset for FIRST_PIN.
            byte pin = (byte) (2 * (message.getStatus() - 127) + Arduino.FIRST_PIN - 2);

            mb.sendEvent(pin, 0);
            currentPeriod[message.getStatus() - 128] = 0;
        } else if (message.getStatus() > 143 && message.getStatus() < 160) { // Note ON
            //Convert the MIDI channel being used to the controller pin on the
            //Arduino by multipying by 2 and handles the offset for FIRST_PIN.
            byte pin = (byte) (2 * (message.getStatus() - 143) + Arduino.FIRST_PIN - 2);

            //Get note number from MIDI message, and look up the period.
            //NOTE: Java bytes range from -128 to 127, but we need to make them
            //0-255 to use for lookups.  & 0xFF does the trick.

            // After looking up the period, devide by (the Arduino resolution * 2).
            // The Arduino's timer will only tick once per X microseconds based on the
            // resolution.  And each tick will only turn the pin on or off.  So a full
            // on-off cycle (one step on the floppy) is two periods.
            int period = Arduino.MICRO_PERIODS[(message.getMessage()[1] & 0xff)] / (Arduino.RESOLUTION * 2);

            //Zero velocity events turn off the pin.
            if (message.getMessage()[2] == 0) {
                mb.sendEvent(pin, 0);
                currentPeriod[message.getStatus() - 144] = 0;
            } else {
                mb.sendEvent(pin, period);
                currentPeriod[message.getStatus() - 144] = period;
            }
        } else if (message.getStatus() > 223 && message.getStatus() < 240) { //Pitch bends
            //Only proceed if the note is on (otherwise, no pitch bending)
            if (currentPeriod[message.getStatus() - 224] != 0) {
                //Convert the MIDI channel being used to the controller pin on the
                //Arduino by multipying by 2 and handles the offset for FIRST_PIN.
                byte pin = (byte) (2 * (message.getStatus() - 223) + Arduino.FIRST_PIN - 2);

                double pitchBend = ((message.getMessage()[2] & 0xff) << 8) + (message.getMessage()[1] & 0xff);

                int period = (int) (currentPeriod[message.getStatus() - 224] / Math.pow(2.0, (pitchBend - 8192) / 8192));
                mb.sendEvent(pin, period);
            }
        }

    }
}
