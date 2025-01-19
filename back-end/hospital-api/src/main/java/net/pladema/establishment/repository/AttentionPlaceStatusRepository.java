package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.AttentionPlaceStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttentionPlaceStatusRepository extends JpaRepository<AttentionPlaceStatus, Integer> {
}
