package net.pladema.sgh.app.seeds.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@Profile("dev")
@ConfigurationProperties(prefix = "app.data.sample")
@PropertySource(value = "classpath:sample-data.properties")
public class SampleProperties {

    private List<Person> people;

    private List<Address> addresses;

    private List<Institution> institutions;

    private List<User> users;

    private List<UserPassword> userPasswords;

    private List<UserRole> userRoles;

    private List<HealthcareProfessional> healthcareProfessionals;

    // standard getters and setters
}