package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.dates.controller.dto.DateDto;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class MedicationInfoDto implements Serializable {

    private SnomedDto snomed;

    private HealthConditionInfoDto healthCondition;

    private DosageInfoDto dosage;

    private String statusId;

    private boolean expired;

    private DateDto startDate;

    private String observations;


}
