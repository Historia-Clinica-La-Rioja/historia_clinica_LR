package net.pladema.sgx.actuator.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "actuator.configuration")
public class ActuatorConfiguration {

    private List<String> whiteList = new ArrayList<>();

    private boolean authenticated;

    public String getAccessInfo() {
        StringBuilder access = new StringBuilder("permitAll()");
        if (authenticated)
            access = new StringBuilder("isAuthenticated()");
        if (whiteList.isEmpty())
            return access.toString();
        access.append(" and ");
        access.append("hasIpAddress('").append(whiteList.get(0)).append("')");
        for (int i = 1; i < whiteList.size(); i++)
            access.append(" and hasIpAddress('").append(whiteList.get(i)).append("')");
        return access.toString();
    }
}