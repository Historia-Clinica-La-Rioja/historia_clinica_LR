package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EBirthCondition;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EGender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class NewbornDto {

	@Nullable
	private Integer id;
	@NotNull(message = "{value.mandatory}")
	private Short weight;
	@NotNull(message = "{value.mandatory}")
	private EBirthCondition birthConditionType;
	@NotNull(message = "{value.mandatory}")
	private EGender genderId;

}
