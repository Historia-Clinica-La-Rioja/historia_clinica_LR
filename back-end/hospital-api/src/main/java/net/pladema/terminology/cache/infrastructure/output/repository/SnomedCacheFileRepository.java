package net.pladema.terminology.cache.infrastructure.output.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
			" WHERE scf.ingestedOn IS NULL " +
			" OR scf.ingestedError IS NOT NULL " +
			" ORDER BY scf.createdOn ASC")
	List<SnomedCacheFile> findToProcessByAge();

}
