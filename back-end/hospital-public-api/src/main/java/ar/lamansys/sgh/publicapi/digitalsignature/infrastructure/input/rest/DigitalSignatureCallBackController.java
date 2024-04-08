package ar.lamansys.sgh.publicapi.digitalsignature.infrastructure.input.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception.DigitalSignatureCallbackException;
import ar.lamansys.sgh.publicapi.digitalsignature.application.updatestatusdigitalsignature.UpdateStatusDigitalSignature;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.DigitalSignatureCallbackRequestDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.digitalsignature.MultipleDigitalSignatureCallbackRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@Tag(name = "PublicApi Firma Digital", description = "Integración con Firma Digital")
@RequestMapping("/public-api/digital-signature/callback")
public class DigitalSignatureCallBackController {

	private final UpdateStatusDigitalSignature updateStatusDigitalSignature;

	@PostMapping("/status")
	public ResponseEntity<Boolean> updateStatus(@RequestBody JsonNode data) throws JsonProcessingException, DigitalSignatureCallbackException {
		log.debug("Callback parameters -> data {}", data);
		MultipleDigitalSignatureCallbackRequestDto requestDto = mapToDto(data);
		updateStatusDigitalSignature.run(requestDto);
		return ResponseEntity.ok().body(Boolean.TRUE);
	}

	private MultipleDigitalSignatureCallbackRequestDto mapToDto(JsonNode data) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return (data.get("documentos") != null)
				? objectMapper.treeToValue(data, MultipleDigitalSignatureCallbackRequestDto.class)
				: new MultipleDigitalSignatureCallbackRequestDto(List.of(objectMapper.treeToValue(data, DigitalSignatureCallbackRequestDto.class)));
	}
}
