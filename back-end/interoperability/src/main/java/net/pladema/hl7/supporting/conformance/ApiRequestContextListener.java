package net.pladema.hl7.supporting.conformance;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.annotation.WebListener;

@Configuration
@WebListener
public class ApiRequestContextListener extends RequestContextListener {
}
