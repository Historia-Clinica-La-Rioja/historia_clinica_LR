package net.pladema.provincialreports.odontologicalreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OdontologyConsultationDetail {

	private String professional;

	private String procedures;

	private String counter;

}
