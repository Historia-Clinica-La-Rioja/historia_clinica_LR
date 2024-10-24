package net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportInstitutionalizedDto {

	private Boolean isInstitutionalized;

	private String institutionalizedDetails;

}
