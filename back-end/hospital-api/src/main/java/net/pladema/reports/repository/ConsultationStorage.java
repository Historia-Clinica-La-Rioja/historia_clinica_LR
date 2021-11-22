package net.pladema.reports.repository;

import java.util.List;

public interface ConsultationStorage {

    List<ConsultationsVo> fetchAllByPatientId(Integer patientId);
}
