package pl.tscript3r.tracciato.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public interface ConcurrentStressTest {

    // TODO definitely refactor
    default <T> StressTestResult<T> concurrentStressTest(int threadsCount, int callableExecutionCount,
                                                         int maxExecutionTimeMilliSec, Callable<T> callable) throws ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsCount);
        long executionTimeBegin = System.currentTimeMillis();
        List<Future<T>> futures = new ArrayList<>();

        for (int i = 0; i < callableExecutionCount; i++)
            futures.add(executorService.submit(callable));

        while (true) {
            boolean allDone = true;

            for (Future<?> future : futures)
                allDone &= future.isDone();

            if (allDone)
                break;

            if (System.currentTimeMillis() - executionTimeBegin > maxExecutionTimeMilliSec)
                break;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        executorService.shutdownNow();

        var completedCount = 0;
        var uncompletedCount = 0;

        for (Future<T> future : futures) {
            if (future.isDone()) {
                completedCount++;
                try {
                    future.get(); // throws nested exception if there is any
                } catch (InterruptedException e) {
                    // ignore
                }
            } else
                uncompletedCount++;
        }

        var executionTime = System.currentTimeMillis() - executionTimeBegin;

        return new StressTestResult<>(executionTime, completedCount, uncompletedCount, futures);
    }

    default <T> List<T> unwrapFutures(Collection<Future<T>> futures) {
        List<T> results = new ArrayList<>();
        for (Future<T> tFuture : futures) {
            try {
                results.add(tFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

}
