package commercialmedication.cache.domain;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Setter
@NoArgsConstructor
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class CommercialMedicationResponseEnvelope {

	private CommercialMedicationResponseBody commercialMedicationResponseBody;

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	public CommercialMedicationResponseBody getCommercialMedicationResponseBody() {
		return commercialMedicationResponseBody;
	}

	public String getContent() {
		return commercialMedicationResponseBody.getCommercialMedicationResponseContent().getReturnValue();
	}

}

@Setter
@NoArgsConstructor
@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
class CommercialMedicationResponseBody {

	private CommercialMedicationResponseContent commercialMedicationResponseContent;

	@XmlElement(name = "actualizarResponse", namespace = "http://descargas.ws.webservice.alfabeta.net/")
	public CommercialMedicationResponseContent getCommercialMedicationResponseContent() {
		return commercialMedicationResponseContent;
	}

}

@Setter
@NoArgsConstructor
class CommercialMedicationResponseContent {

	private String returnValue;

	@XmlElement(name = "return")
	public String getReturnValue() {
		return returnValue;
	}

}

