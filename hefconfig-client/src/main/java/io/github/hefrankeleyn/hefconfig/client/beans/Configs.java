package io.github.hefrankeleyn.hefconfig.client.beans;

import com.google.common.base.MoreObjects;

/**
 * @Date 2024/5/26
 * @Author lifei
 */
public class Configs {
    private String capp;
    private String cenv;
    private String cnamespace;
    private String ckey;
    private String cvalue;

    public Configs() {
    }

    public Configs(String capp, String cenv, String cnamespace, String ckey, String cvalue) {
        this.capp = capp;
        this.cenv = cenv;
        this.cnamespace = cnamespace;
        this.ckey = ckey;
        this.cvalue = cvalue;
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

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Configs.class)
                .add("capp", capp)
                .add("cenv", cenv)
                .add("cnamespace", cnamespace)
                .add("ckey", ckey)
                .add("cvalue", cvalue)
                .toString();
    }
}
