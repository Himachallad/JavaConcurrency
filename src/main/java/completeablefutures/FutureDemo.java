package completeablefutures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FutureDemo {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
      try { Thread.sleep(300); } catch (InterruptedException ignored) {}
      return "Task 1 completed";
    });

    CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      try { Thread.sleep(100); } catch (InterruptedException ignored) {}
      return "Task 2 completed";
    });

    CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
      try { Thread.sleep(200); } catch (InterruptedException ignored) {}
      return "Task 3 completed";
    });

    // anyOf() will complete when the fastest future completes
    CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(future1, future2, future3);

    System.out.println("First completed: " + firstCompleted.get());

    CompletableFuture.allOf(future1, future2, future3).thenApply(v -> {
      System.out.println(v);
      return "1";
    });
  }
}
