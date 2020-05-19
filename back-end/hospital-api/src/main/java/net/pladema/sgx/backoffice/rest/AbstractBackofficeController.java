package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

public abstract class AbstractBackofficeController<E, I> {
	protected final Logger logger;
	protected final BackofficeStore<E, I> store;
	private final BackofficePermissionValidator<E, I> permissionValidator;

	public AbstractBackofficeController(
			BackofficeStore<E, I> repository,
			BackofficePermissionValidator<E, I> permissionValidator
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.store = repository;
		this.permissionValidator = permissionValidator;
	}

	public AbstractBackofficeController(
			BackofficeStore<E, I> repository
	) {
		this(
				repository,
				new BackofficePermissionValidatorAdapter<>()
		);
	}

	public AbstractBackofficeController(
			JpaRepository<E, I> repository
	) {
		this(
				new BackofficeRepository<>(repository)
		);
	}

	public AbstractBackofficeController(
			JpaRepository<E, I> repository,
			BackofficePermissionValidator<E, I> permissionValidator
	) {
		this(
				new BackofficeRepository<>(repository),
				permissionValidator
		);
	}

	@GetMapping(params = "!ids")
	public @ResponseBody
	Page<E> getList(Pageable pageable, E entity) {
		logger.debug("GET_LIST {}", entity);
		permissionValidator.assertGetList(entity);
		return store.findAll(entity, pageable);
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	Iterable<E> getMany(@RequestParam List<I> ids) {
		return store.findAllById(ids);
	}

	@GetMapping("/{id}")
	public @ResponseBody
	E getOne(@PathVariable("id") I id) {
		logger.debug("GET_ONE[id={}]", id);
		permissionValidator.assertGetOne(id);
		return store.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found " + id));
	}


	@PostMapping
	public @ResponseBody
	E create(@Valid @RequestBody E entity) {
		logger.debug("CREATE {}", entity);
		permissionValidator.assertCreate(entity);
		return store.save(entity);
	}


	@PutMapping("/{id}") @Modifying
	public @ResponseBody
	E update(@PathVariable("id") I id, @RequestBody E body) {
		logger.debug("UPDATE[id={}] {}", id, body);
		permissionValidator.assertUpdate(id, body);
		return store.save(body);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody
	BackofficeDeleteResponse<I> delete(@PathVariable("id") I id) {
		logger.debug("DELETE[id={}]", id);
		permissionValidator.assertDelete(id);
		store.deleteById(id);

		return new BackofficeDeleteResponse<>(id);
	}

}
