package ar.lamansys.sgh.publicapi;

import ar.lamansys.sgh.publicapi.userinformation.infrastructure.output.TestDataSourceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;

@EnableJpaRepositories(basePackages = {"net.pladema", "ar.lamansys.sgh.publicapi"})
@EntityScan(basePackages = {"net.pladema", "ar.lamansys.sgh.publicapi"})
@Transactional
@PropertySource("classpath:application-test.properties")
@DataJpaTest(showSql = false)
@ContextConfiguration(classes = {TestDataSourceConfig.class}, loader = AnnotationConfigContextLoader.class)
public class UnitRepository {

	@Autowired
	private TestEntityManager entityManager;

	@MockBean
	private UserInformationPublicApiPermission userInformationPublicApiPermission;

	protected <E> E save(E entity) {
		E saved = entityManager.persist(entity);
		entityManager.flush();
		return saved;
	}

	protected <E> E merge(E entity){
		return entityManager.merge(entity);
	}

	protected void flush() {
		entityManager.flush();
	}
}
