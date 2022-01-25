package ar.lamansys.sgx.shared.restclient.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestClientMeasureRepository extends JpaRepository<RestClientMeasure, Long> {
	
}
