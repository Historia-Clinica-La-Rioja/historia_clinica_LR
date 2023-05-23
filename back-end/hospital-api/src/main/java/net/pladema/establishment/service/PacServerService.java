package net.pladema.establishment.service;


import net.pladema.establishment.service.domain.PacServerBO;

import java.util.List;

public interface PacServerService {

	PacServerBO getPacServer(Integer pacServerId);

	List<PacServerBO> getAllPacServer();
}
