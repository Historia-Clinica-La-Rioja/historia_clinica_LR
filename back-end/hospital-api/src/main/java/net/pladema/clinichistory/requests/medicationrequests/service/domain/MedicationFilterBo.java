package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicationFilterBo {

    private final Integer patientId;

    private final String statusId;

    private final String medicationStatement;

    private final String healthCondition;
}
