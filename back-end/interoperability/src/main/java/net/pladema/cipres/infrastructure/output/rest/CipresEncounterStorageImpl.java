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

import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresConsultationPayload;

import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresDatosClinicosPayload;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEntityResponse;

import net.pladema.cipres.infrastructure.output.rest.domain.CipresRegisterResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;

import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEstablishmentResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresSnomedPayload;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CipresEncounterStorageImpl extends CipresStorage implements CipresEncounterStorage {

	private static final String BLANK = "";

	private static final String NOT_FOUND_CLINICAL_SPECIALTY_ID = "No se encontró identificador para la especialidad clinica con snomed sctid: ";

	private static final String NOT_FOUND_ESTABLISHMENT_ID = "No se encontró identificador de establecimiento con código sisa: ";

	public CipresEncounterStorageImpl(CipresRestTemplate cipresRestTemplate, CipresWSConfig cipresWSConfig,
									  CipresEncounterRepository cipresEncounterRepository) {
		super(cipresRestTemplate, cipresWSConfig, cipresEncounterRepository);
	}

	@Override
	public Optional<String> getClinicalSpecialtyBySnomedCode(String snomedCode, Integer encounterId, Integer cipresEncounterId) {
		String url = String.join("?codigoSnomed=", cipresWSConfig.getClinicalSpecialtiesUrl(), snomedCode);
		ResponseEntity<CipresEntityResponse[]> response = null;
		try {
			response = restClient.exchangeGet(url, CipresEntityResponse[].class);
			if (isSuccessfulResponse(response))
				return Optional.of(response.getBody()[0].getId());
			this.saveStatusError(cipresEncounterId, encounterId, NOT_FOUND_CLINICAL_SPECIALTY_ID + snomedCode, HttpStatus.BAD_REQUEST.value());
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener el identificador de especialidad", e);
			this.saveStatusError(cipresEncounterId, encounterId,  e.mapErrorBody(CipresRegisterResponse.class).getDetail(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			this.handleResourceAccessException(e, encounterId, cipresEncounterId);
		}
		return Optional.empty();
	}

	@Override
	public Optional<CipresEstablishmentResponse> getEstablishmentBySisaCode(String sisaCode, Integer encounterId, Integer cipresEncounterId) {
		String url = String.join( "?codigoRefes=", cipresWSConfig.getDependenciesUrl(), sisaCode);
		ResponseEntity<CipresEstablishmentResponse[]> response = null;
		try {
			response = restClient.exchangeGet(url, CipresEstablishmentResponse[].class);
			if (isSuccessfulResponse(response))
				return Optional.of(response.getBody()[0]);
			saveStatusError(cipresEncounterId, encounterId, NOT_FOUND_ESTABLISHMENT_ID + sisaCode, HttpStatus.BAD_REQUEST.value());
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener el identificador del establecimiento", e);
			saveStatusError(cipresEncounterId, encounterId,  e.mapErrorBody(CipresRegisterResponse.class).getDetail(), e.getStatusCode().value());
		} catch (ResourceAccessException e) {
			handleResourceAccessException(e, encounterId, cipresEncounterId);
		}
		return Optional.empty();
	}
	
	@Override
	public CipresEncounterBo makeAndSendOutpatientConsultation(OutpatientConsultationBo consultation,
														       String clinicalSpecialtyIRI,
														       String establishmentIRI) {
		String url = cipresWSConfig.getConsultationUrl();
		ResponseEntity<CipresEntityResponse> response = null;
		CipresConsultationPayload body = mapToCipresConsultationPayload(consultation, clinicalSpecialtyIRI, establishmentIRI);
		try {
			response = restClient.exchangePost(url, body, CipresEntityResponse.class);
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar crear la consulta con id: ", consultation.getId());
			return new CipresEncounterBo(consultation.getId(),e.mapErrorBody(CipresRegisterResponse.class).getDetail() + " - " + body.toString(), (short) e.getStatusCode().value());
		} catch (ResourceAccessException e){
			log.debug("Fallo en la comunicación - API SALUD");
			return new CipresEncounterBo(consultation.getId(), e.getMessage(), (short) HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return new CipresEncounterBo(consultation.getId(), Integer.parseInt(response.getBody().getId()), " ", (short) response.getStatusCode().value());
	}

	private CipresConsultationPayload mapToCipresConsultationPayload(OutpatientConsultationBo oc,
																	 String clinicalSpecialtyIRI,
																	 String establishmentIRI) {
		return CipresConsultationPayload.builder()
				.idPaciente(oc.getApiPatientId())
				.fecha(LocalDateTime.parse(oc.getDate()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
				.especialidad(clinicalSpecialtyIRI)
				.motivoConsulta(CipresMasterData.CONSULTATION_REASON)
				.contactoMedicoPaciente(CipresMasterData.PATIENT_MEDICAL_CONTACT)
				.datosClinico(mapToSaludDatosClinicosPayloadList(oc.getAnthropometricData(), oc.getRiskFactor()))
				.diagnosticosSnomed(oc.getProblems().stream().map(this::mapToSaludSnomedPayload).collect(Collectors.toList()))
				.prestacionesSnomed(oc.getProcedures().stream().map(this::mapToSaludSnomedPayload).collect(Collectors.toList()))
				.medicacionsSnomed(oc.getMedications().stream().map(this::mapToSaludSnomedPayload).collect(Collectors.toList()))
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
		clinicalData.setPerimetroCefalico(anthropometricDataBo != null && anthropometricDataBo.getHeadCircumference() != null ? Math.round(Float.parseFloat(anthropometricDataBo.getHeadCircumference())) : 0);
		clinicalData.setImc(anthropometricDataBo != null && anthropometricDataBo.getBmi() != null && anthropometricDataBo.getBmi().length() <= 6 ?  Float.parseFloat(anthropometricDataBo.getBmi().replace(",",".")) : 0);
		clinicalData.setTensionArterial(parseTensionArterial(riskFactorBo));
		result.add(clinicalData);
		return result;
	}

	private String parseTensionArterial(RiskFactorBo riskFactorBo) {
		if (riskFactorBo != null && riskFactorBo.getDiastolicBloodPressure() != null && riskFactorBo.getSystolicBloodPressure() != null)
			return StringUtils.leftPad(riskFactorBo.getSystolicBloodPressure(), 3, "0") + "/" + StringUtils.leftPad(riskFactorBo.getDiastolicBloodPressure(), 3, "0");
		return BLANK;
	}

}
