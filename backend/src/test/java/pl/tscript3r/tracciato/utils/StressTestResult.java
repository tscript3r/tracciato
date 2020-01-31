package pl.tscript3r.tracciato.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.Future;

@Getter
@ToString(exclude = {"futuresList"})
@AllArgsConstructor
public class StressTestResult<T> {
    public final long executionTime;
    public final long completedCount;
    public final long uncompletedCount;
    public final List<Future<T>> futuresList;
}
