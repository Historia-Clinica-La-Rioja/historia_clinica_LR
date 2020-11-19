package net.pladema.clinichistory.hospitalization.service.anamnesis;

import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.sgx.pdf.PDFDocumentException;

import java.io.IOException;

public interface CreateAnamnesisService {

    AnamnesisBo createDocument(Integer institutionId, AnamnesisBo anamnesis) throws IOException, PDFDocumentException;
}
