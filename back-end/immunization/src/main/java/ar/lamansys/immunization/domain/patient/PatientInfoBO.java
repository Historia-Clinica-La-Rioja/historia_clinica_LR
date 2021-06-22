package ar.lamansys.immunization.domain.patient;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PatientInfoBO {

    private final Integer id;

    private final LocalDate birthday;

    public PatientInfoBO(Integer id, LocalDate birthday) {
        super();
        this.id = id;
        this.birthday = birthday;
    }

}
