package ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto;

import ar.lamansys.sgx.shared.validation.CustomContainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Schema(name = "ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReferableItemDto", implementation = ReferableItemDto.class)
public class ReferableItemDto<T> extends CustomContainer<T> {

	private Boolean isReferred;

}
