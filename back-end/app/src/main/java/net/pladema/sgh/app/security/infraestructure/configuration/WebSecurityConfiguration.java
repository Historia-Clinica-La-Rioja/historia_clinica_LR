package net.pladema.sgh.app.security.infraestructure.configuration;

import static net.pladema.sgh.app.security.infraestructure.filters.PublicApiAuthenticationFilter.PUBLIC_API_CONTEXT_MATCHER;

import java.util.stream.Stream;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.filter.AuthenticationTokenFilter;
import ar.lamansys.sgx.auth.oauth.infrastructure.input.OAuth2AuthenticationFilter;
import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.ActuatorConfiguration;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgh.app.security.infraestructure.filters.AuthorizationFilter;
import net.pladema.sgh.app.security.infraestructure.filters.PublicApiAuthenticationFilter;
import net.pladema.sgh.app.security.infraestructure.filters.TwoFactorAuthenticationFilter;
import net.pladema.user.controller.filters.BackofficeRolesFilter;

@Configuration
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String RECAPTCHA = "/recaptcha";

	private static final String PASSWORD_RESET = "/password-reset";
	
	private static final String BACKOFFICE = "/backoffice";

	private static final String PUBLIC = "/public";

	private static final String[] SWAGGER_RESOURCES = {
			"/v3/**",
			"/swagger-ui/**"
	};

	private static final String[] PUBLIC_DOCUMENT_RESOURCES = {
			"/external-document-access/**"
	};

	private String[] BOOKING_API_RESOURCES = new String[]{};

	private final ActuatorConfiguration actuatorConfiguration;

	private final AuthenticationTokenFilter authenticationTokenFilter;

	private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;

	private final PublicApiAuthenticationFilter publicApiAuthenticationFilter;

	private final AuthorizationFilter authorizationFilter;

	private final TwoFactorAuthenticationFilter twoFactorAuthenticationFilter;

	public WebSecurityConfiguration(
			ActuatorConfiguration actuatorConfiguration,
			FeatureFlagsService featureFlagsService,
			//
			AuthenticationTokenFilter authenticationTokenFilter,
			OAuth2AuthenticationFilter oAuth2AuthenticationFilter,
			PublicApiAuthenticationFilter publicApiAuthenticationFilter,
			AuthorizationFilter authorizationFilter,
			TwoFactorAuthenticationFilter twoFactorAuthenticationFilter
	) {
		this.actuatorConfiguration = actuatorConfiguration;
		// filters
		this.authenticationTokenFilter = authenticationTokenFilter;
		this.oAuth2AuthenticationFilter = oAuth2AuthenticationFilter;
		this.publicApiAuthenticationFilter = publicApiAuthenticationFilter;
		this.authorizationFilter = authorizationFilter;
		this.twoFactorAuthenticationFilter = twoFactorAuthenticationFilter;

		if (featureFlagsService.isOn(AppFeature.LIBERAR_API_RESERVA_TURNOS))
				this.BOOKING_API_RESOURCES =  new String[]{
					"/public-api/appointment/booking/**",
					"/public-api/institution/**/appointment/booking/**",
					"/public-api/institution/**/appointment/booking/professional/**",
					"/public-api/institution/**/appointment/**"};
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		String[] publicApiRolesAuthorities = Stream.concat(
				Stream.of(ERole.API_CONSUMER),
				BackofficeRolesFilter.PUBLIC_API_ROLES.stream()
			)
			.map(ERole::getValue)
			.toArray(String[]::new);


		// @formatter:off
		httpSecurity.csrf().disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
				.antMatchers("/actuator/health").permitAll()
				.antMatchers("/actuator/env/**").hasAnyAuthority(
						ERole.ROOT.getValue(),
						ERole.ADMINISTRADOR.getValue())
				.antMatchers("/actuator/**").access(actuatorConfiguration.getAccessInfo())
				.antMatchers( "/auth/**").permitAll()
				.antMatchers(SWAGGER_RESOURCES).permitAll()
				.antMatchers(PUBLIC_DOCUMENT_RESOURCES).permitAll()
				.antMatchers(BACKOFFICE + "/properties").hasAnyAuthority(
						ERole.ROOT.getValue(),
						ERole.ADMINISTRADOR.getValue())
				.antMatchers(BACKOFFICE + "/**").hasAnyAuthority(
					ERole.ROOT.getValue(),
					ERole.ADMINISTRADOR.getValue(),
					ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.getValue(),
					ERole.ADMINISTRADOR_DE_ACCESO_DOMINIO.getValue(),
					ERole.AUDITORIA_DE_ACCESO.getValue(),
					ERole.ADMINISTRADOR_DE_DATOS_PERSONALES.getValue()
				)
				.antMatchers(RECAPTCHA + "/**").permitAll()
				.antMatchers("/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET,PUBLIC + "/**").permitAll()
				.antMatchers(HttpMethod.POST, PASSWORD_RESET).permitAll()
				.antMatchers(HttpMethod.GET, "/bed/reports/**").permitAll()
				.antMatchers(HttpMethod.GET, "/assets/**").permitAll()
				.antMatchers("/fhir/**").permitAll()
				.antMatchers(BOOKING_API_RESOURCES).permitAll()
				.antMatchers("/public-api/digital-signature/callback/**").permitAll()
				.antMatchers(PUBLIC_API_CONTEXT_MATCHER).hasAnyAuthority(publicApiRolesAuthorities)
				.antMatchers("/**").authenticated()
		.anyRequest().authenticated();

		// @formatter:on
		httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		httpSecurity.addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.addFilterAfter(oAuth2AuthenticationFilter, AuthenticationTokenFilter.class);
		httpSecurity.addFilterAfter(publicApiAuthenticationFilter, OAuth2AuthenticationFilter.class);
		httpSecurity.addFilterAfter(authorizationFilter, PublicApiAuthenticationFilter.class);
		httpSecurity.addFilterAfter(twoFactorAuthenticationFilter, AuthorizationFilter.class);
	}

}

