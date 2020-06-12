package net.pladema.internation.service.internment.maindiagnoses;

import net.pladema.internation.service.internment.maindiagnoses.domain.MainDiagnosisBo;

public interface MainDiagnosesService {

    Long createDocument(Integer internmentEpisodeId, Integer patientId, MainDiagnosisBo mainDiagnoseBo);
}
