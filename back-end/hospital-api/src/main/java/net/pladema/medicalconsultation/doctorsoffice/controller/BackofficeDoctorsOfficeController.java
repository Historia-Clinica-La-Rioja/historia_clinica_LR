package net.pladema.medicalconsultation.doctorsoffice.controller;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.doctorsoffice.controller.constraints.BackofficeDoctorsOfficeEntityValidator;
import net.pladema.medicalconsultation.doctorsoffice.controller.permissions.BackofficeDoctorsOfficeValidator;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/doctorsoffices")
public class BackofficeDoctorsOfficeController extends AbstractBackofficeController<DoctorsOffice, Integer> {

    public BackofficeDoctorsOfficeController(DoctorsOfficeRepository repository,
											 BackofficeDoctorsOfficeValidator doctorsOfficeValidator,
											 BackofficeDoctorsOfficeEntityValidator doctorsOfficeEntityValidator,
											 DiaryRepository diaryRepository,
											 EmergencyCareEpisodeRepository emergencyCareEpisodeRepository) {
        super(new DoctorsOfficeStore(repository, diaryRepository, emergencyCareEpisodeRepository), doctorsOfficeValidator, doctorsOfficeEntityValidator);
    }


    @Override
    public DoctorsOffice create(@Valid @RequestBody DoctorsOffice entity) {
        entity.setOpeningTime(LocalTime.of(00,00,00));
        entity.setClosingTime(LocalTime.of(23,00,00));
        return super.create(entity);
    }

    @Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<DoctorsOffice> getList(Pageable pageable, DoctorsOffice entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<DoctorsOffice> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(doctorsOffice -> itemsAllowed.ids.contains(doctorsOffice.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
