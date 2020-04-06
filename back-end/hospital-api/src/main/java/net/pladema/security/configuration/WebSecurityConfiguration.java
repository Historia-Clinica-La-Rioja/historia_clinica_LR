package net.pladema.security.configuration;

import net.pladema.actuator.configuration.ActuatorConfiguration;
import net.pladema.security.filters.AuthenticationTokenFilter;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String PERSON = "/person";
	
	private static final String HEALTH = "/health";

	private static final String ADDRESS = "/address";

	private static final String MASTERDATA_HEALTH = "/masterdata/health";

	private static final String I18N = "/i18n";

	private static final String RECAPTCHA = "/recaptcha";

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
				.antMatchers(HttpMethod.GET, ADDRESS+ "/**").permitAll()
				.antMatchers(HttpMethod.GET, apiUser + "/{id}" + activateApiUser).permitAll()
				.antMatchers(HttpMethod.POST, apiUser + "/activationlink/resend").permitAll()
				.antMatchers(apiPassword + apiPasswordReset ).permitAll()
				.antMatchers(HttpMethod.POST, PERSON ).permitAll()
				.antMatchers(HEALTH + "/**").permitAll()
				.antMatchers(I18N + "/**").permitAll()
				.antMatchers(RECAPTCHA + "/**").permitAll()
				.antMatchers(MASTERDATA_HEALTH + "/**").permitAll()
				.antMatchers("/v2/**","/swagger-ui.html","/swagger-resources/**","/webjars/springfox-swagger-ui/**").permitAll()
				.anyRequest().authenticated();
		// @formatter:on
		httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		httpSecurity.addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
}

