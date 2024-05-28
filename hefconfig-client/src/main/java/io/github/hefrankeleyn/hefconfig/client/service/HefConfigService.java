package io.github.hefrankeleyn.hefconfig.client.service;

public interface HefConfigService {
    String[] getPropertyNames();
    String getProperty(String name);
}
