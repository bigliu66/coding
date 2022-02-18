package com.lx.jmh;

import com.lx.jmh.entity.User;
import com.lx.jmh.entity.UserVo;
import com.lx.jmh.utils.OrikaUtil;
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
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lx
 * @date 2021/8/10 13:21
 */

@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@Threads(4)
@Fork(3)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BeanCopyBenchmark {

    @Benchmark
    public UserVo testBeanUtil() {
        User user = new User("张三", "1", 20, "vip", "电影、音乐", "18888888888", "***", "***", "军人");
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        userVo.setUsername(user.getName());
        return userVo;
    }

    @Benchmark
    public UserVo testOrika() {
        User user = new User("张三", "1", 20, "vip", "电影、音乐", "18888888888", "***", "***", "军人");
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", "username");
        return OrikaUtil.INSTANCE.map(UserVo.class, user, userMap);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 导入要测试的类
                .include(BeanCopyBenchmark.class.getSimpleName())
                .result("D:\\beanCopyResult.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }

}
