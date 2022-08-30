package ar.lamansys.sgh.shared.infrastructure.input.service.institution;


public interface SharedInstitutionPort {

    InstitutionInfoDto fetchInstitutionById(Integer id);

	InstitutionInfoDto fetchInstitutionBySisaCode(String sisaCode);
}
