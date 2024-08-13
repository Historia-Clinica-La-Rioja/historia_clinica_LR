package ar.lamansys.sgh.publicapi.sipplus.infrastructure.output;

import ar.lamansys.sgh.publicapi.sipplus.application.port.out.PublicSipPlusStorage;
import ar.lamansys.sgh.publicapi.sipplus.application.port.out.exceptions.SipPlusException;
import ar.lamansys.sgh.publicapi.sipplus.application.port.out.exceptions.SipPlusExceptionEnum;
import ar.lamansys.sgh.publicapi.sipplus.domain.EmbeddedAuthenticationDataBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusCoordinatesBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionIdBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusMotherIdentificationBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusPermission;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusUserBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.HospitalUserPersonInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicSipPlusStorageImpl implements PublicSipPlusStorage {

	private static final String COUNTRY_ID = "AR";
	private static final Boolean PUBLIC_ROLE = false;
	private static final Boolean IGNORE_LOCKS = false;

	private final SharedPersonPort sharedPersonPort;
	private final SharedPermissionPort sharedPermissionPort;
	private final SharedHospitalUserPort sharedHospitalUserPort;
	private final SharedInstitutionPort sharedInstitutionPort;
	private final SharedPatientPort sharedPatientPort;

	@Override
	public EmbeddedAuthenticationDataBo getDataForAuthentication(String accessData) {
		Integer pregnancy = null;

		String [] accessDataInfo = accessData.split("\\$");
		String token = accessDataInfo[0];
		Integer institutionId = Integer.valueOf(accessDataInfo[1]);
		Integer patientId = Integer.valueOf(accessDataInfo[2]);

		if (accessDataInfo.length > 3)
			pregnancy = Integer.valueOf(accessDataInfo[3]);

		Integer userId = sharedHospitalUserPort.fetchUserInfoFromNormalToken(token)
				.orElseThrow( () -> new SipPlusException(SipPlusExceptionEnum.INVALID_TOKEN, "Error en el token - Es posible que la sesión del usuario haya expirado"))
				.getId();

		SipPlusInstitutionBo sipPlusInstitutionBo = getUserInstitution(institutionId);

		return EmbeddedAuthenticationDataBo.builder()
				.embedCoordinates(getCoordinates(patientId, pregnancy))
				.user(getUserData(userId, institutionId, sipPlusInstitutionBo))
				.institution(sipPlusInstitutionBo)
				.build();
	}

	private SipPlusCoordinatesBo getCoordinates(Integer patientId, Integer pregnancy) {
		BasicPatientDto patientData = sharedPatientPort.getBasicDataFromPatient(patientId);
		String countryIsoCode = getCountryIsoCode(patientData.getPerson().getId());

		validatePatientData(patientData);

		String embedId = getEmbedId(countryIsoCode, patientData, pregnancy);

		SipPlusMotherIdentificationBo motherIdentification = SipPlusMotherIdentificationBo.builder()
				.number(patientData.getIdentificationNumber())
				.typeCode(patientData.getIdentificationType())
				.countryCode(countryIsoCode)
				.build();

		return SipPlusCoordinatesBo.builder()
				.form(SipPlusPermission.FORM_SIPNM2)
				.embedId(embedId)
				.motherIdentification(motherIdentification)
				.ignoreLocks(IGNORE_LOCKS)
				.pregnancy(pregnancy)
				.build();

	}

	private String getEmbedId(String countryIsoCode, BasicPatientDto patientData, Integer pregnancy) {
		return countryIsoCode + patientData.getIdentificationType() + patientData.getIdentificationNumber() + pregnancy;
	}

	private String getCountryIsoCode(Integer personId){
		String countryIsoCode = sharedPersonPort.getCountryIsoCodeFromPerson(personId);
		return countryIsoCode != null ? countryIsoCode : COUNTRY_ID;
	}

	private void validatePatientData(BasicPatientDto patientData) {
		if (patientData.getIdentificationType() == null || patientData.getIdentificationType().isBlank())
			throw new SipPlusException(SipPlusExceptionEnum.NULL_IDENTIFICATION_TYPE, "La paciente no cuenta con tipo de identificacion cargado en el sistema, es un dato requerido");
		if (patientData.getIdentificationNumber() == null || patientData.getIdentificationNumber().isBlank())
			throw new SipPlusException(SipPlusExceptionEnum.NULL_IDENTIFICATION_NUMBER, "La paciente no cuenta con número de identificacion cargado en el sistema, es un dato requerido");
	}

	private SipPlusUserBo getUserData(Integer userId, Integer institutionId,
									  SipPlusInstitutionBo sipPlusInstitutionBo) {
		HospitalUserPersonInfoDto userPersonData = sharedHospitalUserPort.getUserCompleteInfo(userId);
		String countryIsoCode = getCountryIsoCode(userPersonData.getPersonId());

		String userFullName = Stream.of(userPersonData.getFirstName(), userPersonData.getLastName())
				.filter(Objects::nonNull).collect(Collectors.joining(" "));

		return SipPlusUserBo.builder()
				.id(getUUID(userId))
				.userName(userPersonData.getFirstName())
				.fullName(userFullName)
				.countryId(countryIsoCode)
				.roles(getUserRoles(userId, institutionId))
				.institutions(Arrays.asList(sipPlusInstitutionBo))
				.readableInstitutions(Arrays.asList(sipPlusInstitutionBo))
				.build();
	}

	private SipPlusInstitutionBo getUserInstitution(Integer institutionId) {
		InstitutionInfoDto institution = sharedInstitutionPort.fetchInstitutionById(institutionId);
		SipPlusInstitutionIdBo sipPlusInstitutionId = SipPlusInstitutionIdBo.builder()
				.countryId(COUNTRY_ID)
				.code(institution.getSisaCode())
				.divisionId("")
				.subdivisionId("")
				.build();

		return SipPlusInstitutionBo.builder()
				.id(sipPlusInstitutionId)
				.name(institution.getName())
				.build();
	}

	private JSONArray getUserRoles(Integer userId, Integer institutionId) {
		JSONArray roles = new JSONArray();
		List<RoleInfoDto> userRoles = sharedPermissionPort.ferPermissionInfoByUserId(userId)
				.stream()
				.filter(ur -> ur.getInstitution().equals(institutionId))
				.collect(Collectors.toList());

		userRoles.stream().forEach(ur -> {
			roles.add(getJSONRole(ur));
		});
		return roles;
	}

	private JSONObject getJSONRole(RoleInfoDto userRoleInfo) {
		JSONObject role = new JSONObject();
		role.put("id", getUUID(Integer.valueOf(userRoleInfo.getId())));
		role.put("name", userRoleInfo.getValue());
		role.put("public", PUBLIC_ROLE);
		role.put("permissions", SipPlusPermission.getUserPermissions());
		return role;
	}

	private String getUUID(Integer userId) {
		String userUUID = String.format("%032d", userId);
		String uuid = userUUID.replaceAll(
				"(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
				"$1-$2-$3-$4-$5");
		return uuid;
	}


}
