package net.pladema.user.repository;

import net.pladema.UnitRepository;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static net.pladema.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserRepositoryTest extends UnitRepository {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void saveCreateTest() {
		User user = UserBean.newUser("username@mail.com");
		user.setPersonId(4);
		User createdUser = userRepository.saveAndFlush(user);

		assertThat(createdUser).isNotNull();

		assertThat(createdUser.getId()).isNotNull();

		assertCreateAuditableEntity(createdUser);
	}

	@Test
	public void saveUpdateTest() {
		User user = UserBean.newUser("username@mail.com");
		user.setPersonId(4);
		User createdUser = userRepository.saveAndFlush(user);

		assertThat(createdUser).isNotNull();

		assertCreateAuditableEntity(createdUser);

		User updatedUser = userRepository.saveAndFlush(createdUser);

		assertThat(updatedUser).isNotNull();

		assertUpdateAuditableEntity(updatedUser);
	}

	@Test
	public void testFindByUsername_exists() {
		User user = UserBean.newUser("username@mail.com");
		user.setPersonId(4);
		user = userRepository.saveAndFlush(user);

		Optional<User> opUser = userRepository.findByUsername(user.getUsername());

		assertThat(opUser).isNotNull();

		assertThat(opUser.isPresent()).isTrue();

		assertThat(opUser.get()).hasFieldOrPropertyWithValue("id", user.getId());

	}

	@Test
	public void testFindByUsername_not_exists() {
		Optional<User> opUser = userRepository.findByUsername("test");

		assertThat(opUser).isNotNull();

		assertThat(opUser.isPresent()).isFalse();
	}

}