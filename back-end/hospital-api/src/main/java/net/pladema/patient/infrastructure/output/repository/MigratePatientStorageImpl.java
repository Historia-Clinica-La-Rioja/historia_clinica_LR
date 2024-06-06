package net.pladema.patient.infrastructure.output.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import net.pladema.patient.application.port.MigratePatientStorage;
import net.pladema.patient.infrastructure.output.repository.entity.EMergeTable;

import net.pladema.patient.infrastructure.output.repository.entity.MergedPatientItem;

import net.pladema.violencereport.application.UpdateOldSituationIds;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MigratePatientStorageImpl implements MigratePatientStorage {

	@PersistenceContext
	private final EntityManager entityManager;
	private final UpdateOldSituationIds updateOldSituationIds;
	private final MergedPatientItemRepository mergedPatientItemRepository;
	private final SharedDocumentPort sharedDocumentPort;

	@Modifying
	public void migrateItem(Integer id, Integer oldPatientId, Integer newPatientId, EMergeTable table) {
		log.debug("Input parameters -> itemId {}, oldPatientId {}, newPatientId {}, table {}", id, oldPatientId, newPatientId, table);
		migrate(id,newPatientId, table.getTableName());
		mergedPatientItemRepository.save(new MergedPatientItem(table.getTableName(),table.getColumnIdName(),id,oldPatientId,newPatientId));
	}

	@Override
	public void undoMigrateByInactivePatient(Integer inactivePatientId) {
		log.debug("Input parameters -> inactivePatientId {}", inactivePatientId);
		List<MergedPatientItem> mpis = mergedPatientItemRepository.findAllByInactivePatientId(inactivePatientId);
		mpis.forEach(mpi -> migrate(mpi.getMergedIdValue(), mpi.getOldPatientId(), mpi.getMergedTableName()));
		mergedPatientItemRepository.deleteAll(mpis);
		updateOldSituationIds.run(inactivePatientId);
		sharedDocumentPort.rebuildFilesFromPatient(inactivePatientId);
	}


	private void migrate(Integer id, Integer newPatientId, String tableName){
		String query = "UPDATE " + tableName +
				" SET patient_id = :newPatientId" +
				" WHERE id = :id";
		entityManager.createNativeQuery(query)
				.setParameter("id", id)
				.setParameter("newPatientId", newPatientId)
				.executeUpdate();
		entityManager.flush();
		entityManager.clear();
	}


}
