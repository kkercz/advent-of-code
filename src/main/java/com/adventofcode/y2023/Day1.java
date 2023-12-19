package com.adventofcode.y2023;

import com.adventofcode.util.FileUtil;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Day1 {

    private final static Map<String, Integer> digitNames = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    public static int number(String line) {
        List<Integer> digits = IntStream.range(0, line.length())
                .mapToObj(i -> digitAt(line, i))
                .flatMap(Optional::stream)
                .toList();

        return Integer.parseInt(
                digits.getFirst().toString() + digits.getLast());
    }

    private static Optional<Integer> digitAt(String line, int i) {
        if (Character.isDigit(line.charAt(i))) {
            return Optional.of(Integer.parseInt(String.valueOf(line.charAt(i))));
        }

        return digitNames.entrySet().stream()
                .filter(e -> line.startsWith(e.getKey(), i))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public static int solution() {
        return FileUtil
                .lines("2023/day1")
                .mapToInt(Day1::number)
                .sum();
    }

}
