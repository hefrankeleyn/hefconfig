package io.github.hefrankeleyn.hefconfig.demo.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Date 2024/5/27
 * @Author lifei
 */
@ConfigurationProperties(prefix = "hef")
public class HefConfig {

    private String b;
    private String c;

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
}
