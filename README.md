# javapai-practice
各种技术的测试例子

------------------并发与多线程----------------------------------------------------
##volatile可见性 和 atomic原子性的区别和联系
原子操作就是不能被线程调度机制中断的操作。不正确的认识：原子操作不需要进行同步。

在Java 中除了 long和 double之外的所有基本类型的读和赋值，都是原子性操作。而64位的long和 double变量由于会被JVM当作两个分离的32位来进行操作，所以不具有原子性，会产生字撕裂问题。
但是当你定义long或double变量时，如果使用 volatile关键字，就会获到（简单的赋值与返回操作的）原子性（注意，在Java SE5之前，volatile一直不能正确的工作）。见第四版《Thinking in java》第21章并发。

volatile关键字只能保证变量对各个线程的可见性，但不能保证原子性。如果你将一个域声明为volatile，那么只要这个域产生了写操作，那么所有的读操作就都可以看到这个修改。

/*问题1*/
证明long和double的简单操作是非原子性的(会产生字撕裂问题)，而使用volatile 关键字可以保证 long 或 double 的简单操作具有原子性，以及验证其它的基本类型（如int）的简单操作具有原子性。

/*证明问题1*/

http://blog.csdn.net/hupitao/article/details/45227891
http://blog.csdn.net/zhaifengmin/article/details/46315019
http://blog.csdn.net/ksjay_1943/article/details/53703230
http://www.cnblogs.com/Mainz/p/3546347.html
http://blog.csdn.net/guyuealian/article/details/52525724
http://blog.csdn.net/littlefang/article/details/6103038
http://www.cnblogs.com/rainwang/p/4398488.html
http://ifeve.com/atomiclong-and-longadder/







/*问题6*/
对jdk8中增加LongAdder类和AtomicLong类的认识。

/*结论6*/
a.在单线程下，新的LongAdder慢了1/3,但是当多个线程在争着增加字段，LongAdder则显示出了自己的价值。
注意到，每个线程所做的唯一事情就是试图增加计数——这是一个最极端形式的综合标准。这里的竞争比你在实际应用程序中可能看到的要高的多，但是有时候你确实需要这种共享计数器，而LongAdder带来了很大的帮助。

b.多线程下，原子实现比AtomicInteger和AtomicLong提供了更优越的性能。
 
-------------------------------------------------------------------------------
