package ar.lamansys.sgx.auth.jwt.domain.token;

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
