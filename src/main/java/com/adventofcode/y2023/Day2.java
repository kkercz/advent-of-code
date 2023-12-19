package com.adventofcode.y2023;

import com.adventofcode.util.FileUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class Day2 {

    enum Cube {
        blue, red, green
    }

    record Game(int id, List<GameRound> rounds) {

        public static Game parse(String line) {
            String[] splitByColon = line.split(":");
            int id = Integer.parseInt(splitByColon[0].substring("Game ".length()));
            List<GameRound> rounds = Arrays
                    .stream(splitByColon[1].split(";"))
                    .map(GameRound::parse)
                    .toList();

            return new Game(id, rounds);
        }

        public boolean isPossible(Map<Cube, Integer> constraints) {
            return rounds.stream().allMatch(round -> round.isPossible(constraints));
        }

        public Map<Cube, Integer> getMinimumSet() {
            return rounds
                    .stream()
                    .flatMap(round -> round.cubes().entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            Integer::max
                    ));
        }
    }

    record GameRound(Map<Cube, Integer> cubes) {
        public static GameRound parse(String string) {
            return new GameRound(Arrays
                    .stream(string.split(","))
                    .map(String::trim)
                    .map(line -> line.split(" "))
                    .collect(toMap(
                            pair -> Cube.valueOf(pair[1]),
                            pair -> Integer.valueOf(pair[0])
                    )));
        }

        public boolean isPossible(Map<Cube, Integer> constraints) {
            return cubes().entrySet().stream().allMatch(entry ->
                    constraints.get(entry.getKey()) >= entry.getValue());
        }
    }

    private final static Map<Cube, Integer> CONSTRAINTS = Map.of(
            Cube.red, 12,
            Cube.green, 13,
            Cube.blue, 14
    );

    public static int part1() {
        return FileUtil.lines("2023/day2")
                .map(Game::parse)
                .filter(game -> game.isPossible(CONSTRAINTS))
                .mapToInt(Game::id)
                .sum();
    }

    public static int part2() {
        return FileUtil.lines("2023/day2")
                .map(Game::parse)
                .map(Game::getMinimumSet)
                .mapToInt(set -> set.values().stream().reduce((a, b) -> a * b).orElse(0))
                .sum();
    }

}
