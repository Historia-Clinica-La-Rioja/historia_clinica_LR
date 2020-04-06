package net.pladema.user.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.TestUtils.assertUpdateAuditableEntity;
import static net.pladema.permissions.RoleTestUtils.createLicense;
import static net.pladema.permissions.RoleTestUtils.createUserLicense;
import static net.pladema.user.UserTestUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.projections.PageableUsers;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class UserRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void saveCreateTest() {
		User user = createUser("username@mail.com");
		User createdUser = save(user);

		assertThat(createdUser).isNotNull();

		assertThat(createdUser.getId()).isNotNull();

		assertCreateAuditableEntity(createdUser);
	}

	@Test
	public void saveUpdateTest() {
		User user = createUser("username@mail.com");
		User createdUser = save(user);

		assertThat(createdUser).isNotNull();

		assertCreateAuditableEntity(createdUser);

		createdUser.setPersonId(4);
		User updatedUser = save(createdUser);

		assertThat(updatedUser).isNotNull();

		assertUpdateAuditableEntity(updatedUser);

	}

	@Test
	public void testFindByUsername_exists() {
		User user = createUser("username@mail.com");
		user = save(user);

		Optional<User> opUser = userRepository.findByUsername(user.getUsername());

		assertThat(opUser).isNotNull();

		assertThat(opUser.isPresent()).isTrue();

		assertThat(opUser.get()).hasFieldOrPropertyWithValue("id", user.getId());

	}

	@Test
	public void testFindByUsername_notexists() {
		Optional<User> opUser = userRepository.findByUsername("asdasdasd");

		assertThat(opUser).isNotNull();

		assertThat(opUser.isPresent()).isFalse();
	}

	@Test
	public void testPage_query() {
		Role admin = save(createLicense("ADMIN"));
		Role free = save(createLicense("FREE"));
		
		User user2Licences = save(createUser("username1@mail.com"));
		
		save(createUserLicense(user2Licences, free));
		save(createUserLicense(save(createUser("username10@mail.com")), admin));
		save(createUserLicense(save(createUser("username3@mail.com")), free));
		save(createUserLicense(save(createUser("username4@mail.com")), admin));
		save(createUserLicense(save(createUser("username2@mail.com")), admin));
		save(createUserLicense(save(createUser("username8@mail.com")), admin));
		save(createUserLicense(save(createUser("username5@mail.com")), admin));
		save(createUserLicense(save(createUser("username6@mail.com")), free));;
		save(createUserLicense(save(createUser("username9@mail.com")), free));
		save(createUserLicense(save(createUser("username11@mail.com")), admin));
		save(createUserLicense(save(createUser("username7@mail.com")), admin));
		save(createUserLicense(user2Licences, admin));

		Pageable sortedByName = PageRequest.of(0, 5, Sort.by("username").ascending());
		Page<PageableUsers> pageUser = userRepository.pageableUsers(sortedByName, PageableUsers.class);

		assertThat(pageUser).isNotNull();
		assertThat(pageUser.getContent())
			.isNotEmpty()
			.hasSize(sortedByName.getPageSize());
		
	}

}