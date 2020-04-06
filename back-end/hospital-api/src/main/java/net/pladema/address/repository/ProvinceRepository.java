package net.pladema.address.repository;

import net.pladema.address.repository.entity.Province;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Short> {

	@Transactional(readOnly = true)
	<T> Collection<T> findAllProjectedBy(Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT p FROM Province as p WHERE p.countryId = :countryId")
	<T> Collection<T> findByCountry(@Param("countryId") Short countryId, Sort by, Class<T> clazz);

}
