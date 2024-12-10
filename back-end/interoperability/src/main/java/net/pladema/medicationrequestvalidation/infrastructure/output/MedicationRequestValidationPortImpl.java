package net.pladema.medicationrequestvalidation.infrastructure.output;

import ar.lamansys.sgh.shared.domain.medicationrequestvalidation.*;
import ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation.EMedicationRequestValidationException;
import ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation.MedicationRequestValidationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicationrequestvalidation.application.port.output.MedicationRequestValidationPort;

import net.pladema.medicationrequestvalidation.infrastructure.output.config.MedicationRequestValidationRestClient;
import net.pladema.medicationrequestvalidation.infrastructure.output.config.MedicationRequestValidationWSConfig;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MedicationRequestValidationPortImpl implements MedicationRequestValidationPort {

	private final MedicationRequestValidationRestClient restClient;

	private final MedicationRequestValidationWSConfig medicationRequestValidationWSConfig;

	@Override
	public List<String> validateMedicationRequest(MedicationRequestValidationDispatcherSenderBo request) {
		try {
			ResponseEntity<Map> requestResult = restClient.exchangePost(MedicationRequestValidationWSConfig.VALIDATE_PATH, request.parseToMap(medicationRequestValidationWSConfig.getClientId()), Map.class);
			List<Map<String, String>> validatedPrescriptions = (List<Map<String, String>>) requestResult.getBody().get("recetas");
			log.info("Request response -> {}", validatedPrescriptions);
			return validatedPrescriptions.stream()
					.map(validatedPrescription -> validatedPrescription.get("idReceta"))
					.collect(Collectors.toList());
		}
		catch (HttpClientErrorException e) {
			log.warn("Error: {}", e.getMessage());
			JsonObject error = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
			String message = String.format("Ha habido un error en el validador de la receta digital. Código %s: %s", error.get("error"), error.get("mensaje"));
			throw new MedicationRequestValidationException(message, EMedicationRequestValidationException.EXTERNAL_ERROR);
		}
	}

}
