package net.pladema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.security.service.SecurityService;
import net.pladema.security.token.service.TokenService;
import net.pladema.sgx.actuator.configuration.ActuatorConfiguration;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.sgx.i18n.CustomMessageSourceConfiguration;


@Import({CustomMessageSourceConfiguration.class, JacksonDateFormatConfig.class, ActuatorConfiguration.class})
public class BaseControllerTest {
	
    @Autowired
    protected MockMvc mockMvc;

	@Autowired
	@Deprecated
	// Crear los Body de los Requests usando el ObjectMapper puede resultar obvio pero en realidad es una mala práctica
	// Si el DTO cambia, el test puede que siga funcionando pero se está modificando la API y se pueden estar rompiendo
	// los clientes que usen ese Body.
	// Escribir los request como String así nos obligamos a modificarlos cuando se modifica un DTO.
    protected ObjectMapper objectMapper;
	
	@MockBean
	protected SecurityService securityService;
	
	@MockBean
	protected TokenService tokenService;
	
}