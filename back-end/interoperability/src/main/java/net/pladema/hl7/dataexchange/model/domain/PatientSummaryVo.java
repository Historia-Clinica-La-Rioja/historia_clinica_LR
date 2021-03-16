package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PatientSummaryVo {

    public static final PatientSummaryVo EMPTY = new PatientSummaryVo();

    private PatientVo patient;

    private List<ConditionVo> conditions=new ArrayList<>();

    private List<MedicationVo> medications=new ArrayList<>();

    private List<ImmunizationVo> immunizations=new ArrayList<>();

    private List<AllergyIntoleranceVo> allergies=new ArrayList<>();

    private OrganizationVo organization;

    public void setPatient(PatientVo resource){
        this.patient=resource;
    }

    public void addCondition(ConditionVo resource){
        conditions.add(resource);
    }

    public void addMedication(MedicationVo resource){
        medications.add(resource);
    }

    public void addImmunization(ImmunizationVo resource){
        immunizations.add(resource);
    }

    public void addAllergy(AllergyIntoleranceVo resource){
        allergies.add(resource);
    }

    public void setOrganization(OrganizationVo resource){
        this.organization=resource;
    }

    public static PatientSummaryVo emptyInstance(){
        return EMPTY;
    }
}
