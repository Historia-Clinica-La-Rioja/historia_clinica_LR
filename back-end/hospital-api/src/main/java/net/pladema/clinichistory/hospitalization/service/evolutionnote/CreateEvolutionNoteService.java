package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.sgx.pdf.PDFDocumentException;

import java.io.IOException;

public interface CreateEvolutionNoteService {

    EvolutionNoteBo execute(EvolutionNoteBo evolutionNoteBo) throws IOException, PDFDocumentException;

}
