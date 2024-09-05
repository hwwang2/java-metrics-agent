package org.hy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class MainInterceptor {
    static Map<String, MethodCounter> counterMap = new ConcurrentHashMap<>();

    /**
     * @param callable 原线程的callble接口对象，执行call()方法,返回原函数运行结束的返回对象
     * @param method   reflect的方法,可以获取原方法各类信息
     * @param args     method传入的参数
     * @Autuor:xy.Gao
     * @Description:
     * @Date: 2018/8/21 15:44
     */

    @RuntimeType
    public static Object interceptor(@SuperCall Callable callable, @Origin Method method, @AllArguments Object[] args) throws Exception {
        MethodCounter counter = counterMap.computeIfAbsent(method.toGenericString(), k -> new MethodCounter());
        counter.times = counter.times + 1;
        long start = System.nanoTime();//调用前
        try {
            return callable.call();
        } finally {
            long cost = System.nanoTime() - start;
            counter.cost += cost;
            if (cost > counter.max) {
                counter.max = cost;
            }
            if (cost > 50 * 1000000) {
                counter.up50ms++;
            }
//            System.out.println("Agent: "+counter);
        }
    }
}
