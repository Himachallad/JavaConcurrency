package org.example.oddeven.driver;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PrintDriver {
  AtomicInteger sharedValue = new AtomicInteger();
  ReentrantLock lock = new ReentrantLock();
  Condition evenCondition = lock.newCondition();
  Condition oddCondition = lock.newCondition();

  private void printOdd() {
    while(true) {
      lock.lock();
      try {
        if (sharedValue.get() % 2 == 0) {
          oddCondition.await();
        } else {
          sharedValue.addAndGet(1);
          System.out.println(sharedValue.get());
          Thread.sleep(2000);
          evenCondition.signal();
        }
      } catch (InterruptedException exception) {
        System.out.println(exception);
      } finally {
        lock.unlock();
      }
    }
  }
  private void printEven() {
    while(true) {
      lock.lock();
      try {
        if (sharedValue.get() % 2 != 0) {
          evenCondition.await();
        } else {
          sharedValue.addAndGet(1);
          System.out.println(sharedValue.get());
          Thread.sleep(2000);
          oddCondition.signal();
        }
      } catch (InterruptedException exception) {
        System.out.println(exception);
      } finally {
        lock.unlock();
      }
    }
  }

  public void print() {
    new Thread(this::printEven).start();
    new Thread(this::printOdd).start();
  }


  public static void main(String[] args) {
    PrintDriver printDriver = new PrintDriver();
    printDriver.print();
  }
}
