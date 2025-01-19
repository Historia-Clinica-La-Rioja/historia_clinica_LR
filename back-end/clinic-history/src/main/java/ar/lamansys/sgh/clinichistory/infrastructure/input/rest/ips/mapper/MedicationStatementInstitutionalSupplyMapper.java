package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyBo;

import ar.lamansys.sgh.clinichistory.domain.ips.SaveMedicationStatementInstitutionalSupplyMedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SaveMedicationStatementInstitutionalSupplyDto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SaveMedicationStatementInstitutionalSupplyMedicationDto;

import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface MedicationStatementInstitutionalSupplyMapper {

	@Named("fromSaveMedicationStatementInstitutionalSupplyMedicationDto")
	SaveMedicationStatementInstitutionalSupplyMedicationBo fromSaveMedicationStatementInstitutionalSupplyMedicationDto(SaveMedicationStatementInstitutionalSupplyMedicationDto saveMedicationStatementInstitutionalSupplyMedicationDto);

	@IterableMapping(qualifiedByName = "fromSaveMedicationStatementInstitutionalSupplyMedicationDto")
	@Named("fromSaveMedicationStatementInstitutionalSupplyMedicationDtoList")
	List<SaveMedicationStatementInstitutionalSupplyMedicationBo> fromSaveMedicationStatementInstitutionalSupplyMedicationDtoList(List<SaveMedicationStatementInstitutionalSupplyMedicationDto> saveMedicationStatementInstitutionalSupplyMedicationDto);

	@Mapping(target = "institutionId", expression = "java(institutionId)")
	@Mapping(source = "medications", target = "medications", qualifiedByName = "fromSaveMedicationStatementInstitutionalSupplyMedicationDtoList")
	@Named("fromSaveMedicationStatementInstitutionalSupplyDto")
	SaveMedicationStatementInstitutionalSupplyBo fromSaveMedicationStatementInstitutionalSupplyDto(@Context Integer institutionId, SaveMedicationStatementInstitutionalSupplyDto saveMedicationStatementInstitutionalSupplyDto);

}
