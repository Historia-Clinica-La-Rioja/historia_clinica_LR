package ar.lamansys.refcounterref.infraestructure.output.repository.counterreference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterReferenceRepository extends JpaRepository<CounterReference, Integer> {
}