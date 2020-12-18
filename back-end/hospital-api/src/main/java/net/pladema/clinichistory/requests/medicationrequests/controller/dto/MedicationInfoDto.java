package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationInfoDto implements Serializable {

    private Integer id;

    private SnomedDto snomed;

    private HealthConditionInfoDto healthCondition;

    private DosageInfoDto dosage;

    private DoctorInfoDto doctor;

    private String statusId;

    private String observations;

    private Integer medicationRequestId;

    private boolean hasRecipe;
}
