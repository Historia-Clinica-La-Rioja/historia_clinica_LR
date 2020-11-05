package net.pladema.hl7.supporting.exchange.documents.profile;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import java.util.List;

public abstract class IDocumentProfile {

    public IDocumentProfile(){
        super();
    }

    public abstract List<Bundle.BundleEntryComponent> getContent(String id);

    public abstract List<Composition.SectionComponent> sections(
            List<Bundle.BundleEntryComponent>... entries);

}
