package ar.lamansys.sgx.shared.gitinfo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "git")
@PropertySource(value = "classpath:git.properties")
public class GitInfoProperties {

	private String branch;
	private GitCommitBo commit;
}
