package org.hy;

import java.util.Date;
import java.util.Map;

public class PrintThread extends Thread {
    public void run() {
        while (true) {
            try {
                if (MainInterceptor.counterMap.isEmpty()) {
                    System.out.println("Agent耗时统计：无调用");
                } else {
                    for (Map.Entry<String, MethodCounter> a : MainInterceptor.counterMap.entrySet()) {
                        System.out.printf("%tc Agent耗时统计：%s %s%n", new Date(), a.getKey(), a.getValue().toString());
                    }
                }
                Thread.sleep(1000 * 60);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
