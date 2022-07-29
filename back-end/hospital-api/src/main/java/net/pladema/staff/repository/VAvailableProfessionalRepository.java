package net.pladema.staff.repository;


import net.pladema.staff.repository.entity.VAvailableProfessional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VAvailableProfessionalRepository extends JpaRepository<VAvailableProfessional, Integer> {
}
