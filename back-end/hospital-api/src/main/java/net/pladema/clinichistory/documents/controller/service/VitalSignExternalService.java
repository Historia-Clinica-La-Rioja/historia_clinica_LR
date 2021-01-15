package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;

public interface VitalSignExternalService {

    NewVitalSignsObservationDto saveVitalSigns(Integer patientId, NewVitalSignsObservationDto vitalSignsObservationDto);

}
