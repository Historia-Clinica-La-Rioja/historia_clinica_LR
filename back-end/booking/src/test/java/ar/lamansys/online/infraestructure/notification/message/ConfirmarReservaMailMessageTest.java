package ar.lamansys.online.infraestructure.notification.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.MailTemplateEngine;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import ar.lamansys.sgx.shared.templating.utils.testing.TemplateMailTestingHelper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class ConfirmarReservaMailMessageTest {
	@MockBean
	private MessageSource messageSource;
	@Autowired
	protected ApplicationContext applicationContext;

	private TemplateMailTestingHelper<ConfirmarReservaNotificationArgs> templateMailTestingHelper;

	@BeforeEach
	void setUp() {
		when(messageSource.getMessage(
				any(),
				isNull(),
				any()
		)).thenReturn("Mail de reserva");

		this.templateMailTestingHelper = new TemplateMailTestingHelper<>(messageSource, applicationContext);
	}

	@Test
	void emptyArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"emptyArgs",
				new ConfirmarReservaTemplateInput(ConfirmarReservaNotificationArgs.builder()
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void requiredArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"requiredArgs",
				new ConfirmarReservaTemplateInput(ConfirmarReservaNotificationArgs.builder()
						.cancelationLink("http://localhost:4700/link?code=uuid-uuid-uuh")
						.date("3/12/2023 a las 18:00 h")
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void allArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"allArgs",
				new ConfirmarReservaTemplateInput(ConfirmarReservaNotificationArgs.builder()
						.cancelationLink("http://localhost:4700/link?code=uuid-uuid-uuh")
						.namePatient("Hugo Martinez")
						.date("13/12/2023 a las 18:00 h")
						.nameProfessional("Martina Hugarte")
						.specialty("Alergia")
						.institution("CAPS 11, Avellaneda 123")
						.recomendation("Llegar 15 minutos antes")
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

}