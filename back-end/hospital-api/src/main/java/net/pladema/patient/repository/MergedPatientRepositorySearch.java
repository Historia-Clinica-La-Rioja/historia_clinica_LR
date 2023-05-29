package net.pladema.patient.repository;


import net.pladema.patient.controller.dto.MergedPatientSearchFilter;
import net.pladema.patient.service.domain.MergedPatientSearch;

import java.util.List;

public interface MergedPatientRepositorySearch {

	List<MergedPatientSearch> getAllByFilter(MergedPatientSearchFilter mergedPatientSearchFilter);

}
