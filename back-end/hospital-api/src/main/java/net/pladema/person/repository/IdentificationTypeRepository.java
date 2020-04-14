package net.pladema.person.repository;

import net.pladema.person.repository.entity.IdentificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Short> {
}
