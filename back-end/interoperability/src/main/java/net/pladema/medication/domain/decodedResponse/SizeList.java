package net.pladema.medication.domain.decodedResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medication.domain.CommercialMedicationMasterData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Tamanio")
public class SizeList {

	@XmlElement(name = "registro")
	private List<CommercialMedicationMasterData> sizes;

}
