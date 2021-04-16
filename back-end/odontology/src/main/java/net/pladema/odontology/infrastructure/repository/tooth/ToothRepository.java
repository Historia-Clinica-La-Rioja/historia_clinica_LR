package net.pladema.odontology.infrastructure.repository.tooth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ToothRepository {

    @Value("${app.api.info}")
    private String info;

    public String getTeeth() {
        return info;
    }
}
