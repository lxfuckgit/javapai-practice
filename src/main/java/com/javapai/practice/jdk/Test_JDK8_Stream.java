package com.javapai.practice.jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.javapai.framework.common.vo.TreeVO;

/**
 * 流的两个特征：<br>
 * 
 * 1、流是一系列操作的集合。<br>
 * 2、流可以并发执行，也可以顺序执行。
 * 
 * <br>
 * <br>
 * 重要说明：<br>
 * 1、java.util.Stream接口中，返回接口(本身)类型的方法叫'中间操作（intermediate operation）'；返回具体类型的方法叫'终端操作（terminal operation）';<br>
 * 2、Stream操作由零个或多个中间操作和一个结束操作两部分组成;只有执行结束操作时，Stream定义的中间操作才会依次执行，这就是Stream的'延迟特性'。<br>
 * N、Stream流式编程的内建功能性接口：<br>
 * --->Predicate－断言,<br>
 * --->Function-功能,<br>
 * --->Suppliers-供应,<br>
 * --->Consumers-消费,<br>
 * --->Comparators-比较,<br>
 * --->Optionals-选项。<br>
 * <br><br>
 * 既然流是一些操作的集合，那么流就可以将一个或多个操作聚集起来。<br>
 * 这里就有两个问题：<br>
 * 1、流是如何将这些操作聚集起来的；<br>
 * 2、流是如何创建的的，只有知道流的创建过程，才知道流是如何连接操作的。<br>
 * 
 * <br>
 * 
 * 更多参考：https://blog.csdn.net/io_field/article/details/54971761
 * 
 * @author pooja
 *
 */
public class Test_JDK8_Stream {
	
	static String[] array = { "a", "b", "c", "d" };
	
	/**
	 * Stream预备知识.<br>
	 * 这些预备知识都是后面需要用到的，方便加深理解stream.<br>
	 */
	public void Test1StreamPrepare() {
		// Predicate接口测试.

		// Comparators接口.

	}

	public static void main(String[] args) {
		TreeVO stuA = new TreeVO("张A", "M");
		TreeVO stuB = new TreeVO("李B", "F");
		TreeVO stuC = new TreeVO("刘C", "F");
		TreeVO stuD = new TreeVO("王D", "M");
		TreeVO stuE = new TreeVO("孙E", "M");
		TreeVO stuG = new TreeVO("孙G", "F");
		List<TreeVO> list = new ArrayList<>();
		list.add(stuA);
		list.add(stuB);
		list.add(stuC);
		list.add(stuD);
		list.add(stuE);
		list.add(stuG);

		System.out.println("1:Stream创建---------------------");
		System.out.println("-----1.1、通过静态工厂方式创建:");
		Stream<String> str_stream = Stream.of("A", "B");// 有限长度

		System.out.println("-----1.2、通过数组方式创建:");
		int[] arr = {1, 2,3, 4};
		IntStream int_stream = Arrays.stream(arr);
		str_stream = Arrays.stream(array);
		
		System.out.println("-----1.3、通过集合(Collection.stream())方式创建:");
		Stream<TreeVO> stream3 = list.stream();

		System.out.println("-----1.4、通过集合(Stream.iterate和Stream.generate)方式创建:");
		//还有两类无限流(相对于前面3种有限流，无限流的对象大小是无法确定)
//		Stream.iterate(see, f);
		Stream.generate(()-> java.lang.Math.random());
		Stream.generate(new Supplier<Double>() {
			@Override
			public Double get() {
				// TODO Auto-generated method stub
				return java.lang.Math.random();
			}
		});
		
		System.out.println("-----1.5、通过集合(Stream.empty)方式创建:");
		Stream.empty();
		
		System.out.println("-----1.6、Stream迭代：");
		System.out.println("-----------java8之前-------");
		for (String string : Arrays.asList(array)) {
			System.out.println("--------->" + string);
		}
		System.out.println("-----------java8之后-------");
		Arrays.asList(array).forEach(item -> System.out.println("--------->" + item));
		
		System.out.println("2:Stream终端操作---------------------");
		System.out.println("-----2.1、forEach循环迭代（聚合操作）stream:");
		list.stream().forEach(x -> System.out.println("姓名:" + x.getKey() + "性别:" + x.getValue()));
		
		System.out.println("-----2.2、forEachOrdered按元素插入顺序循环迭代（聚合操作）stream:");
		Stream.of(5, 2, 1, 4, 3).forEachOrdered(integer -> {
			System.out.println("integer:" + integer);
		});
		
		System.out.println("-----2.3、toArray返回流中元素对应的数组对象:");
		
		System.out.println("-----2.4、reduce聚合操作，用来做统计:");
//		https://blog.csdn.net/IO_Field/article/details/54971679
		
		System.out.println("-----2.5、collect聚合操作，封装目标数据:");
		
		System.out.println("-----2.6、min、max、count聚合操作，最小值，最大值，总数量:");
		
		System.out.println("-----2.7、anyMatch短路操作，有一个符合条件返回true:");
			
		System.out.println("-----2.8、allMatch所有数据都符合条件返回true:");
		boolean allMatch = Stream.of(1, 2, 3, 4).allMatch(integer -> integer > 0);
		System.out.println("allMatch: " + allMatch); // 打印结果：allMatch: true
		
		System.out.println("-----2.9、noneMatch所有数据都不符合条件返回true:");
		
		System.out.println("-----2.10、findFirst短路操作，获取第一个元素:");
		
		System.out.println("-----2.11、findAny短路操作，获取任一元素:");
		
		System.out.println("-----2.12、count()返回Stream中元素的个数");
		long count = Stream.of(1, 2, 3, 4, 5).count();
		System.out.println("count:" + count);// 打印结果：count:5
		
		System.out.println("3:Stream中间操作---------------------");
		System.out.println("----3.1、Stream中间操作-Stream.filter过滤(符合条件的)");
		TreeVO bean = list.stream().filter(x -> x.getKey().equals("F")).findAny().orElse(null);
		System.out.println("-----所有女性数据：" + bean.getValue() + "-" + bean.getValue());
		
		System.out.println("-----3.2、中间操作-Stream.map转换(传入一个T返回一个R)");
//		官网提供三种默认的转换：mapToInt、mapToLong、mapToDouble返回int、long、double基本类型对应的Stream。
		System.out.println("-----list转map:");
		list.stream().collect(Collectors.toMap(TreeVO::getValue, demo -> demo));
		list.stream().map(map -> map.getValue());
		System.out.println("-----list转map:");
		Stream.of("a", "b", "hello").map(item -> item.toUpperCase()).forEach(System.out::println);// 打印结果:A, B, HELLO
		
		System.out.println("3.3、中间操作-Stream.flatMap流合并(将一个或多个流合并成一个新流)-------------------");
		Stream.of(1, 2, 3).flatMap(integer -> Stream.of(integer * 10)).forEach(System.out::println);// 打印结果:10，20，30
//		官方默认提供：flatMapToInt、flatMapToLong、flatMapToDouble返回对应的IntStream、LongStream、DoubleStream流。
		
		System.out.println("3.4、中间操作-Stream.distinct流去重-------------------");
		Stream.of(1, 2, 3, 1, 2, 3).distinct().forEach(System.out::println); // 打印结果：1，2，3
		
		System.out.println("3.5、中间操作-Stream.sorted流排序-------------------");
		list.stream().sorted();
		Stream.of(5, 4, 3, 2, 1).sorted().forEach(System.out::println); // 打印结果:1，2，3,4,5
		
		System.out.println("3.6、中间操作-Stream.peek查看流中元素的数据状态-------------------");
		Stream.of(1, 2, 3, 4, 5).peek(x -> System.out.println("accept:" + x));
		
		System.out.println("3.7、中间操作-Stream.limit(返回前n个元素数据组成的Stream)-------------------");
		
		System.out.println("3.8、中间操作-Stream.skip(返回第n个元素后面数据组成的Stream)(前过前N个元素)-------------------");
		Stream.of(1, 2, 3, 4, 5).skip(2).forEach(System.out::println);// 打印结果 :3,4,5
		
		System.out.println("3.9、中间操作-Stream.concat(流去重)-------------------");
		
		System.out.println("4:内置函数接口---------------------");
		
	}

}
