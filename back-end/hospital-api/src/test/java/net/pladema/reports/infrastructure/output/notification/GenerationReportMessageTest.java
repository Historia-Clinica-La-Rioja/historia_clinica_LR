package net.pladema.reports.infrastructure.output.notification;

import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;
import ar.lamansys.sgx.shared.notifications.templating.utils.testing.AppTemplateConfig;

import ar.lamansys.sgx.shared.notifications.templating.utils.testing.TemplateMailTestingHelper;

import net.pladema.reports.domain.GenerationReportNotificationArgs;
import net.pladema.reports.domain.notification.GenerationReportTemplateInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTemplateConfig.class)
class GenerationReportMessageTest {

    @MockBean
    private MessageSource messageSource;

    @Autowired
    protected ApplicationContext applicationContext;

    private TemplateMailTestingHelper<GenerationReportNotificationArgs> templateMailTestingHelper;

    @BeforeEach
    void setUp() {
        when(messageSource.getMessage(
                any(),
                isNull(),
                any())
        ).thenReturn("Mail de confirmación");
        this.templateMailTestingHelper = new TemplateMailTestingHelper<>(messageSource, applicationContext);
    }

    @Test
    void emptyArgs() throws TemplateException {
        var mail = this.templateMailTestingHelper.renderTemplate(
                "emptyArgs",
                new GenerationReportTemplateInput(GenerationReportNotificationArgs.builder().build(), "a subject")
        );
        assertThat(mail.subject).isNotNull();
    }

    @Test
    void requiredArgs() throws TemplateException {
        var mail = this.templateMailTestingHelper.renderTemplate(
                "requiredArgs",
                new GenerationReportTemplateInput(GenerationReportNotificationArgs.builder()
                        .reportType("Reporte de prueba")
                        .createdOn(LocalDateTime.of(2024,2,1,16,44,0))
                        .build(), "a subject")
        );
        assertThat(mail.subject).isNotNull();
    }

}

