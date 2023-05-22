package net.pladema.patient.repository;


import net.pladema.patient.controller.dto.MergedPatientSearchFilter;

import java.util.List;

public interface MergedPatientRepositorySearch {

	List<Integer> getAllByFilter(MergedPatientSearchFilter mergedPatientSearchFilter);

}
