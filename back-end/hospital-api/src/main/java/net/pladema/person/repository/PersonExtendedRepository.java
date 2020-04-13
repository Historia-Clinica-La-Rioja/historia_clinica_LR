package net.pladema.person.repository;

import net.pladema.person.entity.PersonExtended;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonExtendedRepository extends JpaRepository<PersonExtended, Integer> {
}
