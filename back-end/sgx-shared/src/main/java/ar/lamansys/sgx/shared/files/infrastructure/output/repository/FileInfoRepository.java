package ar.lamansys.sgx.shared.files.infrastructure.output.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

@Repository
public interface FileInfoRepository extends SGXAuditableEntityJPARepository<FileInfo, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE FileInfo e  "
			+ "SET e.deleteable.deleted = true "
			+ ", e.deleteable.deletedOn = CURRENT_TIMESTAMP "
			+ ", e.deleteable.deletedBy = ?#{ principal.userId } "
			+ "WHERE e.relativePath = :relativePath ")
	void deleteByRelativePath(@Param("relativePath") String relativePath);

	@Query("SELECT e from FileInfo e "
			+ " WHERE e.relativePath = :relativePath ")
	Optional<FileInfo> findByRelativePath(@Param("relativePath") String relativePath);
}