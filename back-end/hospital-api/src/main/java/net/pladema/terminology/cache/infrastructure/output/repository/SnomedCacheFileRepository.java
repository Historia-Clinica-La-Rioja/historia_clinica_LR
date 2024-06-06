package net.pladema.terminology.cache.infrastructure.output.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.terminology.cache.controller.dto.ETerminologyKind;
import net.pladema.terminology.cache.infrastructure.output.repository.entity.SnomedCacheFile;


@Repository
public interface SnomedCacheFileRepository extends JpaRepository<SnomedCacheFile, Integer> {
	@Transactional(readOnly = true)
	@Query("SELECT scf FROM SnomedCacheFile scf " +
			" WHERE scf.downloadedOn IS NULL " +
			" ORDER BY scf.createdOn")
	Page<SnomedCacheFile> findToDownloadByAge(Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT scf FROM SnomedCacheFile scf " +
			" WHERE scf.downloadedOn IS NOT NULL" +
			" AND scf.fileId IS NOT NULL " +
			" AND scf.ingestedOn IS NULL " +
			" ORDER BY scf.createdOn ASC")
	Page<SnomedCacheFile> findToIngestByAge(Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT scf FROM SnomedCacheFile scf " +
			" WHERE scf.kind = :kind " +
			" AND scf.ingestedOn IS NULL " +
			" ORDER BY scf.createdOn ASC")
	List<SnomedCacheFile> findToProcessByAge(@Param("kind") ETerminologyKind kind);

	@Transactional
	@Modifying
	@Query("DELETE FROM SnomedCacheFile scf " +
			"WHERE scf.id = :terminologyId")
	void delete(@Param("terminologyId") Integer terminologyId);

	@Transactional(readOnly = true)
	@Query("SELECT scf FROM SnomedCacheFile scf WHERE scf.id = (" +
			"SELECT MAX(sc.id) FROM SnomedCacheFile sc WHERE sc.ingestedOn IS NOT NULL AND sc.ingestedError IS NULL AND sc.conceptsLoaded IS NOT NULL AND sc.ecl = scf.ecl AND sc.kind =:kind" +
			")")
	List<SnomedCacheFile> lastSuccessfulByECL(@Param("kind") ETerminologyKind kind);

}
