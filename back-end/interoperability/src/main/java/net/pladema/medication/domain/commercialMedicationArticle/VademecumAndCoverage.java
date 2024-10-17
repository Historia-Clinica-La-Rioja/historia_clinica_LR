package net.pladema.medication.domain.commercialMedicationArticle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cob")
public class VademecumAndCoverage {

	@XmlElement(name = "c1")
	private Float c1;

	@XmlElement(name = "c2")
	private Float c2;

	@XmlElement(name = "c3")
	private String c3;

	@XmlElement(name = "c4")
	private Float c4;

	@XmlElement(name = "c5")
	private String c5;
	
}
