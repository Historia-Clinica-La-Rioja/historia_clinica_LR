package net.pladema.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import net.pladema.actuator.configuration.ActuatorConfiguration;
import net.pladema.security.filters.AuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String PERSON = "/person";
	
	private static final String PATIENT = "/patient/**";

	private static final String RENAPER = "/renaper/**";
	
	private static final String HEALTH = "/health";

	private static final String MASTERDATA_ADDRESS = "/address/masterdata";

	private static final String ADDRESS = "/address";

	private static final String MASTERDATA = "/masterdata/**";

	private static final String I18N = "/i18n";

	private static final String RECAPTCHA = "/recaptcha";

	private static final String PAS$W0RD_RESET = "/password-reset";

	private static final String MASTERDATA_INTERNMENT = "/internments/masterdata";

	private static final String INTERMENT = "/institutions/{institutionId}/internments/";

	private static final String ANAMNESIS = "/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis";

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
				.antMatchers().permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/actuator/**").access(actuatorConfiguration.getAccessInfo())
				.antMatchers(apiAuth + "/**").permitAll()
				.antMatchers(HttpMethod.GET,MASTERDATA_INTERNMENT + "/**").permitAll()
				.antMatchers(INTERMENT + "/**").permitAll()
				.antMatchers(ANAMNESIS + "/**").permitAll()
				.antMatchers(HttpMethod.GET, ADDRESS+ "/**").permitAll()
				.antMatchers(HttpMethod.GET, MASTERDATA_ADDRESS+ "/**").permitAll()
				.antMatchers(HttpMethod.GET, apiUser + "/{id}" + activateApiUser).permitAll()
				.antMatchers(HttpMethod.POST, apiUser + "/activationlink/resend").permitAll()
				.antMatchers(apiPassword + apiPasswordReset ).permitAll()
				.antMatchers(HttpMethod.POST, PERSON ).permitAll()
				.antMatchers(HttpMethod.GET, PERSON+"/**" ).permitAll()
				.antMatchers(PATIENT).permitAll()
				.antMatchers(RENAPER).permitAll()
				.antMatchers(HttpMethod.GET, PERSON+"/**" ).permitAll()
				.antMatchers(HEALTH + "/**").permitAll()
				.antMatchers(I18N + "/**").permitAll()
				.antMatchers(RECAPTCHA + "/**").permitAll()
				.antMatchers(HttpMethod.POST, PAS$W0RD_RESET ).permitAll()
				.antMatchers(MASTERDATA).permitAll()
				.antMatchers("/v2/**","/swagger-ui.html","/swagger-resources/**","/webjars/springfox-swagger-ui/**").permitAll()
				.anyRequest().authenticated();
		// @formatter:on
		httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		httpSecurity.addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

