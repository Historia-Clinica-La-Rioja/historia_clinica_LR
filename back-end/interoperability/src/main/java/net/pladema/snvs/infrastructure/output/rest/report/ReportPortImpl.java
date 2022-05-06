package net.pladema.snvs.infrastructure.output.rest.report;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.report.ReportPort;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortEnumException;
import net.pladema.snvs.application.ports.report.exceptions.ReportPortException;
import net.pladema.snvs.domain.patient.AddressDataBo;
import net.pladema.snvs.domain.patient.PatientDataBo;
import net.pladema.snvs.domain.report.SnvsReportBo;
import net.pladema.snvs.domain.report.SnvsToReportBo;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsAddressDto;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsCitizenDto;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsEventRegisterResponse;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsNominalCaseEventDto;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsReportDto;
import net.pladema.snvs.infrastructure.output.rest.report.domain.SnvsTutorDto;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static net.pladema.snvs.infrastructure.output.rest.report.SisaEnumException.SISA_TIMEOUT_SERVICE;


@Service
@Slf4j
@Conditional(SnvsCondition.class)
public class ReportPortImpl implements ReportPort {

    private final SisaWSConfig sisaWSConfig;

    private final RestClientInterface restClient;


    public ReportPortImpl(SnvsRestTemplate snvsRestTemplate,
                          SisaWSConfig sisaWSConfig) {
        super();
        this.sisaWSConfig = sisaWSConfig;
        this.restClient = new RestClient(snvsRestTemplate, sisaWSConfig);
    }


    @Override
    public SnvsReportBo run(SnvsToReportBo toReportBo) throws ReportPortException {
        validInput(toReportBo);
        var body = mapBody(toReportBo);
        var relativeUrl = sisaWSConfig.getNominalCase();
        ResponseEntity<SnvsEventRegisterResponse> response;
        try {
            response = restClient.exchangePost(relativeUrl, body, SnvsEventRegisterResponse.class);
        } catch (RestTemplateApiException e) {
            log.error("Fallo en la comunicación", e);
            return mapResponse(toReportBo, e.getStatusCode().value(), e.mapErrorBody(SnvsEventRegisterResponse.class));
        }
        SnvsEventRegisterResponse result = response.getBody();
        if (result == null)
            throw new SisaTimeoutException(SISA_TIMEOUT_SERVICE, String.format("Fallo en la comunicación %s", sisaWSConfig.getBaseUrl()+relativeUrl));
        return mapResponse(toReportBo, response.getStatusCode().value(), result);
    }

    private void validInput(SnvsToReportBo toReportBo) throws ReportPortException {
        if (toReportBo == null)
            throw new ReportPortException(ReportPortEnumException.NULL_REPORT, "La información a reportar es obligatoria");
    }

    private SnvsReportBo mapResponse(SnvsToReportBo toReportBo,
                                     Integer responseCode, SnvsEventRegisterResponse response) {
        var result = new SnvsReportBo();
        result.setEventId(toReportBo.getEventId());
        result.setGroupEventId(toReportBo.getGroupEventId());
        result.setManualClassificationId(toReportBo.getManualClassificationId());
        result.setInstitutionId(toReportBo.getInstitutionId());
        result.setLastUpdate(toReportBo.getDate());
        result.setPatientId(toReportBo.getPatientId());
        result.setResponseCode(responseCode.shortValue());
        result.setStatus(buildStatus(response));
        result.setProfessionalId(toReportBo.getProfessionalId());
        result.setSisaRegisteredId(response == null ? null : response.getIdCaso());
        return result;
    }

    private String buildStatus(SnvsEventRegisterResponse response) {
        return response == null ? null :
                new StringBuilder()
                        .append("Description=")
                        .append(response.getDescription())
                        .append(", Resultado=")
                        .append(response.getResultado())
                        .append(", Timestamp=")
                        .append(response.getTimestamp())
                        .toString();
    }

    private SnvsReportDto mapBody(SnvsToReportBo toReportBo) {
        var result = new SnvsReportDto();
        result.setCiudadano(buildCitizen(toReportBo.getPatient()));
        result.setEventoCasoNominal(buildEventoCasoNominal(toReportBo));
        return result;
    }

    private SnvsNominalCaseEventDto buildEventoCasoNominal(SnvsToReportBo toReportBo) {
        return new SnvsNominalCaseEventDto(toReportBo.getEventId(), toReportBo.getGroupEventId(),
                toReportBo.getManualClassificationId(),
                toReportBo.getInstitutionSisaCode(), parseDate(toReportBo.getDate()));
    }

    private String parseDate(LocalDate date) {
        return date == null ? null : date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private SnvsCitizenDto buildCitizen(PatientDataBo patient) {
        var result = new SnvsCitizenDto();
        result.setNombre(patient.getFirstName());
        result.setApellido(patient.getLastName());
        result.setSexo(buildGender(patient.getGenderId()));
        result.setMail(patient.getEmail());
        result.setNumeroDocumento(patient.getIdentificationNumber());
        result.setTipoDocumento(patient.getIdentificationTypeId()); // TODO hay que chequear si el mapeo funciona
        result.setFechaNacimiento(parseDate(patient.getBirthDate()));
        result.setTelefono(patient.getPhoneNumber());
        result.setSeDeclaraPuebloIndigena(patient.getEthnicityId() != null ? "Si" : "No");
        result.setPersonaACargo(new SnvsTutorDto());
        result.setDomicilio(buildAddress(patient.getAddress()));
        return result;
    }

    private String buildGender(Short genderId){
		switch (genderId) {
			case (short) 1:
				return "F";
			case (short) 2:
				return "M";
			default:
				return "X";
		}
	}

    private SnvsAddressDto buildAddress(AddressDataBo address) {
        if (address == null)
            return null;
        var result = new SnvsAddressDto();
        result.setCalle(address.getStreet());
        result.setIdDepartamento(address.getDepartmentId());
        result.setIdLocalidad(address.getCityId());
        result.setIdProvincia(address.getProvinceId());
        result.setIdPais(address.getCountryId());
        return result;
    }


}
