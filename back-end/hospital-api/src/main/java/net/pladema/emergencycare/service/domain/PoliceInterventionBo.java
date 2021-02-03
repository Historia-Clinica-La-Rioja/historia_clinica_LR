package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.PoliceInterventionVo;
import net.pladema.emergencycare.repository.entity.PoliceIntervention;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PoliceInterventionBo {

    private Integer id;

    private LocalDate callDate;

    private LocalTime callTime;

    private String plateNumber;

    private String firstName;

    private String lastName;

    public PoliceInterventionBo(PoliceIntervention policeIntervention){
        this.id = policeIntervention.getId();
        this.callDate = policeIntervention.getCallDate();
        this.callTime = policeIntervention.getCallTime();
        this.plateNumber = policeIntervention.getPlateNumber();
        this.firstName = policeIntervention.getFirstname();
        this.lastName = policeIntervention.getLastname();
    }

    public PoliceInterventionBo(PoliceInterventionVo policeIntervention){
        this.id = policeIntervention.getId();
        this.callDate = policeIntervention.getCallDate();
        this.callTime = policeIntervention.getCallTime();
        this.plateNumber = policeIntervention.getPlateNumber();
        this.firstName = policeIntervention.getFirstName();
        this.lastName = policeIntervention.getLastName();
    }

}
