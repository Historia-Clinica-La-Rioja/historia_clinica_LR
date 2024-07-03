package net.pladema.user.infrastructure.output.notification;

import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import ar.lamansys.sgx.shared.notifications.templating.utils.testing.AppTemplateConfig;

import ar.lamansys.sgx.shared.notifications.templating.utils.testing.TemplateMailTestingHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTemplateConfig.class)
public class RestorePasswordNotificationMessageTest {

	@MockBean
	private MessageSource messageSource;

	@Autowired
	protected ApplicationContext applicationContext;

	private TemplateMailTestingHelper<RestorePasswordNotificationArgs> templateMailTestingHelper;


	@BeforeEach
	void setUp() {
		when(messageSource.getMessage(any(), isNull(), any())).thenReturn("Mail de confirmación");
		this.templateMailTestingHelper = new TemplateMailTestingHelper<>(messageSource, applicationContext);
	}

	@Test
	void emptyArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"emptyArgs",
				new RestorePasswordTemplateInput(RestorePasswordNotificationArgs.builder()
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void requiredArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"requiredArgs",
				new RestorePasswordTemplateInput(RestorePasswordNotificationArgs.builder()
						.fullname("Nombre completo del usuario")
						.link("/un-path/absoluto")
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void requiredArgsPladema() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				TemplateMailTestingHelper.Domain.PLADEMA,
				"requiredArgsPladema",
				new RestorePasswordTemplateInput(RestorePasswordNotificationArgs.builder()
						.fullname("Nombre completo del usuario")
						.link(RestorePasswordNotificationImpl.ROUTE + "T0K3N")
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

}
