package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.sgx.pdf.PDFDocumentException;

import java.io.IOException;

public interface CreateEpicrisisService {

    EpicrisisBo execute(Integer institutionId, EpicrisisBo epicrisis) throws IOException, PDFDocumentException;
}
