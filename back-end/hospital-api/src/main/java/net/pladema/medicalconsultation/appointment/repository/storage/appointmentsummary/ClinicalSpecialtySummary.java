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
public class ClinicalSpecialtySummary implements Serializable {

	private static final long serialVersionUID = -6349210572359248285L;

    private String sctid;

	private String name;

}
