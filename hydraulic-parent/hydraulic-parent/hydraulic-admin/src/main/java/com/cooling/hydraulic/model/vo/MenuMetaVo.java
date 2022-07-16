package com.cooling.hydraulic.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public class MenuMetaVo implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getNoCache() {
        return noCache;
    }

    public void setNoCache(Boolean noCache) {
        this.noCache = noCache;
    }

    public MenuMetaVo(String title, String icon, Boolean noCache) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }
}
