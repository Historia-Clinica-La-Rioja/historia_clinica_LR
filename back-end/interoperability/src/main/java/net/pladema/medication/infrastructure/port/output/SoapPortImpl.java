package net.pladema.medication.infrastructure.port.output;

import ar.lamansys.sgx.shared.files.FileService;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import net.pladema.medication.application.port.SoapPort;

import net.pladema.medication.configuration.SoapRestTemplate;

import net.pladema.medication.domain.CommercialMedicationResponse;
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

	@Value("${mateo.value.a}")
	private Long ID;

	@Value("${mateo.value.b}")
	private String PASSWORD;

	private final SoapRestTemplate restTemplate;

	private final Marshaller marshaller;

	private final Unmarshaller unmarshaller;

	private final FileService fileService;

	public SoapPortImpl(SoapRestTemplate restTemplate, FileService fileService) throws JAXBException {
		this.restTemplate = restTemplate;
		this.marshaller = initializeMarshaller();
		this.unmarshaller = initializeUnmarshaller();
		this.fileService = fileService;
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
	public CommercialMedicationDecodedResponse callAPIWithNoFile(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException  {
		String content = fetchResponseContent(parameters);
		InputStream stream = new ByteArrayInputStream(Objects.requireNonNull(content).getBytes(StandardCharsets.UTF_8));
		return (CommercialMedicationDecodedResponse) unmarshaller.unmarshal(stream);
	}

	private String fetchResponseContent(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException {
		byte[] decodedData = fetchContent(parameters);
		return handleZip(decodedData);
	}

	@Override
	public CommercialMedicationResponse callAPIWithFile(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException {
		String content = fetchResponseContent(parameters);
		InputStream stream = new ByteArrayInputStream(Objects.requireNonNull(content).getBytes(StandardCharsets.UTF_8));
		CommercialMedicationResponse result = new CommercialMedicationResponse();
		result.setCommercialMedicationDecodedResponse((CommercialMedicationDecodedResponse) unmarshaller.unmarshal(stream));
		if (parameters.isGeneratesFile())
			result.setFilePath(generateFileFromContent(content));
		return result;
	}

	@Override
	public CommercialMedicationDecodedResponse unmarshallCommercialMedicationDecodedResponseXml(InputStream fileContent) throws JAXBException {
		return (CommercialMedicationDecodedResponse) unmarshaller.unmarshal(fileContent);
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

	private String generateFileFromContent(String content) {
		String uuid = fileService.createUuid();
		String path = String.format("commercial-medication/schema-update/%s.txt", uuid);
		FilePathBo filePath = fileService.buildCompletePath(path);
		FileContentBo fileContent = FileContentBo.fromString(content);
		fileService.saveStreamInPath(filePath, uuid, "COMMERCIAL-MEDICATION-UPDATE", true, fileContent);
		return path;
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
