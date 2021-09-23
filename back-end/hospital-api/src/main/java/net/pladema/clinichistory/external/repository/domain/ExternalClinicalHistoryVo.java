package net.pladema.clinichistory.external.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExternalClinicalHistoryVo implements Serializable {

    private Integer id;

    private String professionalSpecialty;

    private LocalDate consultationDate;

    private String professionalName;

    private String notes;

    private String institution;

}
