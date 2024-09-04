package net.pladema.imagenetwork.derivedstudies.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedResultStudiesPort;
import net.pladema.imagenetwork.derivedstudies.service.domain.ResultStudiesBO;

public interface ResultStudiesService extends SharedResultStudiesPort {
	Integer save(ResultStudiesBO resultStudiesBO);

}
