package ar.lamansys.refcounterref.infraestructure.output.repository.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceCounterReferenceFileRepository extends JpaRepository<ReferenceCounterReferenceFile, Integer> {
}
