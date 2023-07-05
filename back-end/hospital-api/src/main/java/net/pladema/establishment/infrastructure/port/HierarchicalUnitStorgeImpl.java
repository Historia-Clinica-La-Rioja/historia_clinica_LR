package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.application.port.HierarchicalUnitStorage;

import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HierarchicalUnitStorgeImpl implements HierarchicalUnitStorage {

	private final HierarchicalUnitStaffRepository hierarchicalUnitStaffRepository;

	@Override
	public void deleteHierarchicalUnitStaff(Integer userId) {
		hierarchicalUnitStaffRepository.deleteHierarchicalUnitStaffByUserId(userId);
	}
}
