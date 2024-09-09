package org.hy.agent;

public class MethodCounter {
    public long times;
    public long cost;
    public long max;
    public long up10ms;
    public long up20ms;
    public long up50ms;
    public long up100ms;

    @Override
    public String toString() {
        return "总次数:" + times + " 平均耗时：" + cost / times + " 最大耗时：" + max
                + " 超过10ms次数：" + up10ms + " 超过20ms次数：" + up20ms
                + " 超过50ms次数：" + up50ms + " 超过100ms次数：" + up100ms;
    }
}
