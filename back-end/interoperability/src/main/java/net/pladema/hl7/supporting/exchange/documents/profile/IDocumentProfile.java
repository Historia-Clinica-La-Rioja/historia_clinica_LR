package net.pladema.hl7.supporting.exchange.documents.profile;

import lombok.NoArgsConstructor;
import net.pladema.hl7.dataexchange.IMultipleResourceFhir;
import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.ResourceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public abstract class IDocumentProfile {

    Map<ResourceType, ISingleResourceFhir> includedSingleResources;
    Map<ResourceType, IMultipleResourceFhir> includedMultipleResources;

    public IDocumentProfile(List<? extends ISingleResourceFhir> singleResources,
                            List<? extends IMultipleResourceFhir> multipleResources){
        includedSingleResources = new HashMap<>();
        singleResources.forEach((resource)->
            includedSingleResources.put(resource.getResourceType(), resource)
        );

        includedMultipleResources = new HashMap<>();
        multipleResources.forEach((resource)->
            includedMultipleResources.put(resource.getResourceType(), resource)
        );
    }

    public Map<ResourceType, ? extends ISingleResourceFhir> getIncludedSingleResources(){
        return includedSingleResources;
    }

    public Map<ResourceType, ? extends IMultipleResourceFhir> getIncludedMultipleResources(){
        return includedMultipleResources;
    }

    public abstract List<Bundle.BundleEntryComponent> getContent(String id);

    public abstract List<Composition.SectionComponent> sections(
            List<Bundle.BundleEntryComponent>...entries);

}
