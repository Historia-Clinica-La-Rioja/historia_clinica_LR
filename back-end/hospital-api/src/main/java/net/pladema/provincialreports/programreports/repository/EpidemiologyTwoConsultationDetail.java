package net.pladema.provincialreports.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EpidemiologyTwoConsultationDetail {

	private String diagnostic;

	private String grp;

	private String counter;

}
