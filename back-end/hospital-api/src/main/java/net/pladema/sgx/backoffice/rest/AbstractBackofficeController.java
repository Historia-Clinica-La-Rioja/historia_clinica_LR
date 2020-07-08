package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBackofficeController<E, I> {
    protected final Logger logger;
    protected final BackofficeStore<E, I> store;
    protected final BackofficePermissionValidator<E, I> permissionValidator;
    private final BackofficeEntityValidator<E, I> entityValidator;

    public AbstractBackofficeController(
            BackofficeStore<E, I> repository,
            BackofficePermissionValidator<E, I> permissionValidator,
            BackofficeEntityValidator<E, I> entityValidator
    ) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.store = repository;
        this.permissionValidator = permissionValidator;
        this.entityValidator = entityValidator;
    }

    public AbstractBackofficeController(
            JpaRepository<E, I> repository,
            BackofficePermissionValidator<E, I> permissionValidator,
            BackofficeEntityValidator<E, I> entityValidator
    ) {
        this(
                new BackofficeRepository<>(repository),
                permissionValidator,
                entityValidator
        );
    }

    public AbstractBackofficeController(
            BackofficeStore<E, I> repository
    ) {
        this(
                repository,
                new BackofficePermissionValidatorAdapter<>(),
                new BackofficeEntityValidatorAdapter<>()
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
            BackofficeEntityValidator<E, I> entityValidator
    ) {
        this(
                new BackofficeRepository<>(repository),
                new BackofficePermissionValidatorAdapter<>(),
                entityValidator
        );
    }

    public AbstractBackofficeController(
            JpaRepository<E, I> repository,
            BackofficePermissionValidator<E, I> permissionValidator
    ) {
        this(
                new BackofficeRepository<>(repository),
                permissionValidator,
                new BackofficeEntityValidatorAdapter<>()
        );
    }

    public AbstractBackofficeController(
            BackofficeStore<E, I> repository,
            BackofficePermissionValidator<E, I> permissionValidator) {
        this(
                repository,
                permissionValidator,
                new BackofficeEntityValidatorAdapter<>()
        );
    }

    @GetMapping(params = "!ids")
    public @ResponseBody
    Page<E> getList(Pageable pageable, E entity) {
        logger.debug("GET_LIST {}", entity);
        ItemsAllowed<I> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
        if (itemsAllowed.all)
            return store.findAll(entity, pageable);
        List<E> list = store.findAllById(itemsAllowed.ids);
        return new PageImpl<>(list, pageable, list.size());
    }

    @GetMapping(params = "ids")
    public @ResponseBody
    Iterable<E> getMany(@RequestParam List<I> ids) {
        ids = permissionValidator.filterIdsByPermission(ids);
        return store.findAllById(ids);
    }


    @GetMapping(value="/elements")
    public @ResponseBody
    Iterable<E> getElements() {
        ItemsAllowed<I> itemsAllowed = permissionValidator.itemsAllowedToList();
        if (itemsAllowed.all)
            return store.findAll();
        if (itemsAllowed.isEmpty())
            return new ArrayList<>();
        return store.findAllById(itemsAllowed.ids);
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
    @Transactional
    public @ResponseBody
    E create(@Valid @RequestBody E entity) {
        logger.debug("CREATE {}", entity);
        permissionValidator.assertCreate(entity);
        entityValidator.assertCreate(entity);
        return store.save(entity);
    }


    @PutMapping("/{id}")
    @Modifying
    @Transactional
    public @ResponseBody
    E update(@PathVariable("id") I id, @RequestBody E body) {
        logger.debug("UPDATE[id={}] {}", id, body);
        permissionValidator.assertUpdate(id, body);
        entityValidator.assertUpdate(id, body);
        return store.save(body);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public @ResponseBody
    BackofficeDeleteResponse<I> delete(@PathVariable("id") I id) {
        logger.debug("DELETE[id={}]", id);
        permissionValidator.assertDelete(id);
        entityValidator.assertDelete(id);
        store.deleteById(id);

        return new BackofficeDeleteResponse<>(id);
    }

	@DeleteMapping(params = "ids")
    @Transactional
	public @ResponseBody
	BackofficeDeleteResponse<List<I>> deleteMany(@RequestParam List<I> ids) {
		logger.debug("DELETE_MANY[ids={}]", ids);
		ids.forEach(id ->  {
			permissionValidator.assertDelete(id);
			entityValidator.assertDelete(id);
			store.deleteById(id);
		});

		return new BackofficeDeleteResponse<>(ids);
	}
}
