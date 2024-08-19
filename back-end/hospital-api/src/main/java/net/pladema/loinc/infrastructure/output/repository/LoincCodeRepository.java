package net.pladema.loinc.infrastructure.output.repository;

import net.pladema.loinc.infrastructure.output.entity.LoincCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoincCodeRepository extends JpaRepository<LoincCode, Integer> {
	Optional<LoincCode> findByCode(String loincCode);

	List<LoincCode> findByCodeIn(List<String> codes);
}
