package net.pladema.establishment.controller.mapper;


import net.pladema.establishment.controller.dto.InstitutionBasicInfoDto;
import net.pladema.establishment.controller.dto.InstitutionDto;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.establishment.service.domain.InstitutionBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface InstitutionMapper {

	@Named("toInstitutionDto")
	@Mapping(source = "institution.addressId", target = "institutionAddressDto.addressId")
	InstitutionDto toInstitutionDto(Institution institution);

	@Named("toListInstitutionDto")
	@IterableMapping(qualifiedByName = "toInstitutionDto")
	List<InstitutionDto> toListInstitutionDto(List<Institution> roomList);

	@Named("toInstitutionBasicInfoDto")
	InstitutionBasicInfoDto toInstitutionBasicInfoDto(InstitutionBo institution);

	@Named("toListInstitutionBasicInfoDto")
	@IterableMapping(qualifiedByName = "toInstitutionBasicInfoDto")
	List<InstitutionBasicInfoDto> toListInstitutionBasicInfoDto(List<InstitutionBo> roomList);

	@Named("fromListInstitutionBasicInfoBo")
	List<InstitutionBasicInfoDto>  fromListInstitutionBasicInfoBo(List<InstitutionBasicInfoBo> institutionBasicInfoBoList);
}
