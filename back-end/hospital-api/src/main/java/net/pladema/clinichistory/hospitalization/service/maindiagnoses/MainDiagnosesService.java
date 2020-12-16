package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.patient.controller.dto.BasicPatientDto;

public interface MainDiagnosesService {

    Long createDocument(Integer internmentEpisodeId, PatientInfoBo patientInfo, MainDiagnosisBo mainDiagnoseBo);
}
