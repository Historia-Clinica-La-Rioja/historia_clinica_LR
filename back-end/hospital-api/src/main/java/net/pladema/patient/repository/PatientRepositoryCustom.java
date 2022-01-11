package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;

import java.util.List;

public interface PatientRepositoryCustom {

    List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize, boolean filterByNameSelfDetermination);

    Long getCountByOptionalFilter(PatientSearchFilter searchFilter, boolean filterByNameSelfDetermination);
}
