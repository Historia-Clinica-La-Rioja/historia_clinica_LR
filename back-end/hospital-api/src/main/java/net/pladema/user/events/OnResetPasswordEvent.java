package net.pladema.user.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import net.pladema.user.repository.entity.User;

public class OnResetPasswordEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6147904039545771860L;

	private Locale locale;

	private User user;

	public OnResetPasswordEvent(User user) {
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