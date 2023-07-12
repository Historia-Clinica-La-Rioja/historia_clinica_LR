package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiagnosisDto extends HealthConditionDto {

    @Nullable
    private boolean presumptive = false;

	@Nullable
	private String problemTypeId = ProblemType.DIAGNOSIS;
}
