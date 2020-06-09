package net.pladema.seeds.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.address.repository.entity.Address;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "app.data.sample")
@PropertySource(value = "classpath:application-dev.properties")
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