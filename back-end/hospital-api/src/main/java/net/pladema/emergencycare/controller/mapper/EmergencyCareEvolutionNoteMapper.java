package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDocumentDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDto;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, HealthcareProfessionalMapper.class, SnomedMapper.class})
public interface EmergencyCareEvolutionNoteMapper {

	@Named("fromEmergencyCareEvolutionNoteDto")
	EmergencyCareEvolutionNoteDocumentBo fromEmergencyCareEvolutionNoteDto(EmergencyCareEvolutionNoteDto emergencyCareEvolutionNote);

	@Named("toEmergencyCareEvolutionNoteDocumentDto")
	@Mapping(target = "documentId", source = "id")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.mainDiagnosis", source = "mainDiagnosis")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.diagnosis", source = "diagnosis")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.reasons", source = "reasons")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.anthropometricData", source = "anthropometricData")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.riskFactors", source = "riskFactors")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.familyHistories", source = "familyHistories")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.medications", source = "medications")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.allergies", source = "allergies")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.procedures", source = "procedures")
	@Mapping(target = "emergencyCareEvolutionNoteClinicalData.evolutionNote", source = "evolutionNote")
	@Mapping(target = "professional", source = "professional", qualifiedByName = "fromHealthcareProfessionalBo")
	EmergencyCareEvolutionNoteDocumentDto toEmergencyCareEvolutionNoteDocumentDto(EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNote);

	@Named("toEmergencyCareEvolutionNoteDocumentListDto")
	@IterableMapping(qualifiedByName = "toEmergencyCareEvolutionNoteDocumentDto")
	List<EmergencyCareEvolutionNoteDocumentDto> toEmergencyCareEvolutionNoteDocumentListDto(List<EmergencyCareEvolutionNoteDocumentBo> emergencyCareEvolutionNotes);

	@Named("toEmergencyCareEvolutionNoteDto")
	EmergencyCareEvolutionNoteDto toEmergencyCareEvolutionNoteDto(EmergencyCareEvolutionNoteDocumentBo emergencyCareEvolutionNoteDocumentBo);

}
