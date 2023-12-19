package com.adventofcode.y2023;

import com.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {

    record ScannedNumber(String number, int y, int x) {
        public boolean isAdjacentTo(int x) {
            return x() < x && number().length() + x() >= x
                    || x() == x + 1 || x() == x;
        }
    }

    public static List<ScannedNumber> getPartNumbers(List<String> lines) {
        return scanNumbers(lines)
                .stream()
                .filter(scannedNumber -> isPart(scannedNumber, lines))
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
        return getPartNumbers(lines)
                .stream()
                .mapToInt(scannedNumber -> Integer.parseInt(scannedNumber.number))
                .sum();
    }
    public static int part2() {
        List<String> lines = FileUtil.lines("2023/day3").collect(Collectors.toList());

        Map<Integer, List<ScannedNumber>> partsByLine = getPartNumbers(lines)
                .stream()
                .collect(Collectors.groupingBy(ScannedNumber::y));

        int result = 0;
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '*') {
                    List<Integer> numbers = getAdjacentNumbers(partsByLine, x, y);
                    if (numbers.size() == 2) {
                        result += numbers.getFirst() * numbers.getLast();
                    }
                }
            }
        }

        return result;
    }

    private static List<Integer> getAdjacentNumbers(Map<Integer, List<ScannedNumber>> partsByLine, int x, int y) {
        return Stream
                .of(y, y - 1, y + 1)
                .filter(partsByLine::containsKey)
                .flatMap(line -> partsByLine.get(line).stream())
                .filter(n -> n.isAdjacentTo(x))
                .map(ScannedNumber::number)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
