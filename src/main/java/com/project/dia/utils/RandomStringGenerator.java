package com.project.dia.utils;

import java.util.Random;

public class RandomStringGenerator {
    private char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
    private StringBuilder stringBuilder = new StringBuilder();
    private Random random = new Random();
    private String output;

    public String getRandom() {
        for (int lenght = 0; lenght < 3; lenght++) {
            Character character = chars[random.nextInt(chars.length)];
            stringBuilder.append(character);
        }
        output = stringBuilder.toString();
        stringBuilder.delete(0, 10);

        return output;
    }
}
