package ar.lamansys.sgx.shared.publicinfo.infrastructure.input.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApplicationVersionDto {

    public final String version;
	public final String branch;
	public final String commitId;

}
