package net.pladema.user.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static net.pladema.user.UserTestUtils.createPassword;
import static net.pladema.user.UserTestUtils.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.BaseRepositoryTest;
import net.pladema.user.repository.UserPasswordRepository;
import net.pladema.user.repository.entity.User;
import net.pladema.user.repository.entity.UserPassword;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql=false)
public class UserPasswordRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private UserPasswordRepository userPasswordRepository;
	
	@Before 
	public void setUp() throws Exception {
	}
	
	@Test
	public void save_create_test() {
		User user = save(createUser("username@mail.com"));
		UserPassword pass = createPassword(user);
		UserPassword createdPass = userPasswordRepository.saveAndFlush(pass);
			
		assertThat(createdPass)
			.isNotNull();
		
		assertThat(createdPass.getId())
			.isNotNull()
			.isEqualTo(user.getId());
		
		assertCreateAuditableEntity(createdPass);
	}
}