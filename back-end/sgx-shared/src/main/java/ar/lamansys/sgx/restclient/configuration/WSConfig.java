package ar.lamansys.sgx.restclient.configuration;

import org.springframework.util.Assert;

public class WSConfig {
	
    protected String baseUrl;

    public WSConfig(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getBaseUrl(){
    	return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        Assert.notNull(baseUrl, "WS base URL can not be null");
        this.baseUrl = baseUrl;
    }

    public String getAbsoluteURL(String relURL) {
        Assert.notNull(relURL, "WS relative URL can not be null");
        return joinURL(this.baseUrl, relURL);
    }

    public static String joinURL(String prefix, String suffix) {
        return String.format("%s/%s", prefix.replaceAll("/$", ""), suffix.replaceAll("^/", ""));
    }


}
