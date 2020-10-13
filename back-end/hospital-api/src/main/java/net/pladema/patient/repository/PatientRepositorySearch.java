package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;

import java.util.List;

public interface PatientRepositorySearch {
    List<PatientSearch> getAllByFilter(PatientSearchFilter searchFilter);
}
