package net.pladema.hl7.supporting.conformance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet( urlPatterns = "/fhir/*", displayName = "Fhir Server")
public class FhirServerR4 extends RestfulServer {

    public FhirServerR4(){
        super();
    }

    @Override
    protected void initialize() throws ServletException {
        FhirContext context = FhirContext.forR4();

        setFhirContext(context);
        this.setDefaultPrettyPrint( true );
        this.setDefaultResponseEncoding( EncodingEnum.JSON );

        registerInterceptor(new ResponseHighlighterInterceptor());
    }
}
