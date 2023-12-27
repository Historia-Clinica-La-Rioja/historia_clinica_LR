package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.NationalStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.NationalStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalStateDeviceRepository extends JpaRepository<NationalStateDevice, NationalStateDevicePK> {
}
