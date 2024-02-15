package net.pladema.cipres.infrastructure.output.rest.domain;

public class CipresMasterData {

	public static final String GENDER_FEMALE_INITIAL = "F";

	public static final String GENDER_MALE_INITIAL = "M";

	public static final String GENDER_NONBINARY_INITIAL = "X";

	public static final String GENDER_MALE = "/api/paciente/referencias/sexo/1";

	public static final String GENDER_FEMALE = "/api/paciente/referencias/sexo/2";

	public static final String GENDER_NONBINARY = "/api/paciente/referencias/sexo/4";

	public static final String NOT_SPECIFIED_DOCUMENT_TYPE = "/api/paciente/referencias/tipo_documento/0";

	public static final String DNI_DOCUMENT_TYPE = "/api/paciente/referencias/tipo_documento/1";

	public static final String FOREIGN_DOCUMENT_TYPE = "/api/paciente/referencias/tipo_documento/5";

	public static final String CLINICAL_SPECIALTY_IRI = "/api/consultorio/especialidad_medica/";

	public static final String ESTABLISHMENT_IRI = "/api/establecimiento/dependencia/";

	public static final String CONSULTATION_REASON = "/api/consultorio/motivo/2";

	public static final String PATIENT_MEDICAL_CONTACT = "/api/consultorio/contacto_medicopaciente/1";

}
