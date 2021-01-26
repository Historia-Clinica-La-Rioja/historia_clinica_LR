package net.pladema.medicalconsultation.doctorsoffice.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DoctorsOfficeVo {

    private Integer id;

    private String description;

    private LocalTime openingTime;

    private LocalTime closingTime;

    public DoctorsOfficeVo(Integer id, String description){
        this.id = id;
        this.description = description;
    }
}
