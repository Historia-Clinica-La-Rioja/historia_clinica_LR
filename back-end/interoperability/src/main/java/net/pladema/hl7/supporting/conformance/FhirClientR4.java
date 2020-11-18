package net.pladema.hl7.supporting.conformance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

import net.pladema.hl7.supporting.security.ClientAuthInterceptor;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class FhirClientR4 {

    private final IFhirClient busClient;
    private final IGenericClient testClient;

    public FhirClientR4(WebApplicationContext webApplicationContext){
        super();

        FhirContext context = FhirContext.forR4();

        // Instantiate a new JSON parser
        IParser parser = context.newJsonParser();
        parser.setPrettyPrint(true);

        // Create a context and configure it for deferred child scanning
        context.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);

        // Disable server validation (don't pull the server's metadata first) where the client and server are known to be compatible
        context.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

        // Set how long to try and establish the initial TCP connection (in ms)
        context.getRestfulClientFactory().setConnectTimeout(20000);

        // Set how long to block for individual read/write operations (in ms)
        context.getRestfulClientFactory().setSocketTimeout(20000);

        // Create the client
        this.busClient = context.newRestfulClient(IFhirClient.class, CodingSystem.SERVER.BUS);


        busClient.registerInterceptor(webApplicationContext.getBean(ClientAuthInterceptor.class));

        testClient = context.newRestfulGenericClient(CodingSystem.SERVER.TESTAPP);
    }
}
