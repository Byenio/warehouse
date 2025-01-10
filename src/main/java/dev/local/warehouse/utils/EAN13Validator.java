package dev.local.warehouse.utils;

public class EAN13Validator {
    public boolean ValidateBarcode(String barcode) {
        if (barcode == null || barcode.length() != 13 || !barcode.matches("\\d+")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;

        return checkDigit == Character.getNumericValue(barcode.charAt(12));
    }
}
