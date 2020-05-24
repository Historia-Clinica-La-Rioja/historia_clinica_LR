package net.pladema.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.pladema.actuator.configuration.ActuatorConfiguration;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.security.filters.AuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String RECAPTCHA = "/recaptcha";

	private static final String PASSWORD_RESET = "/password-reset";
	
	private static final String BACKOFFICE = "/backoffice";

	private static final String PUBLIC = "/public";

	@Value("${api.user}")
	protected String apiUser;

	@Value("${api.password}")
	protected String apiPassword;

	@Value("${api.user.activateUser}")
	protected String activateApiUser;

	@Value("${api.password.reset}")
	protected String apiPasswordReset;

	@Value("${api.auth}")
	protected String apiAuth;

	private final AuthenticationTokenFilter authenticationTokenFilter;

	private final ActuatorConfiguration actuatorConfiguration;

	public WebSecurityConfiguration(AuthenticationTokenFilter authenticationTokenFilter, ActuatorConfiguration actuatorConfiguration) {
		this.authenticationTokenFilter = authenticationTokenFilter;
		this.actuatorConfiguration = actuatorConfiguration;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		// @formatter:off
		httpSecurity.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers(HttpMethod.GET, apiUser + "/{id}" + activateApiUser).permitAll()
		.antMatchers(HttpMethod.POST, apiUser + "/activationlink/resend").permitAll()
		.antMatchers(apiPassword + apiPasswordReset ).permitAll()
		.antMatchers("/actuator/**").access(actuatorConfiguration.getAccessInfo())
		.antMatchers(apiAuth + "/**").permitAll()
		.antMatchers("/v2/**","/swagger-ui.html","/swagger-resources/**","/webjars/springfox-swagger-ui/**").permitAll()
		.antMatchers(BACKOFFICE + "/**").hasAnyAuthority(ERole.ROOT.getValue(), ERole.ADMINISTRADOR.getValue())
		.antMatchers(RECAPTCHA + "/**").permitAll()
		.antMatchers(HttpMethod.GET,PUBLIC + "/**").permitAll()
		.antMatchers(HttpMethod.POST, PASSWORD_RESET).permitAll()
		.antMatchers("/**").authenticated()
		.anyRequest().authenticated();

		// @formatter:on
		httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		httpSecurity.addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

