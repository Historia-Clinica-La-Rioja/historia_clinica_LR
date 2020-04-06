package net.pladema.security.service.enums;

public enum ETokenType {

    NORMAL("normal"),
    REFRESH("refresh"),
    VERIFICATION("verification");
 
    private String url;
 
    ETokenType(String envUrl) {
        this.url = envUrl;
    }
 
    public String getUrl() {
        return url;
    }
}
