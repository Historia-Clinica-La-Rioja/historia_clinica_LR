package net.pladema.user.repository;

import static net.pladema.TestUtils.assertCreateAuditableEntity;
import static org.assertj.core.api.Assertions.assertThat;

import net.pladema.user.repository.entity.UserPasswordBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.pladema.user.repository.entity.UserPassword;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql=false)
public class UserPasswordRepositoryTest {
	private final static Integer USER_ID = 1008;

	@Autowired
	private UserPasswordRepository userPasswordRepository;
	
	@Before 
	public void setUp() throws Exception {
	}
	
	@Test
	public void save_create_test() {
		UserPassword pass = UserPasswordBean.newUserPassword(USER_ID);
		UserPassword createdPass = userPasswordRepository.saveAndFlush(pass);
			
		assertThat(createdPass)
			.isNotNull();
		
		assertThat(createdPass.getId())
			.isNotNull()
			.isEqualTo(USER_ID);
		
		assertCreateAuditableEntity(createdPass);
	}
}