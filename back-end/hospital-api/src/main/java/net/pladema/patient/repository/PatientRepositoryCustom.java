package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.domain.PatientSearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientRepositoryCustom {

    Page<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Pageable pageable);

	List<Patient> getLongTermTemporaryPatientIds(LocalDateTime maxDate, Short limit);
}
