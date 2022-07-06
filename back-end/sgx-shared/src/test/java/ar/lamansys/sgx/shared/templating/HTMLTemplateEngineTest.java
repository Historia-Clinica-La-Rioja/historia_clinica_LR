package ar.lamansys.sgx.shared.templating;

import static ar.lamansys.sgx.shared.templating.utils.testing.TemplateTestingUtils.classpathFileContent;
import static org.assertj.core.api.Assertions.assertThat;
import static ar.lamansys.sgx.shared.templating.SpringTemplateUtils.createHtmlTemplateEngine;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.Map;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import lombok.AllArgsConstructor;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class HTMLTemplateEngineTest {
	private static RecipientBo RECIPIENT = new RecipientBo("Neil", "deGrasse Tyson", "neil@lamansys.com", "+5492494321098");
	@Autowired
	protected ApplicationContext applicationContext;
	private HTMLTemplateEngine templateEngine;

	@BeforeEach
	void setUp() {
		this.templateEngine = new HTMLTemplateEngine(
				() -> new NotificationEnv("https://hsi-domain.com.ar"),
				createHtmlTemplateEngine(
						"classpath:/templates/notifications/",
						applicationContext
				)
		);
	}

	@Test
	void process_template1_basic() throws TemplateException {
		assertThat(
				templateEngine.process(
						RECIPIENT,
						new NotificationTemplateInput<>("template1", new TestTemplateDto(
								"Nice to see you here",
								null
						),
								AppFeature.HABILITAR_NOTIFICACIONES_TURNOS)
				)
		).isEqualTo(classpathFileContent("templates/test/template1-basic.html"));
	}

	@Test
	void process_template2_basic() throws TemplateException {
		assertThat(
				templateEngine.process(
						RECIPIENT,
						new NotificationTemplateInput<>("template2", new TestTemplateDto(
								"Nice to see you here",
								null
						), AppFeature.HABILITAR_NOTIFICACIONES_TURNOS)
				)
		).isEqualTo(classpathFileContent("templates/test/template2-basic.html"));
	}

	@Test
	void process_template2_full() throws TemplateException {
		assertThat(
				templateEngine.process(
						RECIPIENT,
						new NotificationTemplateInput<>("template2", new TestTemplateDto(
								"Nice to see you here",
								"Institución 1"
						), AppFeature.HABILITAR_NOTIFICACIONES_TURNOS)
				)
		).isEqualTo(classpathFileContent("templates/test/template2-full.html"));
	}
}

@Configuration
@ComponentScan("ar.lamansys.online.infraestructure.notification.message")
class AppConfig {
}

@AllArgsConstructor
class TestTemplateDto {
	public final String message;
	public final String institution;
}