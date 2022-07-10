package com.finn.gulimall.member.config;

import net.sf.jsqlparser.statement.create.view.ForceOption;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/*
 * @description: 独占锁
 * @author: Finn
 * @create: 2022/07/09 14:12
 */
public class MyLock implements Lock {
    // 用AQS实现一个排他锁
    private static class Sync extends AbstractQueuedSynchronizer {
        // 是否处于占用状态
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        // 当状态为0的时候获取锁
        public boolean tryAcquire(int acquires) {
            // 用CAS方法把状态码state设为1
            if (compareAndSetState(0, 1)) {
                // 设置排他锁的拥有者
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        // 释放锁，将状态设置为0
        protected boolean tryRelease(int releases) {
            if (getState() == 0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        // 返回一个Condition，每个condition都包含了一个condition队列
        Condition newCondition() { return new ConditionObject(); }
    }

    private Sync sync = new Sync();

    // 调用该方法的线程获取锁
    @Override
    public void lock() {
        if (!sync.isHeldExclusively()) {
            sync.tryAcquire(1);
        }
    }

    // 可中断的获取锁，该方法可以响应中断，在锁的获取中可以中断线程，然后抛出异常
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    // 尝试非阻塞的获取锁，调用该方法后立即返回。
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    // 尝试超时非阻塞的获取锁。
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    // 释放锁
    @Override
    public void unlock() {
        sync.release(1);
    }

    // 获取等待通知组件，该组件和锁绑定，只有获取了锁，才能使用该组件中的wait()方法，而调用后，当前线程释放掉锁。
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}