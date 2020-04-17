package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.InternmentEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternmentEpisodeRepository extends JpaRepository<InternmentEpisode, Integer> {

}
