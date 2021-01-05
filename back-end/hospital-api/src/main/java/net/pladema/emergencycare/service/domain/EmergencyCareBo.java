package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareBo {

    private Integer id;

    private String firstname;

    private String lastname;

    private Integer patientId;

    private Short triageCategoryId;

    private EEmergencyCareType emergencyCareType;

    private EEmergencyCareState emergencyCareState;

    private Integer doctorsOffice;

    private String doctorsOfficeDescription;

    private LocalDateTime createdOn;

    public EmergencyCareBo(EmergencyCareVo emergencyCareVo){
        this.id = emergencyCareVo.getId();
        this.firstname = emergencyCareVo.getFirstname();
        this.lastname = emergencyCareVo.getLastname();
        this.patientId = emergencyCareVo.getPatientId();
        this.triageCategoryId = emergencyCareVo.getTriageCategoryId();
        this.emergencyCareType = EEmergencyCareType.getById(emergencyCareVo.getEmergencyCareType());
        this.emergencyCareState = EEmergencyCareState.getById(emergencyCareVo.getEmergencyCareState());
        this.doctorsOffice = emergencyCareVo.getDoctorsOffice();
        this.doctorsOfficeDescription = emergencyCareVo.getDoctorsOfficeDescription();
        this.createdOn = emergencyCareVo.getCreatedOn();
    }
}
