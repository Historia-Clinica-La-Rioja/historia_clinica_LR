package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;

import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.application.port.CipresEncounterStorage;

import net.pladema.cipres.application.port.CipresStorage;
import net.pladema.cipres.domain.AnthropometricDataBo;
import net.pladema.cipres.domain.CipresEncounterBo;

import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.pladema.cipres.domain.RiskFactorBo;
import net.pladema.cipres.domain.SnomedBo;

import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresConsultationPayload;

import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresDatosClinicosPayload;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEntityResponse;

import net.pladema.cipres.infrastructure.output.rest.domain.CipresRegisterResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;

import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresSnomedPayload;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CipresEncounterStorageImpl extends CipresStorage implements CipresEncounterStorage {

	private static final String BLANK = "";

	public CipresEncounterStorageImpl(CipresRestTemplate cipresRestTemplate, CipresWSConfig cipresWSConfig) {
		super(cipresRestTemplate, cipresWSConfig);
	}

	@Override
	public String getClinicalSpecialtiyBySnomedCode(String snomedCode) {
		String url = String.join("?codigoSnomed=", cipresWSConfig.getClinicalSpecialtiesUrl(), snomedCode);
		ResponseEntity<CipresEntityResponse[]> response = null;
		try {
			response = restClient.exchangeGet(url, CipresEntityResponse[].class);
		} catch (RestTemplateApiException e) {
			log.error("Fallo en la comunicación al intentar obtener el identificador de especialidad", e);
		}
		if (response != null) {
			List<CipresEntityResponse> clinicalSpecialty = Arrays.asList(response.getBody());
			if (clinicalSpecialty.size() > 0)
				return clinicalSpecialty.get(0).getId();
		}
		return null;
	}

	@Override
	public String getEstablishmentByRefesCode(String refesCode) {
		String url = String.join("?codigoRefes=", cipresWSConfig.getDependenciesUrl(), refesCode);
		ResponseEntity<CipresEntityResponse[]> response = null;
		try {
			response = restClient.exchangeGet(url, CipresEntityResponse[].class);
		} catch (RestTemplateApiException e) {
			log.error("Fallo en la comunicación al intentar obtener el identificador de institución", e);
		}
		if (response != null) {
			List<CipresEntityResponse> establishment = Arrays.asList(response.getBody());
			if (establishment.size() > 0)
				return establishment.get(0).getId();
		}
		return null;
	}
	
	@Override
	public CipresEncounterBo createOutpatientConsultation(OutpatientConsultationBo consultation,
                                                          String clinicalSpecialtyIRI,
														  String establishmentIRI) {
		String url = cipresWSConfig.getConsultationUrl();
		ResponseEntity<CipresEntityResponse> response = null;
		CipresConsultationPayload body = mapToCipresConsultationPayload(consultation, clinicalSpecialtyIRI, establishmentIRI);
		try {
			response = restClient.exchangePost(url, body, CipresEntityResponse.class);
		} catch (RestTemplateApiException e) {
			log.error("Error al intentar crear la consulta", e);
			return mapCipresEncounterResponse(consultation.getId(), e.getStatusCode().value(), e.mapErrorBody(CipresRegisterResponse.class), null);
		} catch (ResourceAccessException e){
			log.error("Fallo en la comunicación", e);
			return mapCipresEncounterResponse(consultation.getId(), HttpStatus.SERVICE_UNAVAILABLE.value(), null, null);
		}
		return mapCipresEncounterResponse(consultation.getId(), response.getStatusCodeValue(), null, Integer.parseInt(response.getBody().getId()));
	}
	
	public CipresEncounterBo mapCipresEncounterResponse(Integer encounterId, Integer responseCode,
														CipresRegisterResponse response, Integer encounterApiId) {
		CipresEncounterBo cipresEncounter = new CipresEncounterBo();
		cipresEncounter.setEncounterId(encounterId);
		cipresEncounter.setResponseCode(responseCode.shortValue());
		cipresEncounter.setStatus(this.buildStatus(response));
		if (encounterApiId != null)
			cipresEncounter.setEncounterApiId(encounterApiId);
		return cipresEncounter;
	}

	public String buildStatus(CipresRegisterResponse response) {
		return response == null ? "Error=null" :
				new StringBuilder()
						.append("Error=")
						.append(response.getDetail())
						.toString();
	}

	private CipresConsultationPayload mapToCipresConsultationPayload(OutpatientConsultationBo oc,
																	String clinicalSpecialtyIRI,
																	String establishmentIRI) {
		return CipresConsultationPayload.builder()
				.idPaciente(oc.getApiPatientId())
				.fecha(LocalDate.parse(oc.getDate()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
				.especialidad(clinicalSpecialtyIRI)
				.motivoConsulta(CipresMasterData.CONSULTATION_REASON)
				.contactoMedicoPaciente(CipresMasterData.PATIENT_MEDICAL_CONTACT)
				.datosClinico(mapToSaludDatosClinicosPayloadList(oc.getAnthropometricData(), oc.getRiskFactor()))
				.diagnosticosSnomed(oc.getProblems().stream().map(this::mapToSaludSnomedPayload).collect(Collectors.toList()))
				.prestacionesSnomed(oc.getProcedures().stream().map(this::mapToSaludSnomedPayload).collect(Collectors.toList()))
				.establecimiento(establishmentIRI)
				.build();
	}

	private CipresSnomedPayload mapToSaludSnomedPayload(SnomedBo snomedBo) {
		return CipresSnomedPayload
				.builder()
				.nombre(snomedBo.getPt())
				.sctId(snomedBo.getSctid())
				.build();
	}
	
	private List<CipresDatosClinicosPayload> mapToSaludDatosClinicosPayloadList(AnthropometricDataBo anthropometricDataBo,
																				RiskFactorBo riskFactorBo) {
		List<CipresDatosClinicosPayload> result = new ArrayList<>();
		CipresDatosClinicosPayload clinicalData = new CipresDatosClinicosPayload();
		clinicalData.setPeso(anthropometricDataBo != null && anthropometricDataBo.getWeight() != null ? anthropometricDataBo.getWeight() : BLANK);
		clinicalData.setTalla(anthropometricDataBo != null && anthropometricDataBo.getHeight() != null ? anthropometricDataBo.getHeight() : BLANK);
		clinicalData.setPerimetroCefalico(anthropometricDataBo != null && anthropometricDataBo.getHeadCircumference() != null ? Integer.valueOf(anthropometricDataBo.getHeadCircumference()) : 0);
		clinicalData.setImc(anthropometricDataBo != null && anthropometricDataBo.getBmi() != null && anthropometricDataBo.getBmi().length() <= 6 ?  Float.parseFloat(anthropometricDataBo.getBmi().replace(",",".")) : 0);
		if (riskFactorBo != null && riskFactorBo.getDiastolicBloodPressure() != null && riskFactorBo.getSystolicBloodPressure() != null) {
			clinicalData.setTensionArterial(StringUtils.leftPad(riskFactorBo.getSystolicBloodPressure(), 3, "0") + "/" + StringUtils.leftPad(riskFactorBo.getDiastolicBloodPressure(), 3, "0"));
		}
		else
			clinicalData.setTensionArterial(BLANK);
		result.add(clinicalData);
		return result;
	}

}
