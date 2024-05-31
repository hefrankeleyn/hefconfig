package io.github.hefrankeleyn.hefconfig.client.beans;

import com.google.common.base.MoreObjects;

/**
 * @Date 2024/5/31
 * @Author lifei
 */
public class ConfigMetas {
    private String capp;
    private String cenv;
    private String cnamespace;
    private String cconfigServer;

    public ConfigMetas() {
    }

    public ConfigMetas(String capp, String cenv, String cnamespace, String cconfigServer) {
        this.capp = capp;
        this.cenv = cenv;
        this.cnamespace = cnamespace;
        this.cconfigServer = cconfigServer;
    }

    public String getCapp() {
        return capp;
    }

    public void setCapp(String capp) {
        this.capp = capp;
    }

    public String getCenv() {
        return cenv;
    }

    public void setCenv(String cenv) {
        this.cenv = cenv;
    }

    public String getCnamespace() {
        return cnamespace;
    }

    public void setCnamespace(String cnamespace) {
        this.cnamespace = cnamespace;
    }

    public String getCconfigServer() {
        return cconfigServer;
    }

    public void setCconfigServer(String cconfigServer) {
        this.cconfigServer = cconfigServer;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ConfigMetas.class)
                .add("capp", capp)
                .add("cenv", cenv)
                .add("cnamespace", cnamespace)
                .add("cconfigServer", cconfigServer)
                .toString();
    }
}