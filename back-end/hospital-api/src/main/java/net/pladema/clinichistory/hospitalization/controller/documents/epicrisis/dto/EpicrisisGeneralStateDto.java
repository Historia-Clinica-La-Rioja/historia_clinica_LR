package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EpicrisisGeneralStateDto implements Serializable {

    private HealthConditionDto mainDiagnosis;

    private List<DiagnosisDto> diagnosis = new ArrayList<>();

    private List<HealthHistoryConditionDto> personalHistories = new ArrayList<>();

    private List<HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    private List<MedicationDto> medications = new ArrayList<>();

    private List<ImmunizationDto> immunizations= new ArrayList<>();

    private List<AllergyConditionDto> allergies = new ArrayList<>();

}
