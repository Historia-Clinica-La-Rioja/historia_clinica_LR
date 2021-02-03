package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.controller.dto.DateDto;
import net.pladema.sgx.dates.controller.dto.TimeDto;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PoliceInterventionDto implements Serializable {

    private DateDto callDate;

    private TimeDto callTime;

    private String plateNumber;

    private String firstName;

    private String lastName;

}
