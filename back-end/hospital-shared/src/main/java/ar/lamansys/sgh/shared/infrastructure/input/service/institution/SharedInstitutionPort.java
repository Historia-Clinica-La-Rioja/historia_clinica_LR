package ar.lamansys.sgh.shared.infrastructure.input.service.institution;


import ar.lamansys.sgh.shared.domain.general.AddressBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressDto;

public interface SharedInstitutionPort {

    InstitutionInfoDto fetchInstitutionById(Integer id);

	InstitutionInfoDto fetchInstitutionDataById(Integer id);

	AddressBo fetchInstitutionAddress(Integer id);

	SharedAddressDto fetchAddress(Integer institutionId);

	InstitutionInfoDto fetchInstitutionBySisaCode(String sisaCode);
}
