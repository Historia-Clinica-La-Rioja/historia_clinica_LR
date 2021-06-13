//
//Document End-Points
//

package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fhir")
@Conditional(InteroperabilityCondition.class)
public class BundleProvider implements IResourceProvider {

    private final BundleResource bundleResource;

    public BundleProvider(BundleResource bundleResource){
        this.bundleResource = bundleResource;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Bundle.class;
    }

    /**
     * @param id document identifier (Property content.attachment.url of DocumentReference)
     * @return fhir document (special Bundle)
     */
    @GetMapping(value = "/Bundle/{id}")
    @Read
    public Bundle assembleDocument(@IdParam IdType id){
        return bundleResource.assembleDocument(id);
    }
}
