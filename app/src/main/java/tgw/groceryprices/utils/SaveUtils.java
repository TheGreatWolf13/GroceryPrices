package tgw.groceryprices.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class SaveUtils {

    private static final byte[] WRITE_BUFFER = new byte[4];
    private static final byte[] READ_BUFFER = new byte[4];

    private SaveUtils() {
    }

    private static void read(FileInputStream stream, int length) throws IOException {
        if (stream.read(READ_BUFFER, 0, length) != length) {
            throw new IOException("End of file");
        }
    }

    public static int read1Byte(FileInputStream stream) throws IOException {
        read(stream, 1);
        return READ_BUFFER[0] & 0xff;
    }

    public static int read3Bytes(FileInputStream stream) throws IOException {
        read(stream, 3);
        return READ_BUFFER[0] & 0xff | (READ_BUFFER[1] & 0xff) << 8 | (READ_BUFFER[2] & 0xff) << 16;
    }

    public static boolean readBoolean(FileInputStream stream) throws IOException {
        read(stream, 1);
        return (READ_BUFFER[0] & 0xff) != 0;
    }

    public static byte readByte(FileInputStream stream) throws IOException {
        read(stream, 1);
        return READ_BUFFER[0];
    }

    public static char readChar(FileInputStream stream) throws IOException {
        read(stream, 2);
        return (char) (READ_BUFFER[0] & 0xff | (READ_BUFFER[1] & 0xff) << 8);
    }

    public static <T extends Enum<T>> T readEnum(FileInputStream stream, T[] values) throws IOException {
        read(stream, 1);
        return values[READ_BUFFER[0] & 0xff];
    }

    public static int readInt(FileInputStream stream) throws IOException {
        read(stream, 4);
        return READ_BUFFER[0] & 0xff | (READ_BUFFER[1] & 0xff) << 8 | (READ_BUFFER[2] & 0xff) << 16 | (READ_BUFFER[3] & 0xff) << 24;
    }

    public static String readString(FileInputStream stream) throws IOException {
        int length = readInt(stream);
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = readChar(stream);
        }
        return String.valueOf(chars);
    }

    public static void write(FileOutputStream stream, int value) throws IOException {
        WRITE_BUFFER[0] = (byte) value;
        WRITE_BUFFER[1] = (byte) (value >> 8);
        WRITE_BUFFER[2] = (byte) (value >> 16);
        WRITE_BUFFER[3] = (byte) (value >> 24);
        stream.write(WRITE_BUFFER, 0, 4);
    }

    public static void write(FileOutputStream stream, byte value) throws IOException {
        WRITE_BUFFER[0] = value;
        stream.write(WRITE_BUFFER, 0, 1);
    }

    public static void write(FileOutputStream stream, char value) throws IOException {
        WRITE_BUFFER[0] = (byte) value;
        WRITE_BUFFER[1] = (byte) (value >> 8);
        stream.write(WRITE_BUFFER, 0, 2);
    }

    public static void write(FileOutputStream stream, String value) throws IOException {
        int length = value.length();
        write(stream, length);
        for (int i = 0; i < length; i++) {
            write(stream, value.charAt(i));
        }
    }

    public static void write(FileOutputStream stream, Enum<?> unit) throws IOException {
        WRITE_BUFFER[0] = (byte) unit.ordinal();
        stream.write(WRITE_BUFFER, 0, 1);
    }

    public static void write(FileOutputStream stream, boolean value) throws IOException {
        WRITE_BUFFER[0] = (byte) (value ? 1 : 0);
        stream.write(WRITE_BUFFER, 0, 1);
    }

    public static void write1Byte(FileOutputStream stream, int value) throws IOException {
        WRITE_BUFFER[0] = (byte) value;
        stream.write(WRITE_BUFFER, 0, 1);
    }

    public static void write3Bytes(FileOutputStream stream, int value) throws IOException {
        WRITE_BUFFER[0] = (byte) value;
        WRITE_BUFFER[1] = (byte) (value >> 8);
        WRITE_BUFFER[2] = (byte) (value >> 16);
        stream.write(WRITE_BUFFER, 0, 3);
    }
}
