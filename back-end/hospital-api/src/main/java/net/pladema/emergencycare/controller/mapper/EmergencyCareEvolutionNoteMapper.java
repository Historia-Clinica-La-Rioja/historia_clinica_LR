package net.pladema.emergencycare.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDto;
import net.pladema.emergencycare.service.domain.EmergencyCareEvolutionNoteDocumentBo;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class, HealthcareProfessionalMapper.class, SnomedMapper.class})
public interface EmergencyCareEvolutionNoteMapper {

	@Named("fromEmergencyCareEvolutionNoteDto")
	EmergencyCareEvolutionNoteDocumentBo fromEmergencyCareEvolutionNoteDto(EmergencyCareEvolutionNoteDto emergencyCareEvolutionNote);

}
