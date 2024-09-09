package org.hy.agent;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * 示例
 * metric:
 * items:
 * - matcher: contains
 * - clz: [x,y]
 * - methods: [a,b,c]
 * - matcher: contains
 * - clz: [x1,y1]
 * - methods: [a,b,c]
 */
public class Config {
    public List<MetricItem> metrics;

    public static class MetricItem {
        public String matcher;
        public List<String> clz;
        public String method;
    }

    public void loadFromYaml(String file) throws FileNotFoundException {
        Config config = new Yaml().loadAs(new FileReader(file), Config.class);
        if (config != null)
            metrics = config.metrics;
    }
}
