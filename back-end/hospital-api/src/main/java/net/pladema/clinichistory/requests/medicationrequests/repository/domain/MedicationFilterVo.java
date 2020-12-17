package net.pladema.clinichistory.requests.medicationrequests.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MedicationFilterVo {

    private final Integer patientId;

    private final String statusId;

    private final String medicationStatement;

    private final String healthCondition;
}
