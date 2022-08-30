package net.pladema.medicalconsultation.appointment.repository.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class BookingPersonBo {
	private LocalDate birthDate;
	private String email;
	private String firstName;
	private Short genderId;
	private String idNumber;
	private String lastName;
}
