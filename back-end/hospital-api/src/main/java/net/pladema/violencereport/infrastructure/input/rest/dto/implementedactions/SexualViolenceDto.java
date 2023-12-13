package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.ESexualViolenceAction;

import javax.validation.constraints.NotNull;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SexualViolenceDto {

	@NotNull(message = "{value.mandatory}")
	private Boolean wasSexualViolence;

	private List<ESexualViolenceAction> implementedActions;

}
