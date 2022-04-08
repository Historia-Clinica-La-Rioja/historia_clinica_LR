package net.pladema.snowstorm.repository;

import net.pladema.snowstorm.repository.entity.SnomedCacheLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnomedCacheLogRepository extends JpaRepository<SnomedCacheLog, Integer> {
}
