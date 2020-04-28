package net.pladema.security.token.service.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.pladema.security.service.enums.ETokenType;

public class LocalClaims implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -801022172230261656L;
	
	private transient Map<String, Object> data = new HashMap<>();

	public LocalClaims(ETokenType tokenType, Integer userId) {
		super();
		data.put("userId", userId);
		data.put("tokentype", tokenType.getUrl());
		data.put("createdOn", System.currentTimeMillis());
	}

	public LocalClaims(Map<String, Object> claims) {
		super();
		this.data = claims;
	}

	public Map<String, Object> getClaims() {
		return data;
	}

	public void addInfo(String key, Object value) {
		if (data.containsKey(key))
			data.put(key, value);
		updateInfo(key, value);
	}

	private void updateInfo(String key, Object value) {
		data.remove(key);
		data.put(key, value);
	}

	public Optional<Object> getValue(String key) {
		return Optional.ofNullable(data.get(key));
	}
	
	public Optional<String> getUsername() {
		Optional<Object> opUsername = getValue("username");
		if (opUsername.isPresent())
			return Optional.of((String)opUsername.get());
		return Optional.empty();	
	}
}
