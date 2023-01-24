package net.pladema.patient.application.port;

import net.pladema.patient.infrastructure.output.repository.entity.EMergeTable;

public interface MigratePatientStorage {

	void migrateItem(Integer id, Integer oldPatientId, Integer newPatientId, EMergeTable table);

	void undoMigrateByInactivePatient(Integer inactivePatientId);
}
