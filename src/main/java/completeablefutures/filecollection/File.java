package completeablefutures.filecollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

class File {
  long size;
  List<String> collectionIds;

  public File(long size, List<String> collectionIds) {
    this.size = size;
    this.collectionIds = collectionIds;
  }

  public static List<File> generateTestFiles(int numFiles, int numCollections, int minSize, int maxSize) {
    Random random = new Random();
    List<File> files = new ArrayList<>();

    for (int i = 0; i < numFiles; i++) {
      int fileSize = minSize + random.nextInt(maxSize - minSize + 1);
      int collectionsPerFile = 1 + random.nextInt(3); // Each file belongs to 1–3 collections

      Set<String> collectionIds = new HashSet<>();
      while (collectionIds.size() < collectionsPerFile) {
        collectionIds.add("C" + (1 + random.nextInt(numCollections))); // e.g. C1 to C10
      }

      files.add(new File(fileSize, new ArrayList<>(collectionIds)));
    }

    return files;
  }

  public long getSize() {
    return size;
  }

  public List<String> getCollectionIds() {
    return collectionIds;
  }

  @Override
  public String toString() {
    return "File{" +
           "size=" + size +
           ", collectionIds=" + collectionIds +
           '}';
  }
}
