package net.pladema.hl7.supporting.conformance;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.RestUtils;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import net.pladema.hl7.supporting.conformance.exceptions.FhirClientEnumException;
import net.pladema.hl7.supporting.conformance.exceptions.FhirClientException;
import net.pladema.hl7.supporting.security.ClientAuthInterceptor;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
@Conditional(InteroperabilityCondition.class)
public class FhirClientR4 {

    private final IFhirClient busClient;
    private final IGenericClient nomivacClient;
    private final IGenericClient federadorClient;

    public FhirClientR4(
			WebApplicationContext webApplicationContext,
			@Value("${ws.federar.url.base}") String federador,
			@Value("${ws.bus.url.base}") String bus,
			@Value("${ws.nomivac.synchronization.url.base:localhost}") String nomivac,
			@Value("${ws.nomivac.rest-client.config.trust-invalid-certificate:false}") Boolean nomivacTrustInvalidCertificate,
			HttpClientConfiguration configuration
	) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
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
        this.busClient = context.newRestfulClient(IFhirClient.class, bus);
        busClient.registerInterceptor(webApplicationContext.getBean(ClientAuthInterceptor.class));

        this.nomivacClient = context.newRestfulGenericClient(nomivac);
        nomivacClient.getFhirContext().getRestfulClientFactory().setHttpClient(RestUtils.httpClient(
				nomivacTrustInvalidCertificate,
				configuration.getProxy()
		));
        nomivacClient.registerInterceptor(webApplicationContext.getBean(ClientAuthInterceptor.class));

        federadorClient = context.newRestfulGenericClient(federador + CodingSystem.SERVER.PATIENT_SERVICE);
        federadorClient.registerInterceptor(webApplicationContext.getBean(ClientAuthInterceptor.class));
    }

     public MethodOutcome postImmunizationToNomivac(Immunization immunization) {
        return nomivacClient.create()
                .resource(immunization)
                //.prettyPrint()
                //.encodedJson()
                //.preferResponseType(OperationOutcome.class)
                .andLogRequestAndResponse(true)
                .execute();
    }

    public Bundle operationPatientLocation(StringType id) throws FhirClientException {
        Parameters parameters = new Parameters();
        parameters.addParameter(new Parameters.ParametersParameterComponent()
                .setName(Patient.SP_IDENTIFIER)
                .setValue(id));
        try {
            return federadorClient
                    .operation()
                    .onType(Patient.class)
                    .named("patient-location")
                    .withParameters(parameters)
                    .useHttpGet()
                    .returnResourceType(Bundle.class)
                    .execute();
        } catch (AuthenticationException ex) {
            throw new FhirClientException(FhirClientEnumException.BUS_AUTHENTICATION_EXCEPTION, HttpStatus.valueOf(ex.getStatusCode()), ex.getResponseBody());
        } catch (InternalErrorException ex) {
            return new Bundle();
        }
    }

    public Bundle getResourceById(IdType id){
        return busClient.getResourceById(id);
    }

    public DocumentReference readDocumentReferences(ReferenceParam subject, StringParam custodian, ReferenceParam type) {
        // Invoke the server with method Read and the given ID
        try {
            return busClient.getDocumentReference(subject, custodian, type);
        }
        catch(InvalidRequestException ex){
            throw new InvalidRequestException(ex.getMessage());
        }
        catch(InternalErrorException ex){
            return null;
        }
    }
}
