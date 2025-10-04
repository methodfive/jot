package com.method5.jot.query.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * SystemProperties — class for system properties in the Jot SDK. Provides types and data models.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemProperties {
    @JsonProperty("ss58Format")
    private Integer ss58Format;

    @JsonProperty("tokenSymbol")
    private String tokenSymbol;

    @JsonProperty("tokenDecimals")
    private Integer tokenDecimals;

    @JsonProperty
    private Map<String, Object> additional;

    public Integer getSs58Format() {
        return ss58Format;
    }

    public void setSs58Format(Integer ss58Format) {
        this.ss58Format = ss58Format;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public Integer getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(Integer tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public Map<String, Object> getAdditional() {
        return additional;
    }

    public void setAdditional(Map<String, Object> additional) {
        this.additional = additional;
    }
}
