package ar.lamansys.sgx.shared.actuator.infrastructure.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(ActuatorConfiguration.class);

    private List<String> whiteList = new ArrayList<>();

    private boolean authenticated;

    public String getAccessInfo() {
        StringBuilder access = new StringBuilder("");
        if (authenticated)
            access.append("isAuthenticated()");
        else
            access.append("permitAll()");
        if (whiteList.isEmpty())
            return access.toString();
        access.append(" and ( ");
        access.append("hasIpAddress('").append(whiteList.get(0)).append("')");
        for (int i = 1; i < whiteList.size(); i++)
            access.append(" or hasIpAddress('").append(whiteList.get(i)).append("')");
        access.append(" )");
        LOG.info("Actuator security list {}", access);
        return access.toString();
    }
}