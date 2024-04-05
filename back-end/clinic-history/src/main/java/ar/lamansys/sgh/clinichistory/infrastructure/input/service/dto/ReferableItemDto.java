package ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto;

import ar.lamansys.sgx.shared.validation.CustomContainer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReferableItemDto<T> extends CustomContainer<T> {

	private Boolean isReferred;

}
