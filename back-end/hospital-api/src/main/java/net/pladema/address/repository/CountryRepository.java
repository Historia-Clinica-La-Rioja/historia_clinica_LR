package net.pladema.address.repository;

import net.pladema.address.repository.entity.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface CountryRepository extends JpaRepository<Country, Short> {

	@Transactional(readOnly = true)
	<T> Collection<T> findAllProjectedBy(Sort by, Class<T> clazz);

}
