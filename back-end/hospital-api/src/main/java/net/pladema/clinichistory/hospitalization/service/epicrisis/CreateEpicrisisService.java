package net.pladema.clinichistory.hospitalization.service.epicrisis;

import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

public interface CreateEpicrisisService {

    EpicrisisBo execute(EpicrisisBo epicrisis, boolean draft);
}
