package pl.tscript3r.tracciato.schedule.optimization;

import org.paukov.combinatorics3.Generator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

final class PermutationsGroupGenerator {

    private PermutationsGroupGenerator() {
    }

    public static <T> Stream<List<T>> generate(Collection<T> collection) {
        return Generator.permutation(collection)
                .simple()
                .stream();
    }

}