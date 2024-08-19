package net.pladema.imagenetwork.imagequeue.domain;

import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PersonAgeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import java.time.LocalDate;


@Value
@Builder
@ToString
@AllArgsConstructor
public class ImageQueuePatientBo {

    Integer patientId;

    Short patientTypeId;

    Integer personId;

    String identificationType;

    String identificationNumber;

    String firstName;

    String middleNames;

    String nameSelfDetermination;

    String lastName;

    String otherLastNames;

    GenderDto gender;

    String selfPerceivedGender;

    PersonAgeDto personAgeDto;

    LocalDate birthDate;

}
