package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class CoverageActivityInfoDto implements Serializable {

    private String affiliateNumber;
    private Boolean attentionCoverage;
    private String cuitCoverage;
	private String plan;
}
