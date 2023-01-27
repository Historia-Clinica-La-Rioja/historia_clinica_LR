package ar.lamansys.sgx.shared.migratable;

import java.util.List;

public interface SGXDocumentEntityRepository<T extends SGXDocumentEntity> {

	List<T> getEntitiesByDocuments(List<Long> documentIds);

}
