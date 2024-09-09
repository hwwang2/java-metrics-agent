package org.hy.agent;

import org.hy.logger.AgentLogger;

import java.lang.instrument.Instrumentation;
import java.util.logging.Level;

public class MetricsAgent {

    /*agentArgs 是 premain 函数得到的程序参数，随同 “– javaagent”一起传入*/
    /*Inst 是一个 java.lang.instrument.Instrumentation 的实例，由 JVM 自动传入*/
    public static void premain(String agentArgs, Instrumentation inst) {
        AgentLogger.log(Level.FINE, "Agent 监控正在启动");
        try {
            MetricsInstance.getInstance().init(agentArgs, inst);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                MetricsInstance.getInstance().stop();
            }));
            AgentLogger.log(Level.FINE, "Agent 监控启动完成");
        } catch (Exception e) {
            AgentLogger.log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
}