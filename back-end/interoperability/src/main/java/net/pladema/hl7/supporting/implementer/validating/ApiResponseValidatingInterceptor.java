package net.pladema.hl7.supporting.implementer.validating;

import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.validation.IValidatorModule;
import ca.uhn.fhir.validation.ResultSeverityEnum;

@Interceptor
public class ApiResponseValidatingInterceptor extends ResponseValidatingInterceptor {

    public ApiResponseValidatingInterceptor(IValidatorModule theModule){
        super();
        addValidatorModule(theModule);
        setFailOnSeverity(ResultSeverityEnum.ERROR);
        setAddResponseHeaderOnSeverity(ResultSeverityEnum.WARNING);
        setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
        setResponseHeaderValueNoIssues("No issues detected");
    }
}
