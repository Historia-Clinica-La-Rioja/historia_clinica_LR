package ar.lamansys.pac.domain;

import lombok.Getter;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class PacReverseProxy {
	private final ResponseEntity<?> response;
}
