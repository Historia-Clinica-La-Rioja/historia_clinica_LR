package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PatientBo implements Serializable {

    private Integer id;

    private String firstName;

    private String lastName;

    private String nameSelfDetermination;

	private Short identificationTypeId;

	private String identificationNumber;

	private LocalDate birthDate;

}
