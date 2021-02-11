package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.PoliceInterventionDetailsVo;
import net.pladema.emergencycare.repository.entity.PoliceInterventionDetails;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PoliceInterventionDetailsBo {

    private Integer id;

    private LocalDate callDate;

    private LocalTime callTime;

    private String plateNumber;

    private String firstName;

    private String lastName;

    public PoliceInterventionDetailsBo(PoliceInterventionDetails policeInterventionDetails){
        this.id = policeInterventionDetails.getId();
        this.callDate = policeInterventionDetails.getCallDate();
        this.callTime = policeInterventionDetails.getCallTime();
        this.plateNumber = policeInterventionDetails.getPlateNumber();
        this.firstName = policeInterventionDetails.getFirstname();
        this.lastName = policeInterventionDetails.getLastname();
    }

    public PoliceInterventionDetailsBo(PoliceInterventionDetailsVo policeIntervention){
        this.id = policeIntervention.getId();
        this.callDate = policeIntervention.getCallDate();
        this.callTime = policeIntervention.getCallTime();
        this.plateNumber = policeIntervention.getPlateNumber();
        this.firstName = policeIntervention.getFirstName();
        this.lastName = policeIntervention.getLastName();
    }

}
