package org.hy;

public class MethodCounter {
    public long times;
    public long cost;
    public long max;
    public long up50ms;

    @Override
    public String toString() {
        return "总次数:" + times + " 平均耗时：" + cost / times + " 最大耗时：" + max + " 超过50ms次数：" + up50ms;
    }
}
