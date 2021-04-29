package net.pladema.dosomething.infratracture;

import ar.lamansys.sgh.shared.domain.DoSomethingBo;
import org.springframework.stereotype.Service;

@Service
public class DoSomethingExternalServiceImpl implements DoSomethingExternalService {
    @Override
    public DoSomethingBo doSomething() {
        return new DoSomethingBo("I did something CHANGED");
    }
}
