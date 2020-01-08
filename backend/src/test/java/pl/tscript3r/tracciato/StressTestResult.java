package pl.tscript3r.tracciato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.Future;

@Getter
@ToString(exclude = {"futuresList"})
@AllArgsConstructor
public class StressTestResult<T> {
    final long executionTime;
    final long completedCount;
    final long uncompletedCount;
    final List<Future<T>> futuresList;
}
