package io.github.hefrankeleyn.hefconfig.demo.conf;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Date 2024/5/27
 * @Author lifei
 */
@ConfigurationProperties(prefix = "hef")
public class HefConfig {

    private String a;
    private String b;
    private String c;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(HefConfig.class)
                .add("a", a)
                .add("b", b)
                .add("c", c)
                .toString();
    }
}
