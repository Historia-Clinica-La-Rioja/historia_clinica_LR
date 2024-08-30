package ar.lamansys.sgh.publicapi.sipplus.application.getauthenticationdata;

import ar.lamansys.sgh.publicapi.sipplus.application.port.out.PublicSipPlusStorage;
import ar.lamansys.sgh.publicapi.sipplus.domain.EmbeddedAuthenticationDataBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusCoordinatesBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusInstitutionIdBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusMotherIdentificationBo;
import ar.lamansys.sgh.publicapi.sipplus.domain.SipPlusUserBo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAuthenticationDataTest {

	@Mock
	private PublicSipPlusStorage publicSipPlusStorage;

	@InjectMocks
	private GetAuthenticationData getAuthenticationData;

	@Test
	void testRun() {

		String accessData = "testAccessData";

		SipPlusInstitutionIdBo institutionIdBo = SipPlusInstitutionIdBo.builder()
				.countryId("ARG")
				.divisionId("DIV1")
				.subdivisionId("SUB1")
				.code("CODE1")
				.build();

		SipPlusInstitutionBo institutionBo = SipPlusInstitutionBo.builder()
				.id(institutionIdBo)
				.name("Test Institution")
				.build();

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

		SipPlusUserBo userBo = SipPlusUserBo.builder()
				.id("UserID")
				.userName("UserName")
				.fullName("User Full Name")
				.countryId("ARG")
				.roles(new net.minidev.json.JSONArray())
				.institutions(Collections.singletonList(institutionBo))
				.build();

		EmbeddedAuthenticationDataBo authenticationDataBo = EmbeddedAuthenticationDataBo.builder()
				.user(userBo)
				.embedCoordinates(coordinatesBo)
				.institution(institutionBo)
				.build();

		when(publicSipPlusStorage.getDataForAuthentication(accessData)).thenReturn(authenticationDataBo);

		EmbeddedAuthenticationDataBo result = getAuthenticationData.run(accessData);

		assertEquals(authenticationDataBo, result);
		assertEquals("UserID", result.getUser().getId());
		assertEquals("UserName", result.getUser().getUserName());
		assertEquals("Test Institution", result.getInstitution().getName());
		assertEquals("Form1", result.getEmbedCoordinates().getForm());

		assertEquals("SipPlusInstitutionIdBo(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1)", institutionIdBo.toString());
		assertEquals("SipPlusInstitutionBo(id=SipPlusInstitutionIdBo(countryId=ARG, divisionId=DIV1, subdivisionId=SUB1, code=CODE1), name=Test Institution)", institutionBo.toString());
		assertEquals("SipPlusMotherIdentificationBo(countryCode=ARG, typeCode=DNI, number=12345678)", motherIdentificationBo.toString());
		assertEquals("SipPlusCoordinatesBo(form=Form1, embedId=Embed1, motherIdentification=SipPlusMotherIdentificationBo(countryCode=ARG, typeCode=DNI, number=12345678), ignoreLocks=false, pregnancy=1)", coordinatesBo.toString());
	}
}
