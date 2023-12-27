package net.pladema.violencereport.infrastructure.output.repository;

import net.pladema.violencereport.infrastructure.output.repository.embedded.ProvincialStateDevicePK;
import net.pladema.violencereport.infrastructure.output.repository.entity.ProvincialStateDevice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvincialStateDeviceRepository extends JpaRepository<ProvincialStateDevice, ProvincialStateDevicePK> {
}
