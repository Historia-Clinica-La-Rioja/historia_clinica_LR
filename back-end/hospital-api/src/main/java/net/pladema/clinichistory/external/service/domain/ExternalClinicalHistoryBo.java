package net.pladema.clinichistory.external.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistoryVo;

import java.time.LocalDate;

@Getter
@Setter
public class ExternalClinicalHistoryBo {

    private final String EMPTY = "-";

    private Integer id;

    private String professionalSpecialty;

    private LocalDate consultationDate;

    private String professionalName;

    private String notes;

    private String institution;

    public ExternalClinicalHistoryBo(ExternalClinicalHistoryVo externalClinicalHistoryVo){
        this.id = externalClinicalHistoryVo.getId();
        this.professionalSpecialty = EMPTY.equals(externalClinicalHistoryVo.getProfessionalSpecialty()) ? null: externalClinicalHistoryVo.getProfessionalSpecialty();
        this.consultationDate = externalClinicalHistoryVo.getConsultationDate();
        this.professionalName = EMPTY.equals(externalClinicalHistoryVo.getProfessionalName()) ? null: externalClinicalHistoryVo.getProfessionalName();
        this.notes = externalClinicalHistoryVo.getNotes();
        this.institution = EMPTY.equals(externalClinicalHistoryVo.getInstitution()) ? null : externalClinicalHistoryVo.getInstitution();
    }
}


