package net.pladema.oauth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.repository.entity.Gender;
import net.pladema.person.repository.entity.IdentificationType;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class OauthUser {
	private static final String FEMALE_CUIL = "27";
	private static final String MALE_CUIL = "20";
	private static final int CUIL_LENGTH = 11;
	
	private static final String TYPE_DNI = "DNI";
	private static final String TYPE_CI = "CI";
	private static final String TYPE_CUIL = "CUIL";
	private static final String TYPE_CUIT = "CUIT";
	
	
	String nombres;
	String apellidos;
	List<Documento> tiposDocumentoPersona;
	String cuitCuil;
	
	public Optional<Short> getGenderId() {
		return inferGenderFromCuil(cuitCuil);
	}
	
	public Optional<Short> getIdentificationTypeId() {
		Short identificationTypeId = null;
		if (!getTiposDocumentoPersona().isEmpty()) {
			String  identificationType = getTiposDocumentoPersona().get(0).getTipoDocumento().getTipoDocumento();
			identificationTypeId = inferIdentificationType(identificationType); 
		}
		return Optional.ofNullable(identificationTypeId);
	}
	
	private static Short inferIdentificationType(String identificationType) {
		short typeId;
		switch (identificationType) {
		case TYPE_DNI:
			typeId = IdentificationType.DNI;
			break;
		case TYPE_CI:
			typeId = IdentificationType.CI;
			break;
		case TYPE_CUIL:
			typeId = IdentificationType.CUIL;
			break;
		case TYPE_CUIT:
			typeId = IdentificationType.CUIT;
			break;
		default:
			typeId = IdentificationType.OTHER;
			break;
		}
		return typeId;
	}
	
	private static Optional<Short> inferGenderFromCuil(String cuil) {
		Short result = null;
		if (cuil.length() == CUIL_LENGTH) {
			String cuilGender = cuil.substring(0, 2);
			if (cuilGender.equals(FEMALE_CUIL)) {
				result = Gender.FEMALE;
			} 
			if (cuilGender.equals(MALE_CUIL)) {
				result = Gender.MALE;
			}  
		}
		return Optional.ofNullable(result);
	}
}
