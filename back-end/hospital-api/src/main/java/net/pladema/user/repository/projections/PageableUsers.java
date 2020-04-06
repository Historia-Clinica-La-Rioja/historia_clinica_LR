package net.pladema.user.repository.projections;

import java.time.LocalDateTime;

public interface PageableUsers {
	
	public Integer getId();

	public String getUsername();
	
	public LocalDateTime getLastLogin();
	
	public Boolean getEnable();
	
	public String getRole();
	
	
}
