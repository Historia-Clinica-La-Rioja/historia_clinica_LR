package net.pladema.errata.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.errata.common.repository.entity.Errata;

import java.util.List;

@Repository
public interface ErrataRepository extends JpaRepository<Errata, Integer> {

	boolean existsByDocumentId(Integer documentId);

	Errata findByDocumentId(Integer documentId);

}
