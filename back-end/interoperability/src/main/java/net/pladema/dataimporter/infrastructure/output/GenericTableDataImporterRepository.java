package net.pladema.dataimporter.infrastructure.output;

public interface GenericTableDataImporterRepository {

	void saveGenericTableData(String tableName, Object[] data);

}
