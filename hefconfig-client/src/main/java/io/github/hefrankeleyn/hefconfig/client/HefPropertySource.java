package io.github.hefrankeleyn.hefconfig.client;


import io.github.hefrankeleyn.hefconfig.client.service.HefConfigService;
import org.springframework.core.env.EnumerablePropertySource;
/**
 * @Date 2024/5/27
 * @Author lifei
 */
public class HefPropertySource extends EnumerablePropertySource<HefConfigService> {


    public HefPropertySource(String name, HefConfigService source) {
        super(name, source);
    }

    @Override
    public String[] getPropertyNames() {
        return source.getPropertyNames();
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
