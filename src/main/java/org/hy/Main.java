package org.hy;

import net.bytebuddy.matcher.StringMatcher;
import org.hy.agent.Config;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 测试用
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Yaml().loadAs(new FileReader("D:\\\\code\\\\MyAgent\\\\src\\\\main\\\\resources\\\\test.yml"), Config.class);
        System.out.println(config);
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
