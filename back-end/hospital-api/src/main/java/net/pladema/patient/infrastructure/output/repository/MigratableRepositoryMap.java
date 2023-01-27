package net.pladema.patient.infrastructure.output.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AllergyIntoleranceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ImmunizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.IndicationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationLabRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ObservationRiskFactorRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyDiagnosticRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.OdontologyProcedureRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntityRepository;

import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MigratableRepositoryMap {

	private final Map<Class, SGXDocumentEntityRepository> repositoryMap;

	public MigratableRepositoryMap(
			HealthConditionRepository healthConditionRepository,
			AllergyIntoleranceRepository allergyIntoleranceRepository,
			ImmunizationRepository immunizationRepository,
			MedicationStatementRepository medicationStatementRepository,
			ProceduresRepository proceduresRepository,
			OdontologyDiagnosticRepository odontologyDiagnosticRepository,
			OdontologyProcedureRepository odontologyProcedureRepository,
			ObservationRiskFactorRepository observationRiskFactorRepository,
			ObservationLabRepository observationLabRepository,
			DiagnosticReportRepository diagnosticReportRepository,
			IndicationRepository indicationRepository,
			AppointmentRepository appointmentRepository) {
		repositoryMap = new HashMap<>();
		repositoryMap.put(HealthConditionRepository.class, healthConditionRepository);
		repositoryMap.put(AllergyIntoleranceRepository.class, allergyIntoleranceRepository);
		repositoryMap.put(ImmunizationRepository.class, immunizationRepository);
		repositoryMap.put(MedicationStatementRepository.class, medicationStatementRepository);
		repositoryMap.put(ProceduresRepository.class, proceduresRepository);
		repositoryMap.put(OdontologyDiagnosticRepository.class, odontologyDiagnosticRepository);
		repositoryMap.put(OdontologyProcedureRepository.class, odontologyProcedureRepository);
		repositoryMap.put(ObservationRiskFactorRepository.class, observationRiskFactorRepository);
		repositoryMap.put(ObservationLabRepository.class, observationLabRepository);
		repositoryMap.put(DiagnosticReportRepository.class, diagnosticReportRepository);
		repositoryMap.put(IndicationRepository.class, indicationRepository);
		repositoryMap.put(AppointmentRepository.class, appointmentRepository);
	}

	public <T extends SGXDocumentEntityRepository> SGXDocumentEntityRepository get(Class<T> clazz){
		return repositoryMap.get(clazz);
	}
}
