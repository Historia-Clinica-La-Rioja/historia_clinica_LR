package net.pladema.medication.domain.commercialMedicationArticle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mdroga")
public class NewDrug {

	@XmlElement(name = "id")
	private Integer id;

	@XmlElement(name = "po")
	private BigDecimal potency;

	@XmlElement(name = "upo")
	private Integer potencyUnityId;
	
}
