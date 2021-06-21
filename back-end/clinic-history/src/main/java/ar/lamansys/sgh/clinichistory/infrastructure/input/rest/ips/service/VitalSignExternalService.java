package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewVitalSignsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignObservationDto;

public interface VitalSignExternalService {

    NewVitalSignsObservationDto saveVitalSigns(Integer patientId, NewVitalSignsObservationDto vitalSignsObservationDto);

    VitalSignObservationDto getVitalSignObservationById(Integer vitalSignObservationId);

}
