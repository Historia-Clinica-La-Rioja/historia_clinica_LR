package net.pladema.clinichistory.external.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistorySummaryVo;

import java.time.LocalDate;

@Getter
@Setter
public class ExternalClinicalHistorySummaryBo {

    private Integer id;

    private String professionalSpecialty;

    private LocalDate consultationDate;

    private String professionalName;

    private String notes;

    private String institution;

    public ExternalClinicalHistorySummaryBo(ExternalClinicalHistorySummaryVo externalClinicalHistorySummaryVo){
        this.id = externalClinicalHistorySummaryVo.getId();
        this.professionalSpecialty = externalClinicalHistorySummaryVo.getProfessionalSpecialty().equals("-") ? null: externalClinicalHistorySummaryVo.getProfessionalSpecialty();
        this.consultationDate = externalClinicalHistorySummaryVo.getConsultationDate();
        this.professionalName = externalClinicalHistorySummaryVo.getProfessionalName().equals("-") ? null: externalClinicalHistorySummaryVo.getProfessionalName();
        this.notes = externalClinicalHistorySummaryVo.getNotes();
        this.institution = externalClinicalHistorySummaryVo.getInstitution().equals("-") ? null : externalClinicalHistorySummaryVo.getInstitution();
    }
}


