package blockchain;

import java.util.Arrays;

public class Base58 {
    static {
        Arrays.fill(Configuration.INSTANCE.INDEXES, -1);
        for (int i = 0; i < Configuration.INSTANCE.ALPHABET.length; i++) {
            Configuration.INSTANCE.INDEXES[Configuration.INSTANCE.ALPHABET[i]] = i;
        }
    }

    public static String encode(byte[] input) {
        if (input.length == 0) {
            return "";
        }

        int zeros = 0;
        while (zeros < input.length && input[zeros] == 0) {
            ++zeros;
        }

        input = Arrays.copyOf(input, input.length);
        char[] encoded = new char[input.length * 2];
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < input.length; ) {
            encoded[--outputStart] = Configuration.INSTANCE.ALPHABET[divMod(input, inputStart)];
            if (input[inputStart] == 0) {
                ++inputStart;
            }
        }

        while (outputStart < encoded.length && encoded[outputStart] == Configuration.INSTANCE.ENCODED_ZERO) {
            ++outputStart;
        }

        while (--zeros >= 0) {
            encoded[--outputStart] = Configuration.INSTANCE.ENCODED_ZERO;
        }

        return new String(encoded, outputStart, encoded.length - outputStart);
    }

    private static byte divMod(byte[] number, int firstDigit) {
        int remainder = 0;

        for (int i = firstDigit; i < number.length; i++) {
            int digit = (int) number[i] & 0xFF;
            int temp = remainder * 256 + digit;
            number[i] = (byte) (temp / 58);
            remainder = temp % 58;
        }

        return (byte) remainder;
    }
}