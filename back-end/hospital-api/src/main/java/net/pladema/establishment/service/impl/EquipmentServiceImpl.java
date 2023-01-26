package net.pladema.establishment.service.impl;

import net.pladema.establishment.repository.EquipmentRepository;
import net.pladema.establishment.repository.entity.Equipment;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.domain.EquipmentBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public  class EquipmentServiceImpl implements EquipmentService {

	private final EquipmentRepository equipmentRepository;

	private static final Logger LOG = LoggerFactory.getLogger(EquipmentServiceImpl.class);


	private static final String OUTPUT = "Output -> {}";

	public EquipmentServiceImpl(EquipmentRepository equipmentRepository){
		this.equipmentRepository = equipmentRepository;
	}

	@Override
	public List<EquipmentBO> getEquipmentBySector(Integer sectorId){

		LOG.debug("Input parameter ->, sectorId {}", sectorId);
		List <Equipment> equipments = equipmentRepository.getEquipmentBySector(sectorId);
		List<EquipmentBO> result = equipments.stream().map(this::createEquipmentBoInstance).collect(Collectors.toList());
		LOG.trace(OUTPUT, result);
		return result;

	}

	private EquipmentBO createEquipmentBoInstance(Equipment equipment) {

		LOG.debug("Input parameters -> Equipo {}", equipment);
		EquipmentBO result = new EquipmentBO();
		result.setId(equipment.getId());
		result.setSectorId(equipment.getSectorId());
		result.setName(equipment.getName());
		result.setAeTitle(equipment.getAeTitle());
		result.setModalityId(equipment.getModalityId());
		result.setOrchestratorId(equipment.getOrchestratorId());
		result.setPacServerId(equipment.getPacServerId());
		LOG.debug(OUTPUT, result);
		return result;
	}


	@Override
	public List<EquipmentBO> getEquipmentByInstitution(Integer institutionId){
		LOG.debug("Input parameter ->, institutionId {}", institutionId);
		List <Equipment> equipments = equipmentRepository.getEquipmentByInstitution(institutionId);
		List<EquipmentBO> result = equipments.stream().map(this::createEquipmentBoInstance).collect(Collectors.toList());
		LOG.trace(OUTPUT, result);
		return result;
	}
}
