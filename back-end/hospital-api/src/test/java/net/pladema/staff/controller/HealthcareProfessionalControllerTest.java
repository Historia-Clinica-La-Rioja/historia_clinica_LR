package net.pladema.staff.controller;

import net.pladema.UnitController;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HealthcareProfessionalController.class)
public class HealthcareProfessionalControllerTest extends UnitController {

	@MockBean
	private HealthcareProfessionalService healthcareProfessionalService;

	@MockBean
	private HealthcareProfessionalMapper healthcareProfessionalMapper;

	@MockBean
	private LoggedUserExternalService loggedUserExternalService;

	@Autowired
	private MessageSource messageSource;

	@Before
	public void setup() {
	}

	@Test
	@WithMockUser
	public void test_not_exist_professional() throws Exception {

		when(healthcareProfessionalService.findProfessionalById(any())).thenThrow(new NotFoundException("code", "test"));
		final String GET = "/institution/1/healthcareprofessional/1";

		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.code").value("code"))
				.andExpect(jsonPath("$.text").value("test"));
	}


	@Test
	@WithMockUser
	public void test_exist_professional() throws Exception {
		ProfessionalDto mock = new ProfessionalDto();
		mock.setId(1);
		mock.setLicenceNumber("1234/5");
		mock.setFirstName("Juan");
		mock.setLastName("Perez");
		mock.setIdentificationNumber("1234");


		when(healthcareProfessionalService.findProfessionalById(any())).thenReturn(null);
		when(healthcareProfessionalMapper.fromProfessionalBo(any())).thenReturn(mock);
		final String GET = "/institution/1/healthcareprofessional/1";

		this.mockMvc.perform(MockMvcRequestBuilders.get(GET))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.licenceNumber").value("1234/5"))
				.andExpect(jsonPath("$.firstName").value("Juan"))
				.andExpect(jsonPath("$.lastName").value("Perez"))
				.andExpect(jsonPath("$.identificationNumber").value("1234"));
	}

}
