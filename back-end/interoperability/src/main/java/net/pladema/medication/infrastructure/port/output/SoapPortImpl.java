package net.pladema.medication.infrastructure.port.output;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import net.pladema.medication.application.port.SoapPort;

import net.pladema.medication.configuration.SoapRestTemplate;

import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;
import net.pladema.medication.domain.CommercialMedicationRequestEnvelope;

import net.pladema.medication.domain.CommercialMedicationRequestParameter;

import net.pladema.medication.domain.CommercialMedicationResponseEnvelope;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class SoapPortImpl implements SoapPort {

	@Value("${commercial-medication.api.id}")
	private Long ID;

	@Value("${commercial-medication.api.password}")
	private String PASSWORD;

	private final SoapRestTemplate restTemplate;

	private final Marshaller marshaller;

	private final Unmarshaller unmarshaller;

	public SoapPortImpl(SoapRestTemplate restTemplate) throws JAXBException {
		this.restTemplate = restTemplate;
		this.marshaller = initializeMarshaller();
		this.unmarshaller = initializeUnmarshaller();
	}

	private Unmarshaller initializeUnmarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CommercialMedicationResponseEnvelope.class, CommercialMedicationDecodedResponse.class);
		return jaxbContext.createUnmarshaller();
	}

	private Marshaller initializeMarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(CommercialMedicationRequestEnvelope.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(CharacterEscapeHandler.class.getName(), (CharacterEscapeHandler) (ch, start, length, isAttVal, writer) -> writer.write(ch, start, length));
		return marshaller;
	}

	@Override
	public CommercialMedicationDecodedResponse callAPI(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException  {
		String content = fetchResponseContent(parameters);
		InputStream stream = new ByteArrayInputStream(Objects.requireNonNull(content).getBytes(StandardCharsets.UTF_8));
		return (CommercialMedicationDecodedResponse) unmarshaller.unmarshal(stream);
	}

	private String fetchResponseContent(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException {
		byte[] decodedData = fetchContent(parameters);
		return handleZip(decodedData);
	}

	private String handleZip(byte[] decodedData) throws IOException {
		String content = null;
		ByteArrayInputStream bais = new ByteArrayInputStream(decodedData);
		ZipInputStream zis = new ZipInputStream(bais);
		ZipEntry entry = zis.getNextEntry();
		if (entry != null)
			content = unzip(zis);
		return content;
	}

	private String unzip(ZipInputStream zis) throws IOException {
		String content;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = zis.read(buffer)) > 0) baos.write(buffer, 0, length);
		content = baos.toString();
		zis.closeEntry();
		return content;
	}

	private byte[] fetchContent(CommercialMedicationRequestParameter parameters) throws JAXBException {
		ResponseEntity<String> response = makeRequest(parameters);
		InputStream stream = new ByteArrayInputStream(Objects.requireNonNull(response.getBody()).getBytes(StandardCharsets.UTF_8));
		String responseContent = ((CommercialMedicationResponseEnvelope) unmarshaller.unmarshal(stream)).getContent();
		return Base64.getDecoder().decode(responseContent);
	}

	private ResponseEntity<String> makeRequest(CommercialMedicationRequestParameter parameters) throws JAXBException {
		CommercialMedicationRequestEnvelope commercialMedicationRequestEnvelope = new CommercialMedicationRequestEnvelope(ID, PASSWORD, parameters.toXmlString());

		StringWriter xmlWriter = new StringWriter();
		marshaller.marshal(commercialMedicationRequestEnvelope, xmlWriter);

		String xmlString = xmlWriter.toString();

		HttpEntity<Object> request = new HttpEntity<>(xmlString, restTemplate.getHeaders());
		return restTemplate.exchange(SoapRestTemplate.BASE_URL, HttpMethod.POST, request, String.class);
	}

}
