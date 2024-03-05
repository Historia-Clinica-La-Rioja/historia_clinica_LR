package net.pladema.cipres.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix="ws.cipres")
@Getter
public class CipresWSConfig extends WSConfig {

	private static final String LOGIN = "login";

	private static final String CLINICAL_SPECIALTIES =  "api/consultorio/especialidades_medicas";

	private static final String PATIENT = "api/patient";

	private static final String CONSULTATION = "/api/consultorio/consulta";

	private static final String DEPENDENCIES = "/api/establecimiento/dependencias";

	private static final String ADDRESS = "/api/domicilio";

	private static final String COUNTRY = "api/geolocalizacion/pais";

	private static final String CITIES = "/api/geolocalizacion/localidades";

	private static final String NACIONALITY = "/api/referencias/nacionalidades";

	private static final Duration DEFAULT_TOKEN_EXPIRATION = Duration.ofSeconds(10L);

	private String username;

	private String password;

	private String realUserName;

	private Duration tokenExpiration;

	public CipresWSConfig(@Value("${ws.cipres.url.base:}") String baseUrl,
						  @Value("${ws.cipres.username:}") String username,
						  @Value("${ws.cipres.password:}") String password,
						  @Value("${ws.cipres.realusername:}") String realUsername) {
		super(baseUrl, false);
		this.username = username;
		this.password = password;
		this.realUserName = realUsername;
		this.tokenExpiration = DEFAULT_TOKEN_EXPIRATION;
	}

	public String getLoginUrl() {
		return LOGIN;
	}

	public String getPatientUrl() {
		return PATIENT;
	}


	public String getClinicalSpecialtiesUrl() { return CLINICAL_SPECIALTIES; }

	public String getConsultationUrl() { return CONSULTATION; }

	public String getDependenciesUrl() { return DEPENDENCIES; }

	public String getCitiesUrl() { return CITIES; }

	public String getAddressUrl() { return ADDRESS; }

	public String getCountryUrl() { return COUNTRY; }

	public String getNationalityUrl() { return NACIONALITY; }

}
