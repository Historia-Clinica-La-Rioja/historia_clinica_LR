package net.pladema.clinichistory.external.service.domain;

import lombok.Getter;
import lombok.Setter;

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

    public ExternalClinicalHistoryBo(Integer id, String professionalSpecialty,
									 LocalDate consultationDate, String professionalName,
									 String notes, String institution){
        this.id = id;
        this.professionalSpecialty = EMPTY.equals(professionalSpecialty) ? null : professionalSpecialty;
        this.consultationDate = consultationDate;
        this.professionalName = EMPTY.equals(professionalName) ? null : professionalName;
        this.notes = notes;
        this.institution = EMPTY.equals(institution) ? null : institution;
    }
}


