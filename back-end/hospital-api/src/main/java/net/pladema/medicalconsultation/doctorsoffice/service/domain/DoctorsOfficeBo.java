package net.pladema.medicalconsultation.doctorsoffice.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DoctorsOfficeBo {

    private Integer id;

    private String description;

    private LocalTime openingTime;

    private LocalTime closingTime;

    public DoctorsOfficeBo(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public DoctorsOfficeBo(DoctorsOfficeVo doctorsOfficeVo) {
        this.id = doctorsOfficeVo.getId();
        this.description = doctorsOfficeVo.getDescription();
        this.openingTime = doctorsOfficeVo.getOpeningTime();
        this.closingTime = doctorsOfficeVo.getClosingTime();
    }

    public DoctorsOfficeBo(DoctorsOffice doctorsOffice) {
        this.id = doctorsOffice.getId();
        this.description = doctorsOffice.getDescription();
        this.openingTime = doctorsOffice.getOpeningTime();
        this.closingTime = doctorsOffice.getClosingTime();
    }
}
