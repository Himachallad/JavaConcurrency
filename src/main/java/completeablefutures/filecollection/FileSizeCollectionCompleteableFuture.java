package completeablefutures.filecollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class FileSizeCollectionCompleteableFuture {
  private static Map<String, LongAdder> getEachCollectionSize(List<File> files)
        throws InterruptedException, ExecutionException {
    Map<String, LongAdder> collectionSizeMap = new ConcurrentHashMap<>();
    int numThreads = 2;
    int batchSize = (files.size() + numThreads - 1) / numThreads;
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (int i = 0; i < files.size(); i += batchSize) {
      List<File> subList = files.subList(i, Math.min(i + batchSize, files.size()));
      CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        for (File file: subList) {
          for(String cId: file.getCollectionIds()) {
            collectionSizeMap
                  .computeIfAbsent(cId, k -> new LongAdder())
                  .add(file.getSize());
          }
        }
      }, executor);
      futures.add(future);
      
    }
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
    return collectionSizeMap;
  }
  public static void main(String[] args) throws InterruptedException, ExecutionException   {
    List<File> files = File.generateTestFiles(
          20,     // Number of files
          10,     // Number of distinct collections
          10,     // Min file size (MB)
          1000    // Max file size (MB)
    );

    Map<String, LongAdder> collectionSizeMap = getEachCollectionSize(files);
    PriorityQueue<String> pq = new PriorityQueue<String>((c1, c2)-> Long.compare(collectionSizeMap.get(c1).longValue(), collectionSizeMap.get(c2).longValue()));
    System.out.println(collectionSizeMap);
    int N = 5;

    for (String cId: collectionSizeMap.keySet()) {
      pq.add(cId);
      while (pq.size() > N) {
        pq.poll();
      }
    }
    List<String> ans = new ArrayList<>(pq);
    ans.sort((a, b) -> Long.compare(collectionSizeMap.get(b).longValue(), collectionSizeMap.get(a).longValue()));
    for (String cId : ans) {
      System.out.println(cId + " -> " + collectionSizeMap.get(cId));
    }
  }
}
