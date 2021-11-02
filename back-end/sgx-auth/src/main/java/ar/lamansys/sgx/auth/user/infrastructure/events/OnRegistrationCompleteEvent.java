package ar.lamansys.sgx.auth.user.infrastructure.events;

import java.util.Locale;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6147904039545771860L;

	private Locale locale;

	private User user;

	public OnRegistrationCompleteEvent(User user) {
		super(user);
		this.user = user;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}