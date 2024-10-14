package ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.sipplus.application.getauthenticationdata.GetAuthenticationData;
import ar.lamansys.sgh.publicapi.sipplus.domain.EmbeddedAuthenticationDataBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusCoordinatesBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionIdBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusMotherIdentificationBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusUserBo;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.EmbeddedAuthenticationDataDto;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.SipPlusCoordinatesDto;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.SipPlusInstitutionDto;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.SipPlusInstitutionIdDto;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.SipPlusMotherIdentificationDto;
import ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto.SipPlusUserDto;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SipPlusPublicControllerTest {

	@Mock
	private GetAuthenticationData getAuthenticationData;

	private SipPlusPublicController sipPlusPublicController;

	@BeforeEach
	public void setUp() {
		sipPlusPublicController = new SipPlusPublicController(getAuthenticationData);
	}

	@Test
	void testGetAuthenticationData() {

		String accessData = "testAccessData";

		SipPlusInstitutionIdDto institutionIdDto = SipPlusInstitutionIdDto.builder()
				.countryId("ARG")
				.divisionId("DIV1")
				.subdivisionId("SUB1")
				.code("CODE1")
				.build();


		assertEquals("SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1)", institutionIdDto.toString());

		SipPlusInstitutionDto institutionDto = new SipPlusInstitutionDto();
		institutionDto.setId(institutionIdDto);
		institutionDto.setName("Test Institution");

		assertEquals("SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)", institutionDto.toString());

		SipPlusMotherIdentificationBo motherIdentificationBo = SipPlusMotherIdentificationBo.builder()
				.countryCode("ARG")
				.typeCode("DNI")
				.number("12345678")
				.build();

		SipPlusCoordinatesBo coordinatesBo = SipPlusCoordinatesBo.builder()
				.form("Form1")
				.embedId("Embed1")
				.motherIdentification(motherIdentificationBo)
				.ignoreLocks(false)
				.pregnancy(1)
				.build();

		SipPlusInstitutionIdBo institutionIdBo = SipPlusInstitutionIdBo.builder()
				.countryId("ARG")
				.divisionId("DIV1")
				.subdivisionId("SUB1")
				.code("CODE1")
				.build();

		SipPlusInstitutionBo institutionBo = new SipPlusInstitutionBo(institutionIdBo, "Test Institution");

		SipPlusUserBo userBo = SipPlusUserBo.builder()
				.id("UserID")
				.userName("UserName")
				.fullName("User Full Name")
				.countryId("ARG")
				.roles(new JSONArray())
				.institutions(Collections.singletonList(institutionBo))
				.build();

		SipPlusMotherIdentificationDto motherIdentificationDto = SipPlusMotherIdentificationDto.builder()
				.countryCode("ARG")
				.typeCode("DNI")
				.number("12345678")
				.build();

		assertEquals("SipPlusMotherIdentificationDto(countryCode=ARG, typeCode=DNI, number=12345678)", motherIdentificationDto.toString());

		SipPlusCoordinatesDto coordinatesDto = SipPlusCoordinatesDto.builder()
				.form("Form1")
				.embedId("Embed1")
				.motherIdentification(motherIdentificationDto)
				.ignoreLocks(false)
				.pregnancy(1)
				.build();

		assertEquals("SipPlusCoordinatesDto(form=Form1, embedId=Embed1, motherIdentification=SipPlusMotherIdentificationDto(countryCode=ARG, typeCode=DNI, number=12345678), ignoreLocks=false, pregnancy=1)", coordinatesDto.toString());

		SipPlusUserDto userDto = SipPlusUserDto.builder()
				.id("UserID")
				.userName("UserName")
				.fullName("User Full Name")
				.countryId("ARG")
				.roles(new JSONArray())
				.institutions(Collections.singletonList(institutionDto))
				.readableInstitutions(Collections.singletonList(institutionDto))
				.build();

		assertEquals("SipPlusUserDto(id=UserID, userName=UserName, fullName=User Full Name, countryId=ARG, roles=[], institutions=[SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)], readableInstitutions=[SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)])", userDto.toString());

		EmbeddedAuthenticationDataDto embeddedAuthenticationDataDto = EmbeddedAuthenticationDataDto.builder()
				.user(userDto)
				.institution(institutionDto)
				.embedCoordinates(coordinatesDto)
				.build();

		assertEquals("EmbeddedAuthenticationDataDto(user=SipPlusUserDto(id=UserID, userName=UserName, fullName=User Full Name, countryId=ARG, roles=[], institutions=[SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)], readableInstitutions=[SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)]), institution=SipPlusInstitutionDto(id=SipPlusInstitutionIdDto(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution), embedCoordinates=SipPlusCoordinatesDto(form=Form1, embedId=Embed1, motherIdentification=SipPlusMotherIdentificationDto(countryCode=ARG, typeCode=DNI, number=12345678), ignoreLocks=false, pregnancy=1))", embeddedAuthenticationDataDto.toString());

		EmbeddedAuthenticationDataBo authenticationDataBo = new EmbeddedAuthenticationDataBo(userBo, institutionBo, coordinatesBo);

		when(getAuthenticationData.run(accessData)).thenReturn(authenticationDataBo);

		EmbeddedAuthenticationDataDto result = sipPlusPublicController.getAuthenticationData(accessData);

		assertEquals("UserID", result.getUser().getId());
		assertEquals("UserName", result.getUser().getUserName());
		assertEquals("User Full Name", result.getUser().getFullName());
		assertEquals("ARG", result.getUser().getCountryId());
		assertEquals("Test Institution", result.getInstitution().getName());
		assertEquals("Form1", result.getEmbedCoordinates().getForm());
		assertEquals("Embed1", result.getEmbedCoordinates().getEmbedId());
	}
}
