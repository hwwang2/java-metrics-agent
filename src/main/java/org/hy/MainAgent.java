package org.hy;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

public class MainAgent {
    private static String clazz = "com.jorsun.game.server.battle.util.BattleUtil";//要拦截的类（包）

    private static String method = "createFightModelPoint";//要拦截的方法名
//    private static String clazz = "org.hy.Main";//要拦截的类（包）
//
//    private static String method = "add";//要拦截的方法名


//    public MainAgent(String clazz, String method) {
//        MainAgent.clazz = clazz;
//        MainAgent.method = method;
//    }

    /*agentArgs 是 premain 函数得到的程序参数，随同 “– javaagent”一起传入*/
    /*Inst 是一个 java.lang.instrument.Instrumentation 的实例，由 JVM 自动传入*/
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Agent 监控正在启动");
        try {
            // 拦截spring controller
            AgentBuilder.Identified.Extendable builder1 = new AgentBuilder.Default()
                    // 拦截@Controller 和 @RestController的类
//                    .type(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.stereotype.Controller")
//                            .or(ElementMatchers.named("org.springframework.web.bind.annotation.RestController"))))
                    .type(ElementMatchers.named(clazz)
//                            .or(ElementMatchers.nameMatches("com.jorsun.game.server.*Handler*"))
                            .or(ElementMatchers.nameContains("MainMapThread"))
                            .or(ElementMatchers.nameContains("BattleProcessor")))
                    .transform((builder, typeDescription, classLoader) ->
                            // 拦截 @RestMapping 或者 @Get/Post/Put/DeleteMapping
//                            builder.method(ElementMatchers.isPublic().and(ElementMatchers.isAnnotatedWith(
//                                    ElementMatchers.nameStartsWith("org.springframework.web.bind.annotation")
//                                            .and(ElementMatchers.nameEndsWith("Mapping")))))
//                            builder.method(ElementMatchers.isPublic().and(ElementMatchers.nameStartsWith(method)))
                            builder.method(ElementMatchers.nameStartsWith("main"))
                                    .intercept(MethodDelegation.to(MainInterceptor.class)));
            // 装载到 instrumentation 上
            builder1.installOn(inst);
            AgentBuilder.Identified.Extendable builder2 = new AgentBuilder.Default()
                    // 拦截@Controller 和 @RestController的类
//                    .type(ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.stereotype.Controller")
//                            .or(ElementMatchers.named("org.springframework.web.bind.annotation.RestController"))))
                    .type(ElementMatchers.nameContains("NpcFightModelEntity"))
                    .transform((builder, typeDescription, classLoader) ->
                            // 拦截 @RestMapping 或者 @Get/Post/Put/DeleteMapping
//                            builder.method(ElementMatchers.isPublic().and(ElementMatchers.isAnnotatedWith(
//                                    ElementMatchers.nameStartsWith("org.springframework.web.bind.annotation")
//                                            .and(ElementMatchers.nameEndsWith("Mapping")))))
//                            builder.method(ElementMatchers.isPublic().and(ElementMatchers.nameStartsWith(method)))
                            builder.method(ElementMatchers.any())
                                    .intercept(MethodDelegation.to(MainInterceptor.class)));
            // 装载到 instrumentation 上
            builder2.installOn(inst);

            PrintThread printThread = new PrintThread();
            printThread.setDaemon(true);
            printThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}