package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.sgx.pdf.PDFDocumentException;

import java.io.IOException;

public interface ChangeMainDiagnosesService {

    MainDiagnosisBo execute(Integer institutionId, MainDiagnosisBo mainDiagnosisBo) throws IOException, PDFDocumentException;
}
