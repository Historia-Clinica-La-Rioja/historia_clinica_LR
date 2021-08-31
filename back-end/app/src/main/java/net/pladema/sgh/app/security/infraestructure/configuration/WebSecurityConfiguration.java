package net.pladema.sgh.app.security.infraestructure.configuration;

import net.pladema.permissions.repository.enums.ERole;
import ar.lamansys.sgx.shared.configuration.ActuatorConfiguration;
import net.pladema.sgh.app.security.infraestructure.filters.AuthenticationTokenFilter;
import net.pladema.sgh.app.security.infraestructure.filters.PublicApiAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String RECAPTCHA = "/recaptcha";

	private static final String PASSWORD_RESET = "/password-reset";
	
	private static final String BACKOFFICE = "/backoffice";

	private static final String PUBLIC = "/public";

	private static final String[] SWAGGER_RESOURCES = {
			"/v2/**",
			"/swagger-ui.html",
			"/swagger-resources/**",
			"/webjars/springfox-swagger-ui/**"
	};

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

	private final PublicApiAuthenticationFilter publicApiAuthenticationFilter;

	public WebSecurityConfiguration(AuthenticationTokenFilter authenticationTokenFilter,
									ActuatorConfiguration actuatorConfiguration,
									PublicApiAuthenticationFilter publicApiAuthenticationFilter) {
		this.authenticationTokenFilter = authenticationTokenFilter;
		this.actuatorConfiguration = actuatorConfiguration;
		this.publicApiAuthenticationFilter = publicApiAuthenticationFilter;
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
				.antMatchers("/actuator/health").permitAll()
				.antMatchers("/actuator/**").access(actuatorConfiguration.getAccessInfo())
				.antMatchers(apiAuth + "/**").permitAll()
				.antMatchers(SWAGGER_RESOURCES).permitAll()
				.antMatchers(BACKOFFICE + "/**").hasAnyAuthority(
					ERole.ROOT.getValue(),
					ERole.ADMINISTRADOR.getValue(),
					ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.getValue())
				.antMatchers(RECAPTCHA + "/**").permitAll()
				.antMatchers("/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET,PUBLIC + "/**").permitAll()
				.antMatchers(HttpMethod.POST, PASSWORD_RESET).permitAll()
				.antMatchers(HttpMethod.GET, "/bed/reports/**").permitAll()
				.antMatchers(HttpMethod.GET, "/assets/**").permitAll()
				.antMatchers("/public-api/**").hasAnyAuthority(ERole.API_CONSUMER.getValue())
				.antMatchers("/fhir/**").permitAll()
				.antMatchers("/**").authenticated()
		.anyRequest().authenticated();

		// @formatter:on
		httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		httpSecurity.addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterAfter(publicApiAuthenticationFilter, AuthenticationTokenFilter.class);
	}

}

