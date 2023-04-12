package ar.lamansys.base.domain;

import lombok.Getter;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ReverseProxyBo {
	private final ResponseEntity<?> response;
}
