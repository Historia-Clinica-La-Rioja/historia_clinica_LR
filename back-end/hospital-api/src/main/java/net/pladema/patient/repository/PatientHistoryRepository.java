package net.pladema.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.patient.repository.entity.PatientHistory;

@Repository
public interface PatientHistoryRepository extends JpaRepository<PatientHistory, Integer> {

}