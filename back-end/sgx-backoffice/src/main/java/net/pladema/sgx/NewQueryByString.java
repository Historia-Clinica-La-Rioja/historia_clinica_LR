package net.pladema.sgx;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface NewQueryByString<E> {
	Page<E> findAll(String q, Pageable pageable);
}