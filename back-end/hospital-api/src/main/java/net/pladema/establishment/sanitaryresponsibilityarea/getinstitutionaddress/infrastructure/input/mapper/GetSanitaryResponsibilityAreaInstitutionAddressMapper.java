package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.mapper;

import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.domain.GetSanitaryResponsibilityAreaInstitutionAddressBo;
import net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.rest.dto.GetSanitaryResponsibilityAreaInstitutionAddressDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface GetSanitaryResponsibilityAreaInstitutionAddressMapper {

	@Mapping(target = "state.id", source = "stateId")
	@Mapping(target = "state.description", source = "stateName")
	@Mapping(target = "department.id", source = "departmentId")
	@Mapping(target = "department.description", source = "departmentName")
	@Mapping(target = "city.id", source = "cityId")
	@Mapping(target = "city.description", source = "cityName")
	@Named("fromGetSanitaryResponsibilityAreaInstitutionAddressBo")
	GetSanitaryResponsibilityAreaInstitutionAddressDto fromGetSanitaryResponsibilityAreaInstitutionAddressBo(GetSanitaryResponsibilityAreaInstitutionAddressBo getSanitaryResponsibilityAreaInstitutionAddressBo);

}
