package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.domain.ViolenceReportSituationBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetPatientSituationListRepository {

	Page<ViolenceReportSituationBo> getPatientSituations(Integer patientId, Boolean mustBeLimited, Pageable pageable);

}
