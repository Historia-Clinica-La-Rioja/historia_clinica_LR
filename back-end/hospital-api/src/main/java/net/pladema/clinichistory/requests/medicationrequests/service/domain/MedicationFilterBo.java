package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import lombok.Getter;
import net.pladema.clinichistory.requests.service.domain.EMedicationStatus;

@Getter
public class MedicationFilterBo {

    private final Integer patientId;

    private final String statusId;

    private final String medicationStatement;

    private final String healthCondition;

    public MedicationFilterBo(Integer patientId, String statusId, String medicationStatement, String healthCondition){
        this.patientId = patientId;
        this.statusId = statusId;
        this.medicationStatement = medicationStatement;
        this.healthCondition = healthCondition;
    }
}
