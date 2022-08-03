package net.pladema.medicalconsultation.appointment.repository.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingPersonBo {
	private LocalDate birthDate;
	private String email;
	private String firstName;
	private Short genderId;
	private String idNumber;
	private String lastName;

}
