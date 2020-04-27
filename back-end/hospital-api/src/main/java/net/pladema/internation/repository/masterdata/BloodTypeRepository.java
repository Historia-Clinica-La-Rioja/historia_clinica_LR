package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodTypeRepository extends JpaRepository<BloodType, Short> {

}
