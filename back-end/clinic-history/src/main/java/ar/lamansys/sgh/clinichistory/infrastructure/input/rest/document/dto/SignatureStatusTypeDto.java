package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto;

import ar.lamansys.sgh.clinichistory.domain.document.SignatureStatusTypeBo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignatureStatusTypeDto {

    private Short id;

    private String description;

	public SignatureStatusTypeDto(SignatureStatusTypeBo bo){
		this.id = bo.getId();
		this.description = bo.getDescription();
	}

}
