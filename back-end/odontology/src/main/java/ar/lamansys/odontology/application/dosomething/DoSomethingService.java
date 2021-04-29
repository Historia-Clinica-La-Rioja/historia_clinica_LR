package ar.lamansys.odontology.application.dosomething;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;
import ar.lamansys.sgh.shared.domain.DoSomethingBo;

public interface DoSomethingService {

    DoSomethingBo doSomething();
}
