package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DoctorInfoSummary implements Serializable {

	private static final long serialVersionUID = -6349210572359248285L;

    private String licenseNumber;

	private String firstName;

	private String lastName;

	private String identificationNumber;



}
