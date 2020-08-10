package net.pladema.patient.service.impl;

import net.pladema.patient.repository.AdditionalDoctorRepository;
import net.pladema.patient.repository.entity.AdditionalDoctor;
import net.pladema.patient.service.AdditionalDoctorService;
import net.pladema.patient.service.domain.AdditionalDoctorBo;
import net.pladema.patient.service.domain.DoctorsBo;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AdditionalDoctorServiceImpl implements AdditionalDoctorService {

    private final AdditionalDoctorRepository additionalDoctorRepository;

    public AdditionalDoctorServiceImpl(AdditionalDoctorRepository additionalDoctorRepository) {
        this.additionalDoctorRepository = additionalDoctorRepository;
    }

    private AdditionalDoctorBo addAdditionalDoctor(AdditionalDoctorBo additionalDoctorBo) {
        if ( (additionalDoctorBo.getFullName()!= null) || ( additionalDoctorBo.getPhoneNumber() != null)){
            AdditionalDoctor generalPractitioner = new AdditionalDoctor(additionalDoctorBo);
            generalPractitioner = additionalDoctorRepository.save(generalPractitioner);
            return new AdditionalDoctorBo(generalPractitioner);
        }
        return null;
    }

    @Override
    public DoctorsBo addAdditionalDoctors(DoctorsBo doctorsBo, Integer patientId) {
        AdditionalDoctorBo generalPractitionerBo = doctorsBo.getGeneralPractitionerBo();
        AdditionalDoctorBo pamiDoctorBo = doctorsBo.getPamiDoctorBo();
        pamiDoctorBo.setPatientId(patientId);
        generalPractitionerBo.setPatientId(patientId);
        AdditionalDoctorBo generalPractitionerBoResponse = addAdditionalDoctor(generalPractitionerBo);
        AdditionalDoctorBo pamiDoctorBoResponse = addAdditionalDoctor(pamiDoctorBo);
		return new DoctorsBo(generalPractitionerBoResponse, pamiDoctorBoResponse);
	}

	@Override
	public DoctorsBo getAdditionalDoctors(Integer patientId) {
		Collection<AdditionalDoctor> doctors = additionalDoctorRepository.getAdditionalDoctors(patientId);
		AdditionalDoctorBo generalPractitioner = doctors.stream().filter(AdditionalDoctor::isGeneralPractitioner)
				.findFirst().map(AdditionalDoctorBo::new).orElse(null);
		AdditionalDoctorBo pamiDoctor = doctors.stream().filter(AdditionalDoctor::isPamiDoctor)
				.findFirst().map(AdditionalDoctorBo::new).orElse(null);
		return new DoctorsBo(generalPractitioner, pamiDoctor);
    }
	
}
