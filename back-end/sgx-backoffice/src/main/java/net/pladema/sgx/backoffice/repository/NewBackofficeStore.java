package net.pladema.sgx.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Un BackofficeStore ofrece al <code>AbstractBackofficeController<code/> una interface encargada del almacenamiento de una Entidad con su respectivo Identificador.
 *
 * La mayoría de las veces alcanzará con usar BackofficeRepository para una implementación sencilla desde un JpaRepository.
 *
 * Cuando sea necesario la separación de tipos entre un Controller y un JpaRepositori, es cuando se hace necesario implementar un BackofficeStore específico. Algunos ejemplos:
 *
 * == Uso de claves compuestas ==
 * Un escenario usual es tener un JpaRespository< N2NEntity, n2nPK > pero muchas operaciones del AbstractBackofficeController requieren contar con un id de tipo primitivo.
 *
 * En estos casos se podría tener un <code>AbstractBackofficeController< N2NEntityDto, String ></code> usando un BackofficeStore<N2NEntityDto, String> haciendo el mappeo para usar el JpaRespository< N2NEntity, n2nPK >
 *
 * == Modelo JPA sin relaciones ==
 * Otro escenario posible es que la entidad JPA no cuente con alguna información necesaria por la UI, como por ejemplo para utilizar un ReferenceArrayInput.
 *
 * En estos casos se podría definir un DTO, utilizar otros repository e incluso utilizar MapStruct.
 *
 *
 * @param <E>
 * @param <I>
 */
public interface NewBackofficeStore<E, I> {

	Page<E> findAll(E example, Pageable pageable);

	Page<E> findAll(E example, Pageable pageable, String q);

	List<E> findAllById(List<I> ids);

	Optional<E> findById(I id);

	E save(E entity);

	void deleteById(I id);
}
