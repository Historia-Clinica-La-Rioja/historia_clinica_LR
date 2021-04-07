package net.pladema.odontology.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OdontologyRepository {

    @Value("${app.api.info}")
    private String info;

    public String getInfo(){
        return info;
    }
}
