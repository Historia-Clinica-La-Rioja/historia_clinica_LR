package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;
import net.pladema.clinichistory.documents.controller.dto.VitalSignObservationDto;

public interface VitalSignExternalService {

    NewVitalSignsObservationDto saveVitalSigns(Integer patientId, NewVitalSignsObservationDto vitalSignsObservationDto);

    VitalSignObservationDto getVitalSignObservationById(Integer vitalSignObservationId);

}
