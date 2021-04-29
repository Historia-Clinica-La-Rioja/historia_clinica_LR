package ar.lamansys.odontology.application.dosomething;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.DoSomethingStorage;
import ar.lamansys.odontology.domain.TeethBo;
import ar.lamansys.odontology.domain.ToothStorage;
import ar.lamansys.sgh.shared.domain.DoSomethingBo;
import org.springframework.stereotype.Service;

@Service
public class DoSomethingServiceImpl implements DoSomethingService {

    private final DoSomethingStorage doSomethingStorage;

    public DoSomethingServiceImpl(DoSomethingStorage doSomethingStorage) {
        this.doSomethingStorage = doSomethingStorage;
    }

    @Override
    public DoSomethingBo doSomething() {
        return doSomethingStorage.doSomething();
    }
}
