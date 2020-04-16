package net.pladema.patient.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import net.pladema.patient.controller.dto.PatientSearchDto;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.person.controller.mapper.PersonMapper;

@Mapper(uses = PersonMapper.class)
public interface PatientMapper {

    public PatientSearchDto fromPatientSearch(PatientSearch patientSeach);

    public List<PatientSearchDto>
    fromListPatientSearch(List<PatientSearch> patientSeach);
    
}
