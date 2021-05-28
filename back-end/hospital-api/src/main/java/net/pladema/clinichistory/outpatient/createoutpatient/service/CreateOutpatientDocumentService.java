package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.sgx.pdf.PDFDocumentException;

import java.io.IOException;

public interface CreateOutpatientDocumentService {
    
    OutpatientDocumentBo execute(OutpatientDocumentBo outpatient) throws IOException, PDFDocumentException;
}
