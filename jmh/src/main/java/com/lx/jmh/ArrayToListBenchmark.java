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
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lx
 * @date 2021/8/10 13:21
 */

@Warmup(iterations = 5)
@Measurement(iterations = 50)
@BenchmarkMode(Mode.AverageTime)
@Threads(4)
@Fork(3)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ArrayToListBenchmark {

    //    报错
    public List testArrayCastToListError() {
        String[] strArray = new String[2];
        List list = Arrays.asList(strArray);
        //对转换后的list插入一条数据
        list.add("1");
        return list;
    }

    @Benchmark
    public ArrayList<String> testArrayCastToListRight() {
        String[] strArray = new String[2];
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(strArray));
        list.add("1");
        return list;
    }

    @Benchmark
    public ArrayList<String> testArrayCastToListEfficient() {
        String[] strArray = new String[2];
        ArrayList<String> arrayList = new ArrayList<String>(strArray.length);
        Collections.addAll(arrayList, strArray);
        arrayList.add("1");
        return arrayList;
    }

    public static void main(String[] args) throws RunnerException {
//        ArrayToListBenchmark arrayToListBenchmark = new ArrayToListBenchmark();
//        arrayToListBenchmark.testArrayCastToListError();

        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(ArrayToListBenchmark.class.getSimpleName())
                .result("D:\\arrayToListResult.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();

    }

}
