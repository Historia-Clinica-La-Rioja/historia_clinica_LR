package commercial_medication.update_schema.cache.domain.decodedResponse;

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
@XmlRootElement(name = "respuesta")
public class CommercialMedicationDecodedResponse {

	@XmlElement(name = "error")
	private ErrorCode errorCode;

	@XmlElement(name = "basecompleta")
	private CommercialMedicationCompleteDatabase commercialMedicationCompleteDatabase;

	//Hay que revisar ésto
	@XmlElement(name = "logsMF")
	private CommercialMedicationDatabaseUpdate commercialMedicationDatabaseUpdate;

	@XmlElement(name = "atcdetalle")
	private ATCDetailList atcDetailList;

	private String fileName;

}

