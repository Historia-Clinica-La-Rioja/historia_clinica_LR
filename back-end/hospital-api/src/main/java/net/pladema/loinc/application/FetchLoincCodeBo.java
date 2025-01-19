package net.pladema.loinc.application;

import lombok.Value;

@Value
public class FetchLoincCodeBo {
	private Integer id;

	private String description;

	private String code;

	private String displayName;

	private String customDisplayName;

    public static FetchLoincCodeBo empty() {
    	return new FetchLoincCodeBo(null, "", "", "", "");
    }
}
