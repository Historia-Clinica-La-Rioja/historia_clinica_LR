package net.pladema.user.repository;

import net.pladema.user.repository.entity.VHospitalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VHospitalUserRepository extends JpaRepository<VHospitalUser, Integer> {
}