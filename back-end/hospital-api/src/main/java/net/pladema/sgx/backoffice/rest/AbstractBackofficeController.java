package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;

import net.pladema.sgx.acl.service.AclCrudPermissionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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
	private final JpaRepository<E, I> repository;
	private final AclCrudPermissionValidator<E, I> permissionValidator;
	private final BackofficeQueryAdapter<E> mapper;

	public AbstractBackofficeController(
			JpaRepository<E, I> repository,
			AclCrudPermissionValidator<E, I> permissionValidator,
			BackofficeQueryAdapter<E> mapper
	) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.repository = repository;
		this.permissionValidator = permissionValidator;
		this.mapper = mapper;
	}

	public AbstractBackofficeController(
			JpaRepository<E, I> repository
	) {
		this(
				repository,
				new AclCrudPermissionValidatorAdapter<>(),
				new BackofficeQueryAdapter<>()
		);
	}

	public AbstractBackofficeController(
			JpaRepository<E, I> repository,
			BackofficeQueryAdapter<E> mapper
	) {
		this(
				repository,
				new AclCrudPermissionValidatorAdapter<>(),
				mapper
		);
	}

	@GetMapping(params = "!ids")
	public @ResponseBody
	Page<E> getList(Pageable pageable, E entity) {
		logger.debug("GET_LIST {}", entity);
		permissionValidator.assertGetList(entity);
		Example<E> example = mapper.buildExample(entity);
		return repository.findAll(example, pageable);
	}

	@GetMapping(params = "ids")
	public @ResponseBody
	Iterable<E> getMany(@RequestParam List<I> ids) {
		return repository.findAllById(ids);
	}

	@GetMapping("/{id}")
	public @ResponseBody
	E getOne(@PathVariable("id") I id) {
		logger.debug("GET_ONE[id={}]", id);
		permissionValidator.assertGetOne(id);
		return repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found " + id));
	}


	@PostMapping
	public @ResponseBody
	E create(@Valid @RequestBody E entity) {
		logger.debug("CREATE {}", entity);
		permissionValidator.assertCreate(entity);
		return repository.save(entity);
	}


	@PutMapping("/{id}") @Modifying
	public @ResponseBody
	E update(@PathVariable("id") I id, @RequestBody E body) {
		logger.debug("UPDATE[id={}] {}", id, body);
		permissionValidator.assertUpdate(id);
		return repository.save(body);
	}

	@DeleteMapping("/{id}")
	public @ResponseBody
	BackofficeDeleteResponse delete(@PathVariable("id") I id) {
		logger.debug("DELETE[id={}]", id);
		permissionValidator.assertDelete(id);
		repository.deleteById(id);

		return new BackofficeDeleteResponse<>(id);
	}

}
