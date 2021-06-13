//
// Operation outcomes are sets of error, warning and information messages that provide detailed information about the outcome
// of an attempted system operation. They are provided as a direct system response, or component of one, and provide information
// about the outcome of the operation.
//

package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.OperationOutcome;

public interface IResponseOperationOutcome {

    default void badRequest(String diagnostics) {
        OperationOutcome exception = new OperationOutcome();
        exception.addIssue()
                .setSeverity(OperationOutcome.IssueSeverity.ERROR)
                .setCode(OperationOutcome.IssueType.PROCESSING)
                .setDiagnostics(diagnostics);
        throw new InvalidRequestException("Invalid parameter", exception);
    }

    default Bundle.BundleEntryComponent notFound(String message){
        OperationOutcome warning = new OperationOutcome();
        warning.setId("warning");
        warning.addIssue()
                .setSeverity(OperationOutcome.IssueSeverity.WARNING)
                .setCode(OperationOutcome.IssueType.NOTFOUND)
                .setDetails(new CodeableConcept().addCoding(new Coding().setDisplay(message)));
        return new Bundle.BundleEntryComponent()
                .setResource(warning)
                .setSearch(new Bundle.BundleEntrySearchComponent().setMode(Bundle.SearchEntryMode.OUTCOME));
    }
}
