package org.hy;

import com.alibaba.fastjson2.JSON;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.StringMatcher;
import org.hy.agent.Config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试用
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        StringMatcher matcher1 = new StringMatcher("main.*", StringMatcher.Mode.MATCHES);
        System.out.println(matcher1.matches("main"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        ZoneId zoneId = ZoneId.systemDefault(); // Use the system default time zone
        LocalDateTime localDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(zoneId).toLocalDateTime();
        System.out.println(formatter.format(localDate));
        for (int i = 0; i < 10000; i++) {
            System.out.println(main1(122, 345));
            Thread.sleep(1000);
        }
    }

    public static int main1(int a, int b) {
        return a + b;
    }
}
