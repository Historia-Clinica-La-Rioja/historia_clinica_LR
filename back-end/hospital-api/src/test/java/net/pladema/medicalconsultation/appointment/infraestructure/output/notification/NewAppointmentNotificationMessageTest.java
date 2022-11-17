package net.pladema.medicalconsultation.appointment.infraestructure.output.notification;

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

import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import ar.lamansys.sgx.shared.templating.utils.testing.AppTemplateConfig;
import ar.lamansys.sgx.shared.templating.utils.testing.TemplateMailTestingHelper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTemplateConfig.class)
class NewAppointmentNotificationMessageTest {
	@MockBean
	private MessageSource messageSource;
	@Autowired
	protected ApplicationContext applicationContext;

	private TemplateMailTestingHelper<NewAppointmentNotificationArgs> templateMailTestingHelper;

	@BeforeEach
	void setUp() {
		when(messageSource.getMessage(
				any(),
				isNull(),
				any()
		)).thenReturn("Mail de confirmación");

		this.templateMailTestingHelper = new TemplateMailTestingHelper<>(messageSource, applicationContext);

	}

	@Test
	void emptyArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"emptyArgs",
				new NewAppointmentTemplateInput(NewAppointmentNotificationArgs.builder()
						.build())
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void requiredArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"requiredArgs",
				new NewAppointmentTemplateInput(NewAppointmentNotificationArgs.builder()
						.day("lunes 18 de abril")
						.professionalFullName("Carl Sagan")
						.time("10:15")
						.fromFullName("HSI")
						.doctorOffice("Consultorio 1")
						.build())
		);
		assertThat(mail.subject).isNotNull();

	}
}