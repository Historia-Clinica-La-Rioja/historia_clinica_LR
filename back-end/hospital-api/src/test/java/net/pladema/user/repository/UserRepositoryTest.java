package net.pladema.user.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.TestUtils.assertUpdateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import net.pladema.user.repository.entity.UserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.user.repository.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void saveCreateTest() {
		User user = UserBean.newUser("username@mail.com");
		User createdUser = userRepository.saveAndFlush(user);

		assertThat(createdUser).isNotNull();

		assertThat(createdUser.getId()).isNotNull();

		assertCreateAuditableEntity(createdUser);
	}

	@Test
	public void saveUpdateTest() {
		User user = UserBean.newUser("username@mail.com");
		User createdUser = userRepository.saveAndFlush(user);

		assertThat(createdUser).isNotNull();

		assertCreateAuditableEntity(createdUser);

		createdUser.setPersonId(4);
		User updatedUser = userRepository.saveAndFlush(createdUser);

		assertThat(updatedUser).isNotNull();

		assertUpdateAuditableEntity(updatedUser);

	}

	@Test
	public void testFindByUsername_exists() {
		User user = UserBean.newUser("username@mail.com");
		user = userRepository.saveAndFlush(user);

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

}