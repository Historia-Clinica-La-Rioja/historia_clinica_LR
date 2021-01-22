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

    private LocalDate dateCall;

    private LocalTime timeCall;

    private String plateNumber;

    private String firstName;

    private String lastName;

    public PoliceInterventionBo(PoliceIntervention policeIntervention){
        this.id = policeIntervention.getId();
        this.dateCall = policeIntervention.getCallDate();
        this.timeCall = policeIntervention.getCallTime();
        this.plateNumber = policeIntervention.getPlateNumber();
        this.firstName = policeIntervention.getFirstname();
        this.lastName = policeIntervention.getLastname();
    }

    public PoliceInterventionBo(PoliceInterventionVo policeIntervention){
        this.id = policeIntervention.getId();
        this.dateCall = policeIntervention.getDateCall();
        this.timeCall = policeIntervention.getTimeCall();
        this.plateNumber = policeIntervention.getPlateNumber();
        this.firstName = policeIntervention.getFirstName();
        this.lastName = policeIntervention.getLastName();
    }

}
