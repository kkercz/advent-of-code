package com.adventofcode.y2023;

import com.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {

    record ScannedNumber(String number, int y, int x) {}

    public static List<Integer> getPartNumbers(List<String> lines) {
        return scanNumbers(lines)
                .stream()
                .filter(scannedNumber -> isPart(scannedNumber, lines))
                .map(scannedNumber -> Integer.valueOf(scannedNumber.number))
                .collect(Collectors.toList());
    }

    private static boolean isPart(ScannedNumber n, List<String> lines) {
        return Stream.of(n.y(), n.y() - 1, n.y() + 1)
                .filter(y -> y >= 0 && y < lines.size())
                .anyMatch(y -> containsSymbol(
                        lines.get(y),
                        Math.max(0, n.x() - 1),
                        Math.min(lines.get(y).length(), n.x() + n.number().length() + 1)));
    }

    private static boolean containsSymbol(String line, int indexFrom, int indexTo) {
        return line.substring(indexFrom, indexTo).matches(".*[^0-9.].*");
    }

    private static List<ScannedNumber> scanNumbers(List<String> lines) {
        List<ScannedNumber> result = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                if (Character.isDigit(lines.get(i).charAt(j))) {
                    ScannedNumber number = new ScannedNumber(numberAt(lines.get(i), j), i, j);
                    result.add(number);
                    j += number.number().length();
                }
            }
        }

        return result;
    }

    private static String numberAt(String string, int x) {
        return string.substring(x).replaceAll("(^\\d+).*", "$1");
    }

    public static int part1() {
        List<String> lines = FileUtil.lines("2023/day3").collect(Collectors.toList());
        return getPartNumbers(lines).stream().mapToInt(a -> a).sum();
    }
}
