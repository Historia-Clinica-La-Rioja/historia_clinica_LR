package net.pladema.shared;

import ar.lamansys.sgh.shared.domain.DoSomethingBo;
import ar.lamansys.sgh.shared.domain.DoSomethingSharedPort;
import net.pladema.dosomething.infratracture.DoSomethingExternalService;
import org.springframework.stereotype.Service;

@Service
public class DoSomethingSharedAdapter implements DoSomethingSharedPort {

    private final DoSomethingExternalService doSomethingExternalService;

    public DoSomethingSharedAdapter(DoSomethingExternalService doSomethingExternalService) {
        this.doSomethingExternalService = doSomethingExternalService;
    }

    @Override
    public DoSomethingBo execute() {
        return doSomethingExternalService.doSomething();
    }
}
