package ar.lamansys.sgh.internal.application;

import net.pladema.medicalconsultation.appointment.infraestructure.output.internal.EncryptPatientIdentificationCode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptPatientIdentificationCodeImpl implements EncryptPatientIdentificationCode {

	private final BCryptPasswordEncoder encoder;

	public EncryptPatientIdentificationCodeImpl() {
		this.encoder = new BCryptPasswordEncoder();
	}

	@Override
	public String run(String patientIdentificationCode) {
		return encoder.encode(patientIdentificationCode);
	}

}
