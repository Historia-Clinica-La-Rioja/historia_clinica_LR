package net.pladema.dataimporter.infrastructure.output;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@Repository
@Slf4j
public class GenericTableDataImporterRepositoryImpl implements GenericTableDataImporterRepository {

	private final EntityManager entityManager;

	public GenericTableDataImporterRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Transactional
	@Override
	public void saveGenericTableData(String tableName, Object[] data) {
		log.debug("Input parameters -> tableName {}, data {}", tableName, data);
		String tableData = tableName.concat((String) data[0]);
		for (String row : (List<String>) data[1]) {
			String query = "INSERT INTO {h-schema}" + tableData + " VALUES " + row;
			entityManager.createNativeQuery(query).executeUpdate();
		}
	}

}
