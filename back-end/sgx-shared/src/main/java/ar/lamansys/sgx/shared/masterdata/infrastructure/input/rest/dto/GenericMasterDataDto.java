package ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.GenericMasterDataDto", implementation = GenericMasterDataDto.class)
public class GenericMasterDataDto<T extends Serializable> extends AbstractMasterdataDto<T> {

	private static final long serialVersionUID = 3363239709241228876L;

	private T id;

	private String description;

}
