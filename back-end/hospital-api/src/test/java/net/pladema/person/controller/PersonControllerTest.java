package net.pladema.person.controller;

import net.pladema.BaseControllerTest;
import net.pladema.address.controller.service.AddressExternalService;
import net.pladema.person.controller.mapper.PersonMapper;
import net.pladema.person.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest extends BaseControllerTest {

	@MockBean
	private PersonService personService;

	@MockBean
	private AddressExternalService addressExternalService;

	@MockBean
	private PersonMapper personMapper;

	@Before
	public void setup() {
	}

	@Test
	public void test_getPersonalInformation() throws Exception {
		final Integer personId = 1;
		final String URL = "/person/"+personId +"/personalInformation";
		when(personService.getPersonalInformation(any())).thenReturn(Optional.empty());
		mockMvc.perform(get(URL))
			.andDo(log())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(personId));
	}
	
}
