package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.entity.BookingPerson;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingPersonBo {
	private String birthDate;
	private String email;
	private String firstName;
	private Short genderId;
	private String idNumber;
	private String lastName;

	public BookingPersonBo(BookingPerson bookingPerson) {
		this.birthDate = bookingPerson.getBirthDate().toString();
		this.email = bookingPerson.getEmail();
		this.firstName = bookingPerson.getFirstName();
		this.genderId = bookingPerson.getGenderId();
		this.idNumber = bookingPerson.getIdentificationNumber();
		this.lastName = bookingPerson.getLastName();
	}
}
