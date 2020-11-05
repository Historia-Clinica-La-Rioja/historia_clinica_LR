package net.pladema.hl7.supporting.conformance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.validation.IValidatorModule;
import net.pladema.hl7.supporting.implementer.validating.ApiFhirInstanceValidator;
import net.pladema.hl7.supporting.implementer.validating.ApiResponseValidatingInterceptor;
import net.pladema.hl7.supporting.security.ServerAuthInterceptor;
import net.pladema.hl7.supporting.security.ApiCorsInterceptor;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Map;

@WebServlet( urlPatterns = "/fhir/*", displayName = "Fhir Server")
public class FhirServerR4 extends RestfulServer {

    private  final ApplicationContext applicationContext;

    public FhirServerR4(ApplicationContext applicationContext){
        super();
        this.applicationContext=applicationContext;
    }

    @Override
    protected void initialize() throws ServletException {
        FhirContext context = FhirContext.forR4();

        setFhirContext(context);
        this.setDefaultPrettyPrint( true );
        this.setDefaultResponseEncoding( EncodingEnum.JSON );

        //custom generate narratives
        getFhirContext().setNarrativeGenerator(new CustomThymeleafNarrativeGenerator(
                "classpath:narrative.properties"));

        // Register resource providers
        Map<String, IResourceProvider> providers = applicationContext.getBeansOfType(IResourceProvider.class);
        if(!providers.isEmpty())
            setResourceProviders(providers.values());

        //Create a FhirInstanceValidator and register it to validator
        IValidatorModule validatorModule = new ApiFhirInstanceValidator(getFhirContext());
        getFhirContext()
                .newValidator()
                .registerValidatorModule(validatorModule);

        registerInterceptor(new ApiResponseValidatingInterceptor(validatorModule));
        registerInterceptor(new ApiCorsInterceptor());
        registerInterceptor(applicationContext.getBean(ServerAuthInterceptor.class));
        registerInterceptor(new ResponseHighlighterInterceptor());
    }
}
