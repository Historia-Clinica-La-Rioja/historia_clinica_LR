package net.pladema.establishment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.establishment.service.PacServerService;
import net.pladema.establishment.service.domain.PacServerBO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacServerServiceImpl implements PacServerService {

	private final PacServerRepository pacServerRepository;
	public PacServerBO getPacServer(Integer pacServerId){
		PacServer pacServer = pacServerRepository.getById(pacServerId);
		return pacServer != null ? createPacServerBoInstance(pacServer):null;
	}

	@Override
	public List<PacServerBO> getAllPacServer() {
		List<PacServer> pacServer = pacServerRepository.findAll();
		List<PacServerBO> result = pacServer.stream().map(this::createPacServerBoInstance).collect(Collectors.toList());
		return result;
	}

	private PacServerBO createPacServerBoInstance(PacServer pacServer){

		log.debug("Input parameters -> PacServer {}", pacServer);

		PacServerBO pacServerBO = new PacServerBO();
		pacServerBO.setId(pacServer.getId());
		pacServerBO.setName(pacServer.getName());
		pacServerBO.setDomanin(pacServer.getDomain());

		log.debug("Output -> PacServerBO {}", pacServerBO);

		return pacServerBO;
	}
}
