/* tslint:disable */
/* eslint-disable */

export interface APatientDto extends APersonDto {
    comments: string;
    identityVerificationStatusId: number;
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

export interface AddUserDto extends AbstractUserDto {
    email: string;
    password: string;
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
    allergy: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto;
    diagnosis: HealthConditionDto[];
    documentStatusId: string;
    familyHistory: HealthHistoryConditionDto[];
    inmunization: InmunizationDto[];
    medication: MedicationDto[];
    notes: ObservationsDto;
    personalHistory: HealthHistoryConditionDto[];
    vitalSigns: VitalSignDto;
}

export interface AnthropometricDataDto extends Serializable {
    bloodType: string;
    heigth: string;
    weigth: string;
}

export interface ApiError {
    errors: string[];
    message: string;
    status: HttpStatus;
}

export interface BMPatientDto extends APatientDto {
    id: number;
}

export interface BMPersonDto extends APersonDto {
    id: number;
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

export interface ClinicalSpecialtyDto {
    id: number;
    name: string;
}

export interface ClinicalTermDto extends Serializable {
    snomed: SnomedDto;
    statusId: string;
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
    id: number;
    rnos: string;
}

export interface IdentificationTypeDto extends MasterdataDto<number> {
    id: number;
}

export interface InmunizationDto extends ClinicalTermDto {
    date: string;
    note: string;
}

export interface InternmentEpisodeDto {
    bed: BedDto;
    doctor: ResponsibleDoctorDto;
    id: number;
    patient: PatientDto;
    specialty: ClinicalSpecialtyDto;
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

export interface MasterdataDto<T> extends MasterDataInterface<T> {
}

export interface MedicationDto extends ClinicalTermDto {
    note: string;
}

export interface ObservationsDto extends Serializable {
    clinicalReport: string;
    evolution: string;
    extra: string;
    physicalExamination: string;
    presentIllness: string;
    procedure: string;
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

export interface PersonalInformationDto {
    address: AddressDto;
    birthDate: Date;
    cuil: string;
    email: string;
    healthInsurance: HealthInsuranceDto;
    id: number;
    identificationNumber: string;
    identificationType: IdentificationTypeDto;
    phoneNumber: string;
}

export interface ProvinceDto extends MasterdataDto<number> {
    id: number;
}

export interface RequestUserRoleDto extends Serializable {
    roleId: number;
    userId: number;
}

export interface ResponsibleDoctorDto extends Serializable {
    firstName: string;
    id: number;
    lastName: string;
    licence: string;
}

export interface RoomDto extends Serializable {
    id: number;
    roomNumber: string;
    sector: SectorDto;
}

export interface SectorDto {
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
    bloodOxygenSaturation: string;
    diastolicBloodPresure: string;
    heartRate: string;
    meanPresure: string;
    respiratoryRate: string;
    systolicBloodPresure: string;
    temperature: string;
}

export type HttpStatus = "CONTINUE" | "SWITCHING_PROTOCOLS" | "PROCESSING" | "CHECKPOINT" | "OK" | "CREATED" | "ACCEPTED" | "NON_AUTHORITATIVE_INFORMATION" | "NO_CONTENT" | "RESET_CONTENT" | "PARTIAL_CONTENT" | "MULTI_STATUS" | "ALREADY_REPORTED" | "IM_USED" | "MULTIPLE_CHOICES" | "MOVED_PERMANENTLY" | "FOUND" | "MOVED_TEMPORARILY" | "SEE_OTHER" | "NOT_MODIFIED" | "USE_PROXY" | "TEMPORARY_REDIRECT" | "PERMANENT_REDIRECT" | "BAD_REQUEST" | "UNAUTHORIZED" | "PAYMENT_REQUIRED" | "FORBIDDEN" | "NOT_FOUND" | "METHOD_NOT_ALLOWED" | "NOT_ACCEPTABLE" | "PROXY_AUTHENTICATION_REQUIRED" | "REQUEST_TIMEOUT" | "CONFLICT" | "GONE" | "LENGTH_REQUIRED" | "PRECONDITION_FAILED" | "PAYLOAD_TOO_LARGE" | "REQUEST_ENTITY_TOO_LARGE" | "URI_TOO_LONG" | "REQUEST_URI_TOO_LONG" | "UNSUPPORTED_MEDIA_TYPE" | "REQUESTED_RANGE_NOT_SATISFIABLE" | "EXPECTATION_FAILED" | "I_AM_A_TEAPOT" | "INSUFFICIENT_SPACE_ON_RESOURCE" | "METHOD_FAILURE" | "DESTINATION_LOCKED" | "UNPROCESSABLE_ENTITY" | "LOCKED" | "FAILED_DEPENDENCY" | "TOO_EARLY" | "UPGRADE_REQUIRED" | "PRECONDITION_REQUIRED" | "TOO_MANY_REQUESTS" | "REQUEST_HEADER_FIELDS_TOO_LARGE" | "UNAVAILABLE_FOR_LEGAL_REASONS" | "INTERNAL_SERVER_ERROR" | "NOT_IMPLEMENTED" | "BAD_GATEWAY" | "SERVICE_UNAVAILABLE" | "GATEWAY_TIMEOUT" | "HTTP_VERSION_NOT_SUPPORTED" | "VARIANT_ALSO_NEGOTIATES" | "INSUFFICIENT_STORAGE" | "LOOP_DETECTED" | "BANDWIDTH_LIMIT_EXCEEDED" | "NOT_EXTENDED" | "NETWORK_AUTHENTICATION_REQUIRED";
