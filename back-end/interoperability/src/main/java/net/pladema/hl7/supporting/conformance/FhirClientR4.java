package net.pladema.hl7.supporting.conformance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import net.pladema.hl7.supporting.security.ClientAuthInterceptor;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
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

    public DocumentReference readDocumentReferences(ReferenceParam subject, StringParam custodian, ReferenceParam type) {
        // Invoke the server with method Read and the given ID
        try {
            return busClient.getDocumentReference(subject, custodian, type);
        }
        catch(ResourceNotFoundException ex){
            return null;
        }
        catch(InvalidRequestException ex){
            throw new InvalidRequestException(ex.getMessage());
        }
    }

    public Bundle operationPatientLocation(StringType id){
        Parameters parameters = new Parameters();
        parameters.addParameter(new Parameters.ParametersParameterComponent()
                .setName(Patient.SP_IDENTIFIER)
                .setValue(id));
        try {
            return testClient
                    .operation()
                    .onType(Patient.class)
                    .named("patient-location")
                    .withParameters(parameters)
                    .useHttpGet()
                    .returnResourceType(Bundle.class)
                    .execute();
        } catch(InternalErrorException ex){
            return new Bundle();
        }
    }

    public Bundle getResourceById(IdType id){
        return busClient.getResourceById(id);
    }
}
