package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface MasterDataMapper {

	@Named("fromMasterDataDto")
	default Short fromMasterDataDto(MasterDataDto masterDataDto){
		return masterDataDto.getId().shortValue();
	}
}
