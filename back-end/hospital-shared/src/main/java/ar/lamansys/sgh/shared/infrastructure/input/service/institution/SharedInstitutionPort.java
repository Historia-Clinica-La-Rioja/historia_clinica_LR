package ar.lamansys.sgh.shared.infrastructure.input.service.institution;


import ar.lamansys.sgh.shared.domain.general.AddressBo;

public interface SharedInstitutionPort {

    InstitutionInfoDto fetchInstitutionById(Integer id);

	InstitutionInfoDto fetchInstitutionDataById(Integer id);

	AddressBo fetchInstitutionAddress(Integer id);

	InstitutionInfoDto fetchInstitutionBySisaCode(String sisaCode);
}
