package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.patient.controller.dto.BasicPatientDto;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNoteBo implements Document {

    private Long id;

    private boolean confirmed;

    private DocumentObservationsBo notes;

    private HealthConditionBo mainDiagnosis;

    private List<DiagnosisBo> diagnosis;

    private List<ImmunizationBo> immunizations;

    private List<AllergyConditionBo> allergies;

    private List<ProcedureBo> procedures;

    private AnthropometricDataBo anthropometricData;

    private VitalSignBo vitalSigns;

    @Override
    public short getDocumentType() {
        return 0;
    }

    @Override
    public Integer getEncounterId() {
        return null;
    }

    @Override
    public Short getDocumentSource() {
        return null;
    }

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    @Override
    public Integer getPatientId() {
        return null;
    }

}
