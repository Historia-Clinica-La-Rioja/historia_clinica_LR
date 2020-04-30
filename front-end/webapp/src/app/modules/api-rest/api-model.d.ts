/* tslint:disable */
/* eslint-disable */

export interface APatientDto extends APersonDto {
    comments: string;
    identityVerificationStatusId: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    typeId: number;
}

export interface APersonDto {
    apartment: string;
    birthDate: Date;
    cityId: number;
    cuil: string;
    email: string;
    ethnic: string;
    firstName: string;
    floor: string;
    genderId: number;
    genderSelfDeterminationId: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    mothersLastName: string;
    nameSelfDetermination: string;
    number: string;
    otherLastNames: string;
    phoneNumber: string;
    postcode: string;
    quarter: string;
    religion: string;
    street: string;
}

export interface AbstractUserDto extends Serializable {
}

export interface AddressDto extends Serializable {
    apartment: string;
    city: CityDto;
    cityId: number;
    floor: string;
    id: number;
    number: string;
    postcode: string;
    province: ProvinceDto;
    quarter: string;
    street: string;
}

export interface AllergyConditionDto extends HealthConditionDto {
    categoryId: string;
    date: string;
    severity: string;
}

export interface AnamnesisDto extends Serializable {
    allergies: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto[];
    diagnosis: HealthConditionDto[];
    documentStatusId: string;
    familyHistories: HealthHistoryConditionDto[];
    inmunizations: InmunizationDto[];
    medications: MedicationDto[];
    notes: DocumentObservationsDto;
    personalHistories: HealthHistoryConditionDto[];
    vitalSigns: VitalSignDto[];
}

export interface AnthropometricDataDto extends Serializable {
    bloodType: ClinicalObservationDto;
    deleted: boolean;
    height: ClinicalObservationDto;
    weight: ClinicalObservationDto;
}

export interface ApiError {
    errors: string[];
    message: string;
    status: HttpStatus;
}

export interface ApiErrorMessage {
    code: string;
    text: string;
}

export interface BMPatientDto extends APatientDto {
    id: number;
}

export interface BMPersonDto extends APersonDto {
    id: number;
}

export interface BackofficeUserDto {
    enable: boolean;
    id: number;
    lastLogin: Date;
    personId: number;
    roles: BackofficeUserRoleDto[];
    username: string;
}

export interface BackofficeUserRoleDto {
    institutionId: number;
    roleId: number;
    userId: number;
}

export interface BasicDataPersonDto {
    age: number;
    firstName: string;
    gender: GenderDto;
    id: number;
    lastName: string;
}

export interface BasicPatientDto {
    id: number;
    person: BasicDataPersonDto;
}

export interface BedDto extends Serializable {
    bedNumber: string;
    id: number;
    room: RoomDto;
}

export interface CityDto extends MasterdataDto<number> {
    id: number;
}

export interface ClinicalObservationDto extends Serializable {
    deleted: boolean;
    id: number;
    value: string;
}

export interface ClinicalSpecialtyDto {
    id: number;
    name: string;
}

export interface ClinicalTermDto extends Serializable {
    deleted: boolean;
    id: number;
    snomed: SnomedDto;
    statusId: string;
}

export interface DocumentObservationsDto extends Serializable {
    clinicalImpressionNote: string;
    currentIllnessNote: string;
    evolutionNote: string;
    indicationsNote: string;
    otherNote: string;
    physicalExamNote: string;
    studiesSummaryNote: string;
}

export interface GenderDto extends MasterdataDto<number> {
    id: number;
}

export interface HealthConditionDto extends ClinicalTermDto {
    verificationId: string;
}

export interface HealthHistoryConditionDto extends HealthConditionDto {
    date: string;
    note: string;
}

export interface HealthInsuranceDto {
    acronym: string;
    rnos: number;
}

export interface IdentificationTypeDto extends MasterdataDto<number> {
    id: number;
}

export interface InmunizationDto extends ClinicalTermDto {
    administrationDate: string;
    note: string;
}

export interface InternmentEpisodeDto {
    bed: BedDto;
    doctor: ResponsibleDoctorDto;
    id: number;
    patient: PatientDto;
    specialty: ClinicalSpecialtyDto;
}

export interface InternmentGeneralStateDto extends Serializable {
    allergies: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto[];
    diagnosis: HealthConditionDto[];
    familyHistories: HealthHistoryConditionDto[];
    inmunizations: InmunizationDto[];
    medications: MedicationDto[];
    personalHistories: HealthHistoryConditionDto[];
    vitalSigns: VitalSignDto[];
}

export interface InternmentSummaryDto {
    bed: BedDto;
    createdOn: Date;
    doctor: ResponsibleDoctorDto;
    id: number;
    specialty: ClinicalSpecialtyDto;
    totalInternmentDays: number;
}

export interface JWTokenDto extends Serializable {
    refreshToken: string;
    token: string;
}

export interface LoginDto extends Serializable {
    password: string;
    username: string;
}

export interface MasterDataInterface<T> {
    description: string;
    id: T;
}

export interface MasterdataDto<T> extends MasterDataInterface<T>, Serializable {
}

export interface MedicationDto extends ClinicalTermDto {
    note: string;
}

export interface PasswordResetDto {
    password: string;
    token: string;
}

export interface PatientDto {
    firstName: string;
    id: number;
    lastName: string;
}

export interface PatientSearchDto {
    activo: boolean;
    idPatient: number;
    person: BMPersonDto;
    ranking: number;
}

export interface PatientSearchFilter {
    birthDate: Date;
    firstName: string;
    genderId: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
}

export interface PermissionsDto {
    roleAssignments: RoleAssignment[];
}

export interface PersonBasicDataResponseDto extends Serializable {
    birthDate: string;
    firstName: string;
    lastName: string;
}

export interface PersonalInformationDto {
    address: AddressDto;
    birthDate: Date;
    cuil: string;
    email: string;
    id: number;
    identificationNumber: string;
    identificationType: IdentificationTypeDto;
    phoneNumber: string;
}

export interface ProvinceDto extends MasterdataDto<number> {
    id: number;
}

export interface ResponseAnamnesisDto extends AnamnesisDto {
    id: number;
}

export interface ResponsibleDoctorDto extends Serializable {
    firstName: string;
    id: number;
    lastName: string;
    licence: string;
}

export interface RoleAssignment {
    institutionId: number;
    role: ERole;
}

export interface RoomDto extends Serializable {
    id: number;
    roomNumber: string;
    sector: SectorDto;
}

export interface SectorDto extends Serializable {
    description: string;
    id: number;
}

export interface Serializable {
}

export interface SnomedDto extends Serializable {
    fsn: string;
    id: string;
    parentFsn: string;
    parentId: string;
}

export interface UserDto extends AbstractUserDto {
    email: string;
    id: number;
}

export interface VitalSignDto extends Serializable {
    bloodOxygenSaturation: ClinicalObservationDto;
    deleted: boolean;
    diastolicBloodPressure: ClinicalObservationDto;
    heartRate: ClinicalObservationDto;
    respiratoryRate: ClinicalObservationDto;
    systolicBloodPressure: ClinicalObservationDto;
    temperature: ClinicalObservationDto;
}

export type ERole = "ROOT" | "ADMINISTRADOR" | "ESPECIALISTA_MEDICO" | "PROFESIONAL_DE_SALUD" | "ADMINISTRATIVO";

export type HttpStatus = "CONTINUE" | "SWITCHING_PROTOCOLS" | "PROCESSING" | "CHECKPOINT" | "OK" | "CREATED" | "ACCEPTED" | "NON_AUTHORITATIVE_INFORMATION" | "NO_CONTENT" | "RESET_CONTENT" | "PARTIAL_CONTENT" | "MULTI_STATUS" | "ALREADY_REPORTED" | "IM_USED" | "MULTIPLE_CHOICES" | "MOVED_PERMANENTLY" | "FOUND" | "MOVED_TEMPORARILY" | "SEE_OTHER" | "NOT_MODIFIED" | "USE_PROXY" | "TEMPORARY_REDIRECT" | "PERMANENT_REDIRECT" | "BAD_REQUEST" | "UNAUTHORIZED" | "PAYMENT_REQUIRED" | "FORBIDDEN" | "NOT_FOUND" | "METHOD_NOT_ALLOWED" | "NOT_ACCEPTABLE" | "PROXY_AUTHENTICATION_REQUIRED" | "REQUEST_TIMEOUT" | "CONFLICT" | "GONE" | "LENGTH_REQUIRED" | "PRECONDITION_FAILED" | "PAYLOAD_TOO_LARGE" | "REQUEST_ENTITY_TOO_LARGE" | "URI_TOO_LONG" | "REQUEST_URI_TOO_LONG" | "UNSUPPORTED_MEDIA_TYPE" | "REQUESTED_RANGE_NOT_SATISFIABLE" | "EXPECTATION_FAILED" | "I_AM_A_TEAPOT" | "INSUFFICIENT_SPACE_ON_RESOURCE" | "METHOD_FAILURE" | "DESTINATION_LOCKED" | "UNPROCESSABLE_ENTITY" | "LOCKED" | "FAILED_DEPENDENCY" | "TOO_EARLY" | "UPGRADE_REQUIRED" | "PRECONDITION_REQUIRED" | "TOO_MANY_REQUESTS" | "REQUEST_HEADER_FIELDS_TOO_LARGE" | "UNAVAILABLE_FOR_LEGAL_REASONS" | "INTERNAL_SERVER_ERROR" | "NOT_IMPLEMENTED" | "BAD_GATEWAY" | "SERVICE_UNAVAILABLE" | "GATEWAY_TIMEOUT" | "HTTP_VERSION_NOT_SUPPORTED" | "VARIANT_ALSO_NEGOTIATES" | "INSUFFICIENT_STORAGE" | "LOOP_DETECTED" | "BANDWIDTH_LIMIT_EXCEEDED" | "NOT_EXTENDED" | "NETWORK_AUTHENTICATION_REQUIRED";
