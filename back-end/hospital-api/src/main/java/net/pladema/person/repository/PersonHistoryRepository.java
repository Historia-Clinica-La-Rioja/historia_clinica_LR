package net.pladema.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.person.repository.entity.PersonHistory;

@Repository
public interface PersonHistoryRepository extends JpaRepository<PersonHistory, Integer> {

}
