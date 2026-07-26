package com.example.experience.common.utils;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

public class Uuid7Utils {

    private static final NoArgGenerator TIME_BASED_GENERATOR = Generators.timeBasedEpochGenerator();
    private static String removeHyphens(String uuid) {
        return uuid.replace("-", "");
    }
    private static String addHyphens(String uuid) {
        return uuid.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5");
    }

    public static String generateUuid7() {
        UUID uuid = TIME_BASED_GENERATOR.generate();
        return removeHyphens(uuid.toString());
    }

    public static String getUuidWithHyphens(String uuid) {
        return addHyphens(uuid);
    }

}
