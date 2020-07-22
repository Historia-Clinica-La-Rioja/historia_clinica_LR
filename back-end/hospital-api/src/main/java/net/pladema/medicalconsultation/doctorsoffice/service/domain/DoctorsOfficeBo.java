package net.pladema.medicalconsultation.doctorsoffice.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;

import java.time.LocalTime;

@Getter
@Setter
@ToString
public class DoctorsOfficeBo {

    private Integer id;

    private String description;

    private LocalTime openingTime;

    private LocalTime closingTime;

    public DoctorsOfficeBo(DoctorsOfficeVo doctorsOfficeVo) {
        this.id = doctorsOfficeVo.getId();
        this.description = doctorsOfficeVo.getDescription();
        this.openingTime = doctorsOfficeVo.getOpeningTime();
        this.closingTime = doctorsOfficeVo.getClosingTime();
    }
}
