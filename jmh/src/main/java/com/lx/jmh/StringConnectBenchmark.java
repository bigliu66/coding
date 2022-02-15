package com.lx.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@Threads(4)
@Fork(3)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringConnectBenchmark {

    /**
     * 字符串拼接之 StringBuilder 基准测试
     */
    @Benchmark
    public void testStringBuilder(Blackhole blackhole) {
        blackhole.consume(new StringBuilder().append(1).append(2).append(3).toString());
    }

    /**
     * 字符串拼接之直接相加基准测试
     */
    @Benchmark
    public void testStringAdd(Blackhole blackhole) {
        String add = "";
        add += "1";
        add += "2";
        add += "3";
        blackhole.consume(add);
    }

    /**
     * 字符串拼接之String Concat基准测试
     */
    @Benchmark
    public void testStringConcat(Blackhole blackhole) {
        blackhole.consume("".concat("1").concat("2").concat("3"));
    }

    /**
     * 字符串拼接之 StringBuffer 基准测试
     */
    @Benchmark
    public void testStringBuffer(Blackhole blackhole) {
        blackhole.consume(new StringBuffer().append(1).append(2).append(3).toString());
    }

    /**
     * 字符串拼接之 StringFormat 基准测试
     */
    @Benchmark
//    @Warmup(iterations = 5)
//    @Measurement(iterations = 10)
//    @BenchmarkMode(Mode.Throughput)
//    @Fork(3)
    public void testStringFormat(Blackhole blackhole) {
        blackhole.consume(String.format("%s%s%s", 1, 2, 3));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(StringConnectBenchmark.class.getSimpleName())
//                // 预热5轮
//                .warmupIterations(5)
//                // 度量10轮
//                .measurementIterations(10)
//                .mode(Mode.Throughput)
//                .forks(3)
                .result("D:\\stringConnectResult.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }

}