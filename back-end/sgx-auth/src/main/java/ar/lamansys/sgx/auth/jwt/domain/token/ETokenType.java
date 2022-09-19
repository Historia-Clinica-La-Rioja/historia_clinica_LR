package ar.lamansys.sgx.auth.jwt.domain.token;

public enum ETokenType {

    NORMAL("normal"),
    REFRESH("refresh"),
    VERIFICATION("verification"),
    PARTIALLY_AUTHENTICATED("partially_authenticated")
    ;
 
    private String url;
 
    ETokenType(String envUrl) {
        this.url = envUrl;
    }
 
    public String getUrl() {
        return url;
    }
}
