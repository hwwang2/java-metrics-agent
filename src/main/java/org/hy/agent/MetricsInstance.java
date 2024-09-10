package org.hy.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hy.logger.AgentLogger;

import java.io.FileNotFoundException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class MetricsInstance {
    private MetricsInstance() {
    }

    private static MetricsInstance instance = new MetricsInstance();

    public static MetricsInstance getInstance() {
        return instance;
    }

    public void init(String file, Instrumentation inst) throws FileNotFoundException {
        Config config = new Config();
        config.loadFromYaml(file);
//        config.loadFromYaml("D:\\code\\MyAgent\\src\\main\\resources\\test.yml");
        if (config.metrics != null) {
            for (Config.MetricItem item : config.metrics) {
                ElementMatcher.Junction<NamedElement> matcher = ElementMatchers.named("-xx");
                if ("prefix".equalsIgnoreCase(item.matcher)) {
                    for (String cls : item.clz) {
                        matcher = matcher.or(ElementMatchers.nameStartsWith(cls));
                    }
                } else if ("regex".equalsIgnoreCase(item.matcher)) {
                    for (String cls : item.clz) {
                        matcher = matcher.or(ElementMatchers.nameMatches(cls));
                    }
                } else if ("equal".equalsIgnoreCase(item.matcher)) {
                    for (String cls : item.clz) {
                        matcher = matcher.or(ElementMatchers.named(cls));
                    }
                }
                AgentBuilder.Identified.Extendable builder1 = new AgentBuilder.Default()
                        .type(matcher)
                        .transform((builder, typeDescription, classLoader) ->
                                builder.method(ElementMatchers.nameMatches(item.method))
                                        .intercept(MethodDelegation.to(MethodInterceptor.class)));
                // 装载到 instrumentation 上
                builder1.installOn(inst);
            }
        }
        executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            public Thread newThread(Runnable r) {
                Thread thread = this.defaultFactory.newThread(r);
                if (!thread.isDaemon()) {
                    thread.setDaemon(true);
                }

                thread.setName("metrics-hy-" + this.threadNumber.getAndIncrement());
                return thread;
            }
        });
        executorService.scheduleAtFixedRate(() -> {
            if (MethodInterceptor.counterMap.isEmpty()) {
                AgentLogger.log(Level.FINE, "Agent耗时统计：无调用");
            } else {
                for (Map.Entry<String, MethodCounter> a : MethodInterceptor.counterMap.entrySet()) {
                    AgentLogger.log(Level.INFO, "Agent耗时统计：%s %s%n", a.getKey(), a.getValue().toString());
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    private ScheduledExecutorService executorService;

    public void stop() {
        if (executorService != null)
            executorService.shutdown();
    }
}
