package net.pladema.establishment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.IteratorIterable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.SnomedGroupTypeRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;

@RestController
@RequestMapping("backoffice/snomedgrouptypes")
public class BackofficeSnomedGroupTypeController extends AbstractBackofficeController<SnomedGroupType, Short> {

	public BackofficeSnomedGroupTypeController(SnomedGroupTypeRepository repository) {
		super(repository);
	}

	@Override
	public Iterable<SnomedGroupType> getMany(@RequestParam("ids") List<Short> ids) {
		return featureFlagFilterData(super.getMany(ids));
	}

	@Override
	public Iterable<SnomedGroupType> getElements() {
		return featureFlagFilterData(super.getElements());
	}

	private Iterable<SnomedGroupType> featureFlagFilterData(Iterable<SnomedGroupType> iterable){
		if (!AppFeature.HABILITAR_ASOCIAR_PRACTICAS_A_INSTITUCIONES.isActive()) {
			ArrayList<SnomedGroupType> elements = new ArrayList<>();
			iterable.iterator().forEachRemaining(elements::add);
			ArrayList<SnomedGroupType> items = elements.stream()
					.filter(item -> !Objects.equals(item.getId(), SnomedGroupType.SEARCH_GROUP))
					.collect(Collectors.toCollection(ArrayList::new));
			return new IteratorIterable<>(items.iterator());
		}
		return iterable;
	}
}
