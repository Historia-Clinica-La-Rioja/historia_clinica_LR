package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ConsultationsVo {

    private Integer id;

    private LocalDate consultationDate;

    private String specialty;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;
}
