package ar.lamansys.sgh.publicapi.sipplus.infrastructure.output;

import ar.lamansys.sgh.publicapi.sipplus.application.port.out.exceptions.SipPlusException;
import ar.lamansys.sgh.publicapi.sipplus.application.port.out.exceptions.SipPlusExceptionEnum;
import ar.lamansys.sgh.publicapi.sipplus.domain.*;
import ar.lamansys.sgh.shared.infrastructure.input.service.*;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.InstitutionInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.user.dto.UserSharedInfoDto;

import net.minidev.json.JSONArray;

import net.minidev.json.JSONObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicSipPlusStorageImplTest {

	@Mock
	private SharedPersonPort sharedPersonPort;

	@Mock
	private SharedPermissionPort sharedPermissionPort;

	@Mock
	private SharedHospitalUserPort sharedHospitalUserPort;

	@Mock
	private SharedInstitutionPort sharedInstitutionPort;

	@Mock
	private SharedPatientPort sharedPatientPort;

	private PublicSipPlusStorageImpl publicSipPlusStorage;

	private final Integer USER_ID = 1001;
	private final Integer INSTITUTION_ID = 1;
	private final Integer PATIENT_ID = 123;

	private EmbeddedAuthenticationDataBo authenticationData;
	private SipPlusMotherIdentificationBo motherIdentification;
	private SipPlusCoordinatesBo coordinatesBo;
	private SipPlusInstitutionIdBo institutionIdBo;
	private SipPlusInstitutionBo institutionBo;
	private SipPlusUserBo userBo;
	private BasicPatientDto patientDto;
	private BasicDataPersonDto personDto;
	private HospitalUserPersonInfoDto userPersonData;

	@BeforeEach
	void setUp() {
		publicSipPlusStorage = new PublicSipPlusStorageImpl(sharedPersonPort,sharedPermissionPort,sharedHospitalUserPort,sharedInstitutionPort,sharedPatientPort);
		patientDto = new BasicPatientDto();
		personDto = new BasicDataPersonDto();
		personDto.setId(PATIENT_ID);
		personDto.setIdentificationType("DNI");
		personDto.setIdentificationNumber("12345678");
		patientDto.setPerson(personDto);

		motherIdentification = SipPlusMotherIdentificationBo.builder()
				.countryCode("ARG")
				.typeCode("DNI")
				.number("12345678")
				.build();

		coordinatesBo = SipPlusCoordinatesBo.builder()
				.form("Form1")
				.embedId("Embed1")
				.motherIdentification(motherIdentification)
				.ignoreLocks(false)
				.pregnancy(1)
				.build();

		institutionIdBo = SipPlusInstitutionIdBo.builder()
				.countryId("AR")
				.divisionId("DIV1")
				.subdivisionId("SUB1")
				.code("CODE1")
				.build();

		institutionBo = SipPlusInstitutionBo.builder()
				.id(institutionIdBo)
				.name("Institution")
				.build();

		userBo = SipPlusUserBo.builder()
				.id("UserID")
				.userName("UserName")
				.fullName("User Full Name")
				.countryId("AR")
				.roles(new JSONArray())
				.institutions(Collections.singletonList(institutionBo))
				.build();

		authenticationData = EmbeddedAuthenticationDataBo.builder()
				.user(userBo)
				.institution(institutionBo)
				.embedCoordinates(coordinatesBo)
				.build();

		userPersonData = HospitalUserPersonInfoDto.builder()
				.id(1)
				.username("username")
				.email("email")
				.personId(PATIENT_ID)
				.firstName("firstName")
				.lastName("lastName")
				.nameSelfDetermination("nameSelfDetermination")
				.build();

	}
	@Test
	void testGetDataForAuthenticationSuccess() {
		String accessData = "validToken$1$123";

		when(sharedHospitalUserPort.getUserCompleteInfo(USER_ID))
				.thenReturn(userPersonData);

		when(sharedHospitalUserPort.fetchUserInfoFromNormalToken("validToken"))
				.thenReturn(Optional.of(new UserSharedInfoDto(USER_ID, "username")));

		when(sharedInstitutionPort.fetchInstitutionById(INSTITUTION_ID))
				.thenReturn(new InstitutionInfoDto(INSTITUTION_ID, "Institution"));

		when(sharedPatientPort.getBasicDataFromPatient(PATIENT_ID))
				.thenReturn(patientDto);

		when(sharedPersonPort.getCountryIsoCodeFromPerson(PATIENT_ID))
				.thenReturn("AR");

		when(sharedPermissionPort.ferPermissionInfoByUserId(USER_ID))
				.thenReturn(Collections.emptyList());

		EmbeddedAuthenticationDataBo result = publicSipPlusStorage.getDataForAuthentication(accessData);

		assertNotNull(result);
		assertNotNull(result.getUser());
		assertNotNull(result.getInstitution());
		assertNotNull(result.getEmbedCoordinates());
		assertEquals(authenticationData.getUser().getCountryId(), result.getUser().getCountryId());
		assertEquals(authenticationData.getEmbedCoordinates().getMotherIdentification().getTypeCode(), result.getEmbedCoordinates().getMotherIdentification().getTypeCode());
		assertEquals(authenticationData.getEmbedCoordinates().getMotherIdentification().getNumber(), result.getEmbedCoordinates().getMotherIdentification().getNumber());
		assertEquals(authenticationData.getInstitution().getName(), result.getInstitution().getName());

		verify(sharedHospitalUserPort, times(1)).fetchUserInfoFromNormalToken("validToken");
		verify(sharedInstitutionPort, times(1)).fetchInstitutionById(INSTITUTION_ID);
		verify(sharedPatientPort, times(1)).getBasicDataFromPatient(PATIENT_ID);
	}

	@Test
	void testGetDataForAuthenticationInvalidToken() {
		String accessData = "invalidToken$1$123";

		when(sharedHospitalUserPort.fetchUserInfoFromNormalToken("invalidToken"))
				.thenReturn(Optional.empty());

		SipPlusException exception = assertThrows(SipPlusException.class, () -> {
			publicSipPlusStorage.getDataForAuthentication(accessData);
		});

		assertEquals(SipPlusExceptionEnum.INVALID_TOKEN.toString(), exception.getCode());
		assertEquals("Error en el token - Es posible que la sesión del usuario haya expirado", exception.getMessage());
	}

	@Test
	void testGetDataForAuthenticationNullIdentificationType() {
		String accessData = "validToken$1$123";
		personDto.setIdentificationType(null);

		when(sharedHospitalUserPort.fetchUserInfoFromNormalToken("validToken"))
				.thenReturn(Optional.of(new UserSharedInfoDto(USER_ID, "username")));

		when(sharedInstitutionPort.fetchInstitutionById(INSTITUTION_ID))
				.thenReturn(new InstitutionInfoDto(INSTITUTION_ID, "Institution"));

		when(sharedPatientPort.getBasicDataFromPatient(PATIENT_ID))
				.thenReturn(patientDto);

		SipPlusException exception = assertThrows(SipPlusException.class, () -> {
			publicSipPlusStorage.getDataForAuthentication(accessData);
		});

		assertEquals(SipPlusExceptionEnum.NULL_IDENTIFICATION_TYPE.toString(), exception.getCode());
		assertEquals("La paciente no cuenta con tipo de identificacion cargado en el sistema, es un dato requerido", exception.getMessage());
	}

	@Test
	void testGetDataForAuthenticationNullIdentificationNumber() {
		String accessData = "validToken$1$123";
		personDto.setIdentificationNumber(null);

		when(sharedHospitalUserPort.fetchUserInfoFromNormalToken("validToken"))
				.thenReturn(Optional.of(new UserSharedInfoDto(USER_ID, "username")));

		when(sharedInstitutionPort.fetchInstitutionById(INSTITUTION_ID))
				.thenReturn(new InstitutionInfoDto(INSTITUTION_ID, "Institution"));

		when(sharedPatientPort.getBasicDataFromPatient(PATIENT_ID))
				.thenReturn(patientDto);

		SipPlusException exception = assertThrows(SipPlusException.class, () -> {
			publicSipPlusStorage.getDataForAuthentication(accessData);
		});

		assertEquals(SipPlusExceptionEnum.NULL_IDENTIFICATION_NUMBER.toString(), exception.getCode());
		assertEquals("La paciente no cuenta con número de identificacion cargado en el sistema, es un dato requerido", exception.getMessage());
	}

	@Test
	void testGetJSONRole() {
		RoleInfoDto userRoleInfo = mock(RoleInfoDto.class);
		when(userRoleInfo.getId()).thenReturn((short) 123);
		when(userRoleInfo.getValue()).thenReturn("Admin");

		JSONObject result = invokeGetJSONRole(userRoleInfo);

		assertEquals(getUUID(Integer.valueOf(123)).toString(), result.get("id"));
		assertEquals("Admin", result.get("name"));
		assertEquals(false, result.get("public"));
		assertEquals(SipPlusPermission.getUserPermissions(), result.get("permissions"));
	}

	private UUID getUUID(Integer id) {
		return UUID.fromString(String.format("00000000-0000-0000-0000-%012d", id));
	}

	private JSONObject invokeGetJSONRole(RoleInfoDto userRoleInfo) {
		try {
			java.lang.reflect.Method method = PublicSipPlusStorageImpl.class.getDeclaredMethod("getJSONRole", RoleInfoDto.class);
			method.setAccessible(true);
			return (JSONObject) method.invoke(publicSipPlusStorage, userRoleInfo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}

