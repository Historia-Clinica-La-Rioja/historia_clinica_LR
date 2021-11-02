package ar.lamansys.sgx.shared.scheduling.infrastructure.configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M")
public class ShedlockConfiguration {
	
	@Bean
	public LockProvider lockProvider(DataSource dataSource) {
	    return new JdbcTemplateLockProvider(dataSource, "shedlock");
	}

}
