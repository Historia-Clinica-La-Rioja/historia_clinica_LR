package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresPatientStorage;
import net.pladema.cipres.application.port.CipresStorage;
import net.pladema.cipres.domain.BasicDataPersonBo;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientResponse;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresPatientPayload;


import net.pladema.cipres.infrastructure.output.rest.domain.consultation.CipresEntityResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CipresPatientStorageImpl extends CipresStorage implements CipresPatientStorage {

	public static final short GENDER_FEMALE = 1;

	public static final short GENDER_MALE = 2;

	public static final short GENDER_NONBINARY = 3;

	public static final short IDENTIFICATION_TYPE_DNI = 1;

	public CipresPatientStorageImpl (CipresRestTemplate cipresRestTemplate, CipresWSConfig cipresWSConfig) {
		super(cipresRestTemplate, cipresWSConfig);
	}

	@Override
	public Integer getPatientId(BasicDataPersonBo basicDataPersonBo) {
		ResponseEntity<CipresPatientResponse[]> response = null;
		String url = this.buildPatientUrl(basicDataPersonBo);
		try {
			response = restClient.exchangeGet(url, CipresPatientResponse[].class);
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar obtener un paciente en la api", e);
		} catch (ResourceAccessException e) {
			log.error("Fallo en la comunicación", e);
			return null;
		}
		if (response != null) {
			List<CipresPatientResponse> cipresPatientResponseList = Arrays.asList(response.getBody());
			if (cipresPatientResponseList.size() == 0 && isValidIdentificationTypeToCipres(basicDataPersonBo.getIdentificationTypeId())) {
				return Integer.parseInt(this.createPatient(basicDataPersonBo));
			}
			return  (Integer) cipresPatientResponseList.get(0).getPersona().get("id");
		}
		return null;
	}

	private boolean isValidIdentificationTypeToCipres(Short identificationTypeId) {
		return identificationTypeId == null ||
				identificationTypeId.equals(IDENTIFICATION_TYPE_DNI);
	}

	private String createPatient(BasicDataPersonBo basicDataPersonBo) {
		String url = cipresWSConfig.getPatientUrl();
		CipresPatientPayload body = mapToPatientPayload(basicDataPersonBo);
		ResponseEntity<CipresEntityResponse> response = null;
		try {
			response = restClient.exchangePost(url, body, CipresEntityResponse.class);
		} catch (RestTemplateApiException e) {
			log.debug("Error al intentar insertar un paciente en la api", e);
			return null;
		}
		if (response != null)
			return response.getBody().getId();
		return null;
	}

	public CipresPatientPayload mapToPatientPayload(BasicDataPersonBo basicDataPersonBo) {
		String firstName = Stream.of(basicDataPersonBo.getFirstName(),  basicDataPersonBo.getMiddleNames())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
		String lastName = Stream.of(basicDataPersonBo.getOtherLastNames(), basicDataPersonBo.getLastName())
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
		return CipresPatientPayload.builder()
				.nombre(firstName)
				.apellido(lastName)
				.tipoDocumento(CipresMasterData.DNI_DOCUMENT_TYPE)
				.numeroDocumento(basicDataPersonBo.getIdentificationNumber())
				.sexo(getCipresGenderMasterData(basicDataPersonBo.getGenderId()))
				.fechaNacimiento(basicDataPersonBo.getBirthDate().toString())
				.build();
	}

	private String getCipresGenderMasterData(Short genderId) {
		switch (genderId) {
			case GENDER_FEMALE:
				return CipresMasterData.GENDER_FEMALE;
			case GENDER_MALE:
				return CipresMasterData.GENDER_MALE;
			case GENDER_NONBINARY:
				return CipresMasterData.GENDER_NONBINARY;
			default:
				return CipresMasterData.GENDER_NO_DATA;
		}
	}

	private String getCipresGenderInitial(Short genderId) {
		switch (genderId) {
			case GENDER_FEMALE:
				return CipresMasterData.GENDER_FEMALE_INITIAL;
			case GENDER_MALE:
				return CipresMasterData.GENDER_MALE_INITIAL;
			default:
				return CipresMasterData.GENDER_NONBINARY_INITIAL;
		}
	}

	private String buildPatientUrl(BasicDataPersonBo basicDataPersonBo) {
		StringBuilder url = new StringBuilder();
		url.append(cipresWSConfig.getPatientUrl());
		url.append("?esDocumentoArgentino=");
		url.append(basicDataPersonBo.getIdentificationTypeId().equals(IDENTIFICATION_TYPE_DNI) ? true : false);
		url.append("&numeroDocumento=");
		url.append(basicDataPersonBo.getIdentificationNumber());
		url.append("&sexo=");
		url.append(getCipresGenderInitial(basicDataPersonBo.getGenderId()));
		url.append("&esDocumentoPropio=true&incluirRelaciones=false&incluirDomicilio=false");
		return url.toString();
	}

}
