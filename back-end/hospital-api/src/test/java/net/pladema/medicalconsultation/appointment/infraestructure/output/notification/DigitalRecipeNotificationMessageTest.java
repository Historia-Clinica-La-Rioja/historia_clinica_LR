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

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import ar.lamansys.sgx.shared.templating.utils.testing.AppTemplateConfig;
import ar.lamansys.sgx.shared.templating.utils.testing.TemplateMailTestingHelper;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestNotificationArgs;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestTemplateInput;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTemplateConfig.class)
class DigitalRecipeNotificationMessageTest {
	@MockBean
	private MessageSource messageSource;
	@Autowired
	protected ApplicationContext applicationContext;

	private TemplateMailTestingHelper<NewMedicationRequestNotificationArgs> templateMailTestingHelper;

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
				new NewMedicationRequestTemplateInput(NewMedicationRequestNotificationArgs.builder()
						.build(), "subject")
		);
		assertThat(mail.subject).isNotNull();
	}

	@Test
	void requiredArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"requiredArgs",
				new NewMedicationRequestTemplateInput(NewMedicationRequestNotificationArgs.builder()
						.recipeId(1)
						.recipeIdWithDomain("id-with-domin")
						.build(), "subject")
		);
		assertThat(mail.subject).isNotNull();

	}

	@Test
	void patientArgs() throws TemplateException {
		var mail = this.templateMailTestingHelper.renderTemplate(
				"patientArgs",
				new NewMedicationRequestTemplateInput(NewMedicationRequestNotificationArgs.builder()
						.recipeId(1)
						.recipeIdWithDomain("id-with-domin")
						.patient(mockPatient())
						.build(), "subject")
		);
		assertThat(mail.subject).isNotNull();

	}

	private BasicPatientDto mockPatient() {
		return new BasicPatientDto(14, mockValidPerson(), (short)1);
	}

	private BasicDataPersonDto mockValidPerson(){
		var result = new BasicDataPersonDto();
		result.setFirstName("Gerónimo");
		result.setLastName("Carrasquill");
		result.setIdentificationType("PASAPORTE");
		result.setIdentificationNumber("13694305");
		return result;
	}
}