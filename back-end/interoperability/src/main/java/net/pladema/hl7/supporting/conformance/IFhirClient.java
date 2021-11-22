package net.pladema.hl7.supporting.conformance;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Immunization;

/**
* This is a simple client interface. It can have many methods for various searches
*/
public interface IFhirClient extends IRestfulClient {

     /**
     * The "@Search" annotation indicates that this method supports the
     * search operation. This operation takes three parameters which is the search criteria. It is
     *  annotated with the "@Required" and @Optional annotation.
     * @see <a href="https://www.hl7.org/fhir/search.html">info</a>
     * @param subject the Patient resource that the report is about
     * @param custodian represented custodian organization
     * @param type document identifier
     * @return
     *    This method returns a list of DocumentReference. This list may contain multiple
     *    matching resources, or it may also be empty.
     */
    @Search
    DocumentReference getDocumentReference(
            @RequiredParam(name = "subject:identifier") ReferenceParam subject,
            @RequiredParam(name = "custodian") StringParam custodian,
            @OptionalParam(name = "type") ReferenceParam type);

    /**
     * The "@Read" annotation indicates that this method supports the
     * read operation. Read operations should return a single resource
     * instance.
     * @param theId
     *    The read operation takes one parameter, which must be of type
     *    IdType and must be annotated with the "@Read.IdParam" annotation.
     * @return
     *    Returns a resource matching this identifier, or null if none exists.
     */
    @Read
    Bundle getResourceById(@IdParam IIdType theId);

    @Create
    MethodOutcome postImmunizationToNomivac(@ResourceParam Immunization immunization);

}
