package net.pladema.clinichistory.external.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistoryVo;

import java.time.LocalDate;

@Getter
@Setter
public class ExternalClinicalHistoryBo {

    private Integer id;

    private String professionalSpecialty;

    private LocalDate consultationDate;

    private String professionalName;

    private String notes;

    private String institution;

    public ExternalClinicalHistoryBo(ExternalClinicalHistoryVo externalClinicalHistoryVo){
        this.id = externalClinicalHistoryVo.getId();
        this.professionalSpecialty = externalClinicalHistoryVo.getProfessionalSpecialty().equals("-") ? null: externalClinicalHistoryVo.getProfessionalSpecialty();
        this.consultationDate = externalClinicalHistoryVo.getConsultationDate();
        this.professionalName = externalClinicalHistoryVo.getProfessionalName().equals("-") ? null: externalClinicalHistoryVo.getProfessionalName();
        this.notes = externalClinicalHistoryVo.getNotes();
        this.institution = externalClinicalHistoryVo.getInstitution().equals("-") ? null : externalClinicalHistoryVo.getInstitution();
    }
}


