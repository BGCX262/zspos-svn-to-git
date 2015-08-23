package Beans;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lock {
	public static ReadWriteLock Syslock=new ReentrantReadWriteLock(false); //数据承保锁
	public static ReadWriteLock Synlock=new ReentrantReadWriteLock(false); //数据同步锁
}
