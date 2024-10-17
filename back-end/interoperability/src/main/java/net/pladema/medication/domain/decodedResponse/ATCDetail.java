package net.pladema.medication.domain.decodedResponse;

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
@XmlRootElement(name = "atcd")
public class ATCDetail {

	@XmlElement(name = "atc")
	private String code;

	@XmlElement(name = "d")
	private String description;

	@XmlElement(name = "c")
	private String comment;

	@XmlElement(name = "ddd")
	private Float dailyDose;

	@XmlElement(name = "tu")
	private String unit;

	@XmlElement(name = "ca")
	private String via;

	@XmlElement(name = "dddc")
	private String dddComment;

	@XmlElement(name = "nivel")
	private String level;

}
