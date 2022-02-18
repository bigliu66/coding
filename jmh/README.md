# 简介

JMH is a Java harness for building, running, and analysing nano/micro/milli/macro benchmarks written in Java and other languages targeting the JVM.（JMH 是一个 Java 工具，用于构建、运行和分析用 Java 和其他面向 JVM 的语言编写的 nano/micro/milli/macro 基准测试。）

JMH(Java Microbenchmark Harness)是用于代码微基准测试的工具套件，主要是基于方法层面的基准测试，精度可以达到纳秒级。该工具是由 Oracle 内部实现 JIT 的大牛们编写的，他们应该比任何人都了解 JIT 以及 JVM 对于基准测试的影响。

JMH 比较典型的应用场景如下：

    1. 想准确地知道某个方法需要执行多长时间，以及执行时间和输入之间的相关性
    2. 对比接口不同实现在给定条件下的吞吐量 
    3. 查看多少百分比的请求在多长时间内完成
    
# 加入依赖

因为 JMH 是 JDK9 自带的，如果是 JDK9 之前的版本需要加入如下依赖（目前 JMH 的最新版本为 1.34）：

```$xslt
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.34</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.34</version>
</dependency>
```
# JMH 基础

@BenchmarkMode

用来配置 Mode 选项，可用于类或者方法上，这个注解的 value 是一个数组，可以把几种 Mode 集合在一起执行，如：@BenchmarkMode({Mode.SampleTime, Mode.AverageTime})，还可以设置为 Mode.All，即全部执行一遍。


	* Throughput：整体吞吐量，每秒执行了多少次调用，单位为 ops/time
    * AverageTime：用的平均时间，每次操作的平均时间，单位为 time/op
    * SampleTime：随机取样，最后输出取样结果的分布 
    * SingleShotTime：只运行一次，往往同时把 Warmup 次数设为 0，用于测试冷启动时的性能
	* All：上面的所有模式都执行一次

@State

通过 State 可以指定一个对象的作用范围，JMH 根据 scope 来进行实例化和共享操作。@State 可以被继承使用，如果父类定义了该注解，子类则无需定义。由于 JMH 允许多线程同时执行测试，不同的选项含义如下：

	* Scope.Benchmark：所有测试线程共享一个实例，测试有状态实例在多线程共享下的性能
	* Scope.Group：同一个线程在同一个 group 里共享实例
	* Scope.Thread：默认的 State，每个测试线程分配一个实例

@OutputTimeUnit

为统计结果的时间单位，可用于类或者方法注解

@Warmup

预热所需要配置的一些基本测试参数，可用于类或者方法上。一般前几次进行程序测试的时候都会比较慢，所以要让程序进行几轮预热，保证测试的准确性。参数如下所示：

	* iterations：预热的次数
	* time：每次预热的时间
	* timeUnit：时间的单位，默认秒
	* batchSize：批处理大小，每次操作调用几次方法

为什么需要预热？ 因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译为机器码，从而提高执行速度，所以为了让 benchmark 的结果更加接近真实情况就需要进行预热。

@Measurement

实际调用方法所需要配置的一些基本测试参数，可用于类或者方法上，参数和 @Warmup 相同。

@Threads

每个进程中的测试线程，可用于类或者方法上。

@Fork

进行 fork 的次数，可用于类或者方法上。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。

@Param

指定某项参数的多种情况，特别适合用来测试一个函数在不同的参数输入的情况下的性能，只能作用在字段上，使用该注解必须定义 @State 注解。

[官方提供的 jmh 示例 demo](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)

# main 方法执行基准测试

```$xslt
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(BeanCopyBenchmark.class.getSimpleName())
                .result("D:\\beanCopyResult.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }
--------------------------------------------------------------------------------------------------
# Run complete. Total time: 00:15:08

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                       Mode  Cnt   Score   Error  Units
BeanCopyBenchmark.testBeanUtil  avgt   30  13.144 ± 1.747  us/op
BeanCopyBenchmark.testOrika     avgt   30   2.782 ± 0.065  us/op

Benchmark result is saved to D:\beanCopyResult.json
```
# 生成 jar 包执行

对于一些小测试，直接用上面的方式写一个 main 函数手动执行就好了。对于大型的测试，需要测试的时间比较久、线程数比较多，加上测试的服务器需要，一般要放在 Linux 服务器里去执行。JMH 官方提供了生成 jar 包的方式来执行，我们需要在 maven 里增加一个 plugin，具体配置如下：

```$xslt
<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>jmh-demo</finalName>
                            <transformers>
                                <transformer
                                        implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>org.openjdk.jmh.Main</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
接着执行 maven 的命令生成可执行 jar 包并执行：
```$xslt
mvn clean install
java -jar target/jmh.jar StringConnectBenchmark
```
# JMH 可视化

除此以外，如果你想将测试结果以图表的形式可视化，可以试下这些网站：

[JMH Visual Chart](http://deepoove.com/jmh-visual-chart)

[Visualizer](https://jmh.morethan.io)

# JMH 陷阱

在使用 JMH 的过程中，一定要避免一些陷阱。 比如 JIT 优化中的死码消除，比如以下代码：

```$xslt
@Benchmark
public void testStringAdd(Blackhole blackhole) {
    String a = "";
    for (int i = 0; i < length; i++) {
        a += i;
    }
}
```
JVM 可能会认为变量 a 从来没有使用过，从而进行优化把整个方法内部代码移除掉，这就会影响测试结果。

JMH 提供了两种方式避免这种问题，一种是将这个变量作为方法返回值 return a，一种是通过 Blackhole 的 consume 来避免 JIT 的优化消除。

其他陷阱还有常量折叠与常量传播、永远不要在测试中写循环、使用 Fork 隔离多个测试方法、方法内联、伪共享与缓存行、分支预测、多线程测试等，感兴趣的可以阅读 https://github.com/lexburner/JMH-samples 了解全部的陷阱。