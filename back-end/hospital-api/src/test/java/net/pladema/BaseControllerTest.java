package net.pladema;

import net.pladema.sgx.actuator.configuration.ActuatorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.sgx.i18n.CustomMessageSourceConfiguration;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.security.service.SecurityService;
import net.pladema.security.token.service.TokenService;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class})
public class BaseControllerTest {

	
	@Value("${server.servlet.context-path}")
	protected String contextPath;
	
    @Autowired
    protected MockMvc mockMvc;

	@Autowired
    protected ObjectMapper objectMapper;
	
	@MockBean
	protected SecurityService securityService;
	
	@MockBean
	protected TokenService tokenService;
	
}