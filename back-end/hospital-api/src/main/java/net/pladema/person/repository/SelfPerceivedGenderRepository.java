package net.pladema.person.repository;

import net.pladema.person.repository.entity.SelfPerceivedGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SelfPerceivedGenderRepository extends JpaRepository<SelfPerceivedGender, Short> {

    @Transactional(readOnly = true)
    @Query("  SELECT spg " +
            " FROM SelfPerceivedGender spg " +
            " ORDER BY spg.orden")
    List<SelfPerceivedGender> getSelfPerceivedGenderOrderByOrden();
}
