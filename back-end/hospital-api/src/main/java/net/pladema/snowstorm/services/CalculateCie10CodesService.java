package net.pladema.snowstorm.services;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;

public interface CalculateCie10CodesService {

    String execute(String sctid, PatientInfoBo patient);

}
