package net.pladema.establishment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.Bed;

@Repository
public interface BedRepository extends JpaRepository<Bed, Integer> {

}
