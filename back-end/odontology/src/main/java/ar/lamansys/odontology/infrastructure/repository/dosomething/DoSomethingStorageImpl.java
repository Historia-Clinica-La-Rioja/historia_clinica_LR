package ar.lamansys.odontology.infrastructure.repository.dosomething;

import ar.lamansys.odontology.domain.DoSomethingStorage;
import ar.lamansys.sgh.shared.domain.DoSomethingBo;
import ar.lamansys.sgh.shared.domain.DoSomethingSharedPort;
import org.springframework.stereotype.Service;

@Service
public class DoSomethingStorageImpl implements DoSomethingStorage {

    private final DoSomethingSharedPort doSomethingSharedPort;

    public DoSomethingStorageImpl(DoSomethingSharedPort doSomethingSharedPort) {
        this.doSomethingSharedPort = doSomethingSharedPort;
    }

    @Override
    public DoSomethingBo doSomething() {
        return doSomethingSharedPort.execute();
    }
}
