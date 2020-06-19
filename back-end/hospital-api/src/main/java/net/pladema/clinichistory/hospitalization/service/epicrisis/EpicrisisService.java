package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface EpicrisisService {

    EpicrisisBo getDocument(Long documentId);
}
