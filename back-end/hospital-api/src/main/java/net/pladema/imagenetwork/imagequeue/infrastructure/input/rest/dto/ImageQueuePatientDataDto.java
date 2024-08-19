package net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ImageQueuePatientDataDto {

    private Integer patientId;

    private Short patientTypeId;

    private Integer personId;

    private String identificationType;

    private String identificationNumber;

    private String firstName;

    private String middleNames;

    private String nameSelfDetermination;

    private String lastName;

    private String otherLastNames;

    private GenderDto gender;

    private String selfPerceivedGender;

    private PersonAgeDto personAgeDto;

    private LocalDate birthDate;


}
