package net.pladema.hl7.supporting.exchange.documents.profile;

import org.hl7.fhir.r4.model.Bundle;

import java.util.List;

public interface IDocumentProfile {

    List<Bundle.BundleEntryComponent> getContent(String id);
}