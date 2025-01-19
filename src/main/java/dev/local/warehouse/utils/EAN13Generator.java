package dev.local.warehouse.utils;

import java.util.Random;

public class EAN13Generator {
    public String GenerateBarcode(String countryCode, String manufacturerCode) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < 4) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        String barcode = String.format("%s%s%s", countryCode, manufacturerCode, salt);

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        System.out.println(barcode + checkDigit);
        return String.format("%s%s", barcode, checkDigit);
    }
}
