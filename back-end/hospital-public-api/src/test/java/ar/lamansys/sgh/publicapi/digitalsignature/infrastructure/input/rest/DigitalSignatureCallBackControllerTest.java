package ar.lamansys.sgh.publicapi.digitalsignature.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;
import ar.lamansys.sgh.publicapi.digitalsignature.application.updatestatusdigitalsignature.UpdateStatusDigitalSignature;

import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureStatusDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.MultipleDigitalSignatureCallbackRequestDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DigitalSignatureCallBackControllerTest {

	@Mock
	private UpdateStatusDigitalSignature updateStatusDigitalSignature;

	private ObjectMapper objectMapper = new ObjectMapper();

	private DigitalSignatureCallBackController digitalSignatureCallBackController;

	@BeforeEach
	public void setUp() {
		digitalSignatureCallBackController = new DigitalSignatureCallBackController(updateStatusDigitalSignature);
	}

	@Test
	void testUpdateStatus_SingleDocument() throws JsonProcessingException, DigitalSignatureCallbackException {
		String jsonString = "{\n" +
				"  \"documentos\": [\n" +
				"    {\n" +
				"      \"status\": {\n" +
				"        \"success\": true,\n" +
				"        \"msg\": \"Successful\"\n" +
				"      },\n" +
				"      \"metadata\": {\n" +
				"        \"documentId\": \"1\",\n" +
				"        \"personId\": \"123\"\n" +
				"      },\n" +
				"      \"hash\": \"hashValue\",\n" +
				"      \"documento\": \"signatureHashValue\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";
		JsonNode jsonNode = objectMapper.readTree(jsonString);

		DigitalSignatureStatusDto statusDto = new DigitalSignatureStatusDto(true, "Successful");

		DigitalSignatureCallbackRequestDto callbackRequest = new DigitalSignatureCallbackRequestDto(
				statusDto,
				Map.of("documentId", "1", "personId", "123"),
				"hashValue",
				"signatureHashValue"
		);

		MultipleDigitalSignatureCallbackRequestDto requestDto = new MultipleDigitalSignatureCallbackRequestDto(List.of(callbackRequest));

		ResponseEntity<Boolean> response = digitalSignatureCallBackController.updateStatus(jsonNode);

		ArgumentCaptor<MultipleDigitalSignatureCallbackRequestDto> captor =
				ArgumentCaptor.forClass(MultipleDigitalSignatureCallbackRequestDto.class);

		verify(updateStatusDigitalSignature).run(captor.capture());

		List<DigitalSignatureCallbackRequestDto> capturedDocuments = captor.getValue().getDocuments();
		assertEquals(requestDto.getDocuments().size(), capturedDocuments.size());

		for (int i = 0; i < capturedDocuments.size(); i++) {
			assertEquals(requestDto.getDocuments().get(i).getHash(), capturedDocuments.get(i).getHash());
			assertEquals(requestDto.getDocuments().get(i).getStatus().getSuccess(), capturedDocuments.get(i).getStatus().getSuccess());
			assertEquals(requestDto.getDocuments().get(i).getStatus().getMsg(), capturedDocuments.get(i).getStatus().getMsg());
			assertEquals(requestDto.getDocuments().get(i).getMetadata(), capturedDocuments.get(i).getMetadata());
			assertEquals(requestDto.getDocuments().get(i).getSignatureHash(), capturedDocuments.get(i).getSignatureHash());
		}

		assertEquals(ResponseEntity.ok().body(Boolean.TRUE), response);

	}

	@Test
	void testUpdateStatus_MultipleDocuments() throws JsonProcessingException, DigitalSignatureCallbackException {
		String jsonString = "{\n" +
				"  \"documentos\": [\n" +
				"    {\n" +
				"      \"status\": {\n" +
				"        \"success\": true,\n" +
				"        \"msg\": \"Successful\"\n" +
				"      },\n" +
				"      \"metadata\": {\n" +
				"        \"documentId\": \"1\",\n" +
				"        \"personId\": \"123\"\n" +
				"      },\n" +
				"      \"hash\": \"hashValue1\",\n" +
				"      \"documento\": \"signatureHashValue1\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"status\": {\n" +
				"        \"success\": false,\n" +
				"        \"msg\": \"Failed\"\n" +
				"      },\n" +
				"      \"metadata\": {\n" +
				"        \"documentId\": \"2\",\n" +
				"        \"personId\": \"456\"\n" +
				"      },\n" +
				"      \"hash\": \"hashValue2\",\n" +
				"      \"documento\": \"signatureHashValue2\"\n" +
				"    }\n" +
				"  ]\n" +
				"}";

		JsonNode jsonNode = objectMapper.readTree(jsonString);

		DigitalSignatureStatusDto statusDto1 = new DigitalSignatureStatusDto(true, "Successful");
		DigitalSignatureCallbackRequestDto callbackRequest1 = new DigitalSignatureCallbackRequestDto(
				statusDto1,
				Map.of("documentId", "1", "personId", "123"),
				"hashValue1",
				"signatureHashValue1"
		);

		DigitalSignatureStatusDto statusDto2 = new DigitalSignatureStatusDto(false, "Failed");
		DigitalSignatureCallbackRequestDto callbackRequest2 = new DigitalSignatureCallbackRequestDto(
				statusDto2,
				Map.of("documentId", "2", "personId", "456"),
				"hashValue2",
				"signatureHashValue2"
		);

		MultipleDigitalSignatureCallbackRequestDto requestDto = new MultipleDigitalSignatureCallbackRequestDto(List.of(callbackRequest1, callbackRequest2));

		ResponseEntity<Boolean> response = digitalSignatureCallBackController.updateStatus(jsonNode);

		ArgumentCaptor<MultipleDigitalSignatureCallbackRequestDto> captor =
				ArgumentCaptor.forClass(MultipleDigitalSignatureCallbackRequestDto.class);

		verify(updateStatusDigitalSignature).run(captor.capture());

		List<DigitalSignatureCallbackRequestDto> capturedDocuments = captor.getValue().getDocuments();
		assertEquals(requestDto.getDocuments().size(), capturedDocuments.size());

		for (int i = 0; i < capturedDocuments.size(); i++) {
			assertEquals(requestDto.getDocuments().get(i).getHash(), capturedDocuments.get(i).getHash());
			assertEquals(requestDto.getDocuments().get(i).getStatus().getSuccess(), capturedDocuments.get(i).getStatus().getSuccess());
			assertEquals(requestDto.getDocuments().get(i).getStatus().getMsg(), capturedDocuments.get(i).getStatus().getMsg());
			assertEquals(requestDto.getDocuments().get(i).getMetadata(), capturedDocuments.get(i).getMetadata());
			assertEquals(requestDto.getDocuments().get(i).getSignatureHash(), capturedDocuments.get(i).getSignatureHash());
		}

		assertEquals(ResponseEntity.ok().body(Boolean.TRUE), response);
	}


}
