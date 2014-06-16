package moppydesk;

/**
 * Utility class for the Arduino. <BR>
 * <BR>
 * Values in this class must correspond with the values in the Arduino code.
 *
 * @author Marc Andre Wyss
 */
public final class Arduino {

    /**
     * Serial data transmission rate in baud (bits per second).
     */
    public static final int SERIAL_DATA_TRANSMISSION_RATE = 9600;

    /**
     * Resolution for notes in microseconds.
     */
    public static final int RESOLUTION = 40;

    /**
     * First digital pin used for floppies.
     */
    public static final int FIRST_PIN = 2;

    /**
     * Last digital pin used for floppies.
     */
    public static final int MAX_PIN = 17;

    /**
     * The periods for each MIDI note in an array. The floppy drives don't really do well
     * outside of the defined range, so skip those notes. Periods are in microseconds
     * because that's what the Arduino uses for its clock-cycles in the micro() function,
     * and because milliseconds aren't precise enough for musical notes.
     */
    public static int[] MICRO_PERIODS = {
    /*  C,    C#,     D,    D#,     E,     F,    F#,     G,    G#,     A,    A#,     B, Level */
        0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0, /* -1 */
        0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0, /*  0 */
    30578, 28861, 27242, 25713, 24270, 22909, 21622, 20409, 19263, 18182, 17161, 16198, /*  1 */
    15289, 14436, 13621, 12856, 12135, 11454, 10811, 10205,  9632,  9091,  8581,  8099, /*  2 */
     7645,  7218,  6811,  6428,  6068,  5727,  5406,  5103,  4816,  4546,  4291,  4050, /*  3 */
     3823,  3609,  3406,  3214,  3034,  2864,  2703,  2552,  2408,  2273,  2146,  2025, /*  4 */
        0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0, /*  5 */
        0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0, /*  6 */
        0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0  /*  7 */
};

    /*
     * Private constructor, because this is a utility class.
     */
    private Arduino(){}

}
