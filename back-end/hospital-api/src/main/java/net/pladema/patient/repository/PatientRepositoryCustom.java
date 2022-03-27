package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;

import java.util.List;

public interface PatientRepositoryCustom {

    List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize);

    Integer getCountByOptionalFilter(PatientSearchFilter searchFilter);
}
