package ru.clevertec.check.validator;

import java.util.Map;
import java.util.regex.Pattern;

public class CardDataValidator {

    private static final String CARD_REGEX = "0000\\s0|1111\\s1|2222\\s2|3333\\s3|4444\\s4|5555\\s5";
    private static final String CARD_NUMBER_REGEX = "0000|1111|2222|3333|4444|5555";

    public CardDataValidator() {
    }

    public static boolean isValidCardParameters(Map<String, String> cardParameters) {
        String line = cardParameters.get("card_number") + " " + cardParameters.get("discount");
        return Pattern.matches(CARD_REGEX, line);
    }

    public static boolean isValidNumberCard(String number) {
        return number != null && !number.isEmpty() && number.matches(CARD_NUMBER_REGEX);
    }
}
