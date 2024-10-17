package net.pladema.medication.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medication.domain.service.CDataAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlType(propOrder = {"commercialMedicationRequestHeader", "commercialMedicationRequestBody"})
public class CommercialMedicationRequestEnvelope {

	private CommercialMedicationRequestHeader commercialMedicationRequestHeader;

	private CommercialMedicationRequestBody commercialMedicationRequestBody;

	public CommercialMedicationRequestEnvelope(Long id, String password, String parameters) {
		commercialMedicationRequestBody = new CommercialMedicationRequestBody(id, password, parameters);
		commercialMedicationRequestHeader = new CommercialMedicationRequestHeader();
	}

	@XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	public CommercialMedicationRequestHeader getCommercialMedicationRequestHeader() {
		return commercialMedicationRequestHeader;
	}

	@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
	public CommercialMedicationRequestBody getCommercialMedicationRequestBody() {
		return commercialMedicationRequestBody;
	}

}

@XmlRootElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
class CommercialMedicationRequestHeader {
	// This is empty as per your XML structure
}

@Setter
@NoArgsConstructor
@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
class CommercialMedicationRequestBody {

	private CommercialMedicationRequestContent commercialMedicationRequestContent;

	public CommercialMedicationRequestBody(Long id, String password, String parameters) {
		commercialMedicationRequestContent = new CommercialMedicationRequestContent(id, password, parameters);
	}

	@XmlElement(name = "actualizar", namespace = "http://descargas.ws.webservice.alfabeta.net/")
	public CommercialMedicationRequestContent getCommercialMedicationRequestContent() {
		return commercialMedicationRequestContent;
	}

}

@Setter
@NoArgsConstructor
@XmlRootElement(name = "actualizar", namespace = "http://descargas.ws.webservice.alfabeta.net/")
class CommercialMedicationRequestContent {

	@XmlElement(name = "id")
	private Long id;

	@XmlElement(name = "clave")
	private String password;

	@XmlElement(name = "xml")
	@XmlJavaTypeAdapter(CDataAdapter.class)
	private String xml;

	public CommercialMedicationRequestContent(Long id, String password, String parameters) {
		this.id = id;
		this.password = password;
		this.xml = parameters;
	}

}

