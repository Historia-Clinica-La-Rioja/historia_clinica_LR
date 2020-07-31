/* tslint:disable */
/* eslint-disable */

export interface AAdditionalDoctorDto {
    fullName: string;
    generalPractitioner: boolean;
    id?: number;
    phoneNumber: string;
}

export interface APatientDto extends APersonDto {
    comments: string;
    generalPractitioner: AAdditionalDoctorDto;
    identityVerificationStatusId: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    pamiDoctor: AAdditionalDoctorDto;
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
    departmentId: number;
    floor: string;
    id: number;
    latitud: number;
    longitud: number;
    number: string;
    postcode: string;
    province: ProvinceDto;
    provinceId: number;
    quarter: string;
    street: string;
}

export interface AllergyConditionDto extends HealthConditionDto {
    categoryId: string;
    date: string;
    severity: string;
}

export interface AnamnesisDto extends DocumentDto, Serializable {
    allergies: AllergyConditionDto[];
    anthropometricData?: AnthropometricDataDto;
    confirmed: boolean;
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: HealthConditionDto;
    medications: MedicationDto[];
    notes?: DocumentObservationsDto;
    personalHistories: HealthHistoryConditionDto[];
}

export interface AnamnesisSummaryDto extends DocumentSummaryDto {
}

export interface AnthropometricDataDto extends Serializable {
    bloodType?: ClinicalObservationDto;
    bmi?: ClinicalObservationDto;
    height?: ClinicalObservationDto;
    weight?: ClinicalObservationDto;
}

export interface ApiErrorDto {
    errors: string[];
    message: string;
}

export interface ApiErrorMessageDto {
    code: string;
    text: string;
}

export interface AppointmentBasicPatientDto {
    id: number;
    person: BasicPersonalDataDto;
}

export interface AppointmentListDto {
    date: string;
    hour: string;
    id: number;
    overturn: boolean;
    patient: AppointmentBasicPatientDto;
}

export interface BMPatientDto extends APatientDto {
    id: number;
}

export interface BMPersonDto extends APersonDto {
    department: DepartmentDto;
    id: number;
    province: ProvinceDto;
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

export interface BasicDataPersonDto extends Serializable {
    age: number;
    firstName: string;
    gender: GenderDto;
    id: number;
    identificationNumber: string;
    identificationType: string;
    lastName: string;
    middleNames: string;
    otherLastNames: string;
}

export interface BasicPatientDto extends Serializable {
    id: number;
    person: BasicDataPersonDto;
}

export interface BasicPersonalDataDto extends IBasicPersonalData {
}

export interface BedCategoryDto extends MasterdataDto<number> {
    id: number;
}

export interface BedDto extends Serializable {
    bedCategory: BedCategoryDto;
    bedNumber: string;
    free: boolean;
    id: number;
    room: RoomDto;
}

export interface BedInfoDto extends Serializable {
    bed: BedDto;
    patient: BasicPatientDto;
    probableDischargeDate: string;
}

export interface CityDto extends MasterdataDto<number> {
    id: number;
}

export interface ClinicalObservationDto extends Serializable {
    id?: number;
    value: string;
}

export interface ClinicalSpecialtyDto extends Serializable {
    id: number;
    name: string;
}

export interface ClinicalTermDto extends Serializable {
    id?: number;
    snomed: SnomedDto;
    statusId?: string;
}

export interface CompleteDiaryDto extends DiaryDto {
    clinicalSpecialtyId: number;
    sectorId: number;
}

export interface CompletePatientDto extends BasicPatientDto {
    generalPractitioner?: AAdditionalDoctorDto;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    pamiDoctor?: AAdditionalDoctorDto;
    patientType: PatientType;
}

export interface CreateAppointmentDto {
    date: string;
    diaryId: number;
    hour: string;
    openingHoursId: number;
    overturn: boolean;
    patientId: number;
}

export interface CreateOutpatientDto {
    allergies: OutpatientAllergyConditionDto[];
    anthropometricData?: OutpatientAnthropometricDataDto;
    evolutionNote?: string;
    familyHistories: OutpatientFamilyHistoryDto[];
    medications: OutpatientMedicationDto[];
    problems: OutpatientProblemDto[];
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    vitalSigns?: OutpatientVitalSignDto;
}

export interface DepartmentDto extends MasterdataDto<number> {
    id: number;
}

export interface DiagnosesGeneralStateDto extends DiagnosisDto {
    main: boolean;
}

export interface DiagnosisDto extends HealthConditionDto {
    presumptive?: boolean;
}

export interface DiaryADto {
    appointmentDuration: number;
    automaticRenewal?: boolean;
    diaryOpeningHours: DiaryOpeningHoursDto[];
    doctorsOfficeId: number;
    endDate: string;
    healthcareProfessionalId: number;
    includeHoliday?: boolean;
    professionalAsignShift?: boolean;
    startDate: string;
}

export interface DiaryDto extends DiaryADto {
    id: number;
}

export interface DiaryListDto {
    appointmentDuration: number;
    doctorsOfficeId: number;
    endDate: string;
    id: number;
    includeHoliday: boolean;
    professionalAssignShift: boolean;
    startDate: string;
}

export interface DiaryOpeningHoursDto extends Overlapping<DiaryOpeningHoursDto> {
    medicalAttentionTypeId: number;
    openingHours: OpeningHoursDto;
    overturnCount?: number;
}

export interface DoctorsOfficeDto {
    closingTime: string;
    description: string;
    id: number;
    openingTime: string;
}

export interface DocumentDto {
    vitalSigns?: VitalSignDto;
}

export interface DocumentHistoricDto {
    documents: DocumentSearchDto[];
    message: string;
}

export interface DocumentObservationsDto extends Serializable {
    clinicalImpressionNote?: string;
    currentIllnessNote?: string;
    evolutionNote?: string;
    indicationsNote?: string;
    otherNote?: string;
    physicalExamNote?: string;
    studiesSummaryNote?: string;
}

export interface DocumentSearchDto extends Serializable {
    createdOn: Date;
    creator: ResponsibleDoctorDto;
    diagnosis: string[];
    id: number;
    mainDiagnosis: string;
    message: string;
    notes: DocumentObservationsDto;
}

export interface DocumentSearchFilterDto {
    plainText: string;
    searchType: EDocumentSearch;
}

export interface DocumentSummaryDto extends Serializable {
    confirmed: boolean;
    id: number;
}

export interface DocumentsSummaryDto extends Serializable {
    anamnesis: AnamnesisSummaryDto;
    epicrisis: EpicrisisSummaryDto;
    lastEvaluationNote: EvaluationNoteSummaryDto;
}

export interface EffectiveClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: string;
}

export interface EpicrisisDto extends Serializable {
    allergies: AllergyConditionDto[];
    confirmed: boolean;
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: DiagnosisDto;
    medications: MedicationDto[];
    notes?: EpicrisisObservationsDto;
    personalHistories: HealthHistoryConditionDto[];
}

export interface EpicrisisGeneralStateDto extends Serializable {
    allergies: AllergyConditionDto[];
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: HealthConditionDto;
    medications: MedicationDto[];
    personalHistories: HealthHistoryConditionDto[];
}

export interface EpicrisisObservationsDto extends Serializable {
    evolutionNote?: string;
    indicationsNote?: string;
    otherNote?: string;
    physicalExamNote?: string;
    studiesSummaryNote?: string;
}

export interface EpicrisisSummaryDto extends DocumentSummaryDto {
}

export interface EvaluationNoteSummaryDto extends DocumentSummaryDto {
}

export interface EvolutionDiagnosisDto extends Serializable {
    diagnosesId?: number[];
    notes?: DocumentObservationsDto;
}

export interface EvolutionNoteDto extends DocumentDto, Serializable {
    allergies?: AllergyConditionDto[];
    anthropometricData?: AnthropometricDataDto;
    confirmed: boolean;
    diagnosis?: DiagnosisDto[];
    immunizations?: ImmunizationDto[];
    mainDiagnosis?: HealthConditionDto;
    notes?: DocumentObservationsDto;
}

export interface GenderDto extends MasterdataDto<number> {
    id: number;
}

export interface HCEAllergyDto extends ClinicalTermDto {
}

export interface HCEAnthropometricDataDto extends Serializable {
    bloodType?: HCEClinicalObservationDto;
    bmi?: HCEClinicalObservationDto;
    height?: HCEClinicalObservationDto;
    weight?: HCEClinicalObservationDto;
}

export interface HCEClinicalObservationDto extends Serializable {
    id?: number;
    value: string;
}

export interface HCEClinicalTermDto extends Serializable {
    id?: number;
    snomed: SnomedDto;
    statusId?: string;
}

export interface HCEEffectiveClinicalObservationDto extends HCEClinicalObservationDto {
    effectiveTime: string;
}

export interface HCEImmunizationDto extends Serializable {
    administrationDate: string;
    id?: number;
    snomed: SnomedDto;
    statusId?: string;
}

export interface HCELast2VitalSignsDto extends Serializable {
    current: HCEVitalSignDto;
    previous: HCEVitalSignDto;
}

export interface HCEMedicationDto extends ClinicalTermDto {
    suspended: boolean;
}

export interface HCEPersonalHistoryDto extends HCEClinicalTermDto {
    inactivationDate: string;
    startDate: string;
}

export interface HCEVitalSignDto extends Serializable {
    bloodOxygenSaturation?: HCEEffectiveClinicalObservationDto;
    diastolicBloodPressure?: HCEEffectiveClinicalObservationDto;
    heartRate?: HCEEffectiveClinicalObservationDto;
    respiratoryRate?: HCEEffectiveClinicalObservationDto;
    systolicBloodPressure?: HCEEffectiveClinicalObservationDto;
    temperature?: HCEEffectiveClinicalObservationDto;
}

export interface HealthCareProfessionalGroupDto {
    healthcareProfessionalId: number;
    internmentEpisodeId: number;
    responsible: boolean;
}

export interface HealthConditionDto extends ClinicalTermDto {
    verificationId?: string;
}

export interface HealthHistoryConditionDto extends HealthConditionDto {
    date: string;
    note: string;
}

export interface HealthInsuranceDto {
    acronym: string;
    rnos: number;
}

export interface HealthInsurancePatientDataDto {
    id: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    person: BasicPersonalDataDto;
}

export interface HealthcareProfessionalDto {
    id: number;
    licenceNumber: string;
    person: PersonBasicDataResponseDto;
}

export interface IBasicPersonalData {
    firstName: string;
    identificationNumber: string;
    lastName: string;
}

export interface IdentificationTypeDto extends MasterdataDto<number> {
    id: number;
}

export interface ImmunizationDto extends ClinicalTermDto {
    administrationDate: string;
    note: string;
}

export interface InstitutionAddressDto {
    addressId: number;
    apartment: string;
    city: CityDto;
    floor: string;
    number: string;
    street: string;
}

export interface InstitutionDto extends Serializable {
    id: number;
    institutionAddressDto: InstitutionAddressDto;
    name: string;
    website: string;
}

export interface InternmentEpisodeADto {
    bedId: number;
    clinicalSpecialtyId: number;
    dischargeDate: Date;
    entryDate: Date;
    institutionId: number;
    noteId: number;
    patientId: number;
    responsibleContact?: ResponsibleContactDto;
    responsibleDoctorId: number;
}

export interface InternmentEpisodeBMDto extends InternmentEpisodeADto {
    id: number;
}

export interface InternmentEpisodeDto {
    bed: BedDto;
    doctor: ResponsibleDoctorDto;
    id: number;
    patient: PatientDto;
    specialty: ClinicalSpecialtyDto;
}

export interface InternmentEpisodeProcessDto {
    id?: number;
    inProgress: boolean;
}

export interface InternmentGeneralStateDto extends Serializable {
    allergies: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto;
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    medications: MedicationDto[];
    personalHistories: HealthHistoryConditionDto[];
    vitalSigns: Last2VitalSignsDto;
}

export interface InternmentPatientDto {
    birthDate: Date;
    firstName: string;
    genderId: number;
    identificationNumber: string;
    identificationTypeId: number;
    internmentId: number;
    lastName: string;
    patientId: number;
}

export interface InternmentSummaryDto {
    bed: BedDto;
    doctor: ResponsibleDoctorDto;
    documents: DocumentsSummaryDto;
    entryDate: Date;
    id: number;
    probableDischargeDate: string;
    responsibleContact?: ResponsibleContactDto;
    specialty: ClinicalSpecialtyDto;
    totalInternmentDays: number;
}

export interface JWTokenDto extends Serializable {
    refreshToken: string;
    token: string;
}

export interface Last2VitalSignsDto extends Serializable {
    current: VitalSignDto;
    previous: VitalSignDto;
}

export interface LoginDto extends Serializable {
    password: string;
    username: string;
}

export interface MainDiagnosisDto extends Serializable {
    mainDiagnosis: HealthConditionDto;
    notes: DocumentObservationsDto;
}

export interface MasterDataInterface<T> {
    description: string;
    id: T;
}

export interface MasterdataDto<T> extends MasterDataInterface<T>, Serializable {
}

export interface MedicationDto extends ClinicalTermDto {
    note: string;
    suspended: boolean;
}

export interface OauthConfigDto {
    enabled: boolean;
    loginUrl: string;
}

export interface OccupationDto {
    description: string;
    id: number;
    timeRanges: TimeRangeDto[];
}

export interface OpeningHoursDto extends TimeRangeDto {
    dayWeekId: number;
}

export interface OutpatientAllergyConditionDto {
    categoryId: string;
    severity: string;
    snomed: SnomedDto;
    startDate: string;
    statusId?: string;
    verificationId?: string;
}

export interface OutpatientAnthropometricDataDto extends Serializable {
    bloodType?: ClinicalObservationDto;
    bmi?: ClinicalObservationDto;
    height: ClinicalObservationDto;
    weight: ClinicalObservationDto;
}

export interface OutpatientFamilyHistoryDto {
    snomed: SnomedDto;
    startDate: string;
    statusId?: string;
    verificationId?: string;
}

export interface OutpatientImmunizationDto {
    administrationDate: string;
    note: string;
    snomed: SnomedDto;
}

export interface OutpatientMedicationDto {
    id?: number;
    note: string;
    snomed: SnomedDto;
    statusId?: string;
    suspended: boolean;
}

export interface OutpatientProblemDto {
    chronic: boolean;
    endDate?: string;
    snomed: SnomedDto;
    startDate: string;
    statusId?: string;
    verificationId?: string;
}

export interface OutpatientProcedureDto {
    snomed: SnomedDto;
}

export interface OutpatientReasonDto {
    snomed: SnomedDto;
}

export interface OutpatientUpdateImmunizationDto {
    administrationDate: string;
    snomed: SnomedDto;
}

export interface OutpatientVitalSignDto extends Serializable {
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    diastolicBloodPressure: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface Overlapping<T> {
}

export interface PasswordResetDto {
    password: string;
    token: string;
}

export interface PatientBedRelocationDto extends Serializable {
    destinationBedId: number;
    internmentEpisodeId: number;
    originBedFree: boolean;
    originBedId: number;
    relocationDate: string;
}

export interface PatientDischargeDto {
    administrativeDischargeDate: Date;
    dischargeTypeId: number;
    medicalDischargeDate: Date;
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

export interface PatientType extends Serializable {
    active: boolean;
    audit: boolean;
    description: string;
    id: number;
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

export interface ProbableDischargeDateDto {
    probableDischargeDate: string;
}

export interface ProfessionalDto extends BasicPersonalDataDto {
    id: number;
    licenceNumber: string;
}

export interface ProvinceDto extends MasterdataDto<number> {
    id: number;
}

export interface PublicInfoDto {
    features: AppFeature[];
    flavor: string;
}

export interface ReportClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: Date;
}

export interface ResponseAnamnesisDto extends AnamnesisDto {
    id: number;
}

export interface ResponseEpicrisisDto extends EpicrisisDto {
    id: number;
}

export interface ResponseEvolutionNoteDto extends EvolutionNoteDto {
    id: number;
}

export interface ResponsibleContactDto extends Serializable {
    fullName?: string;
    phoneNumber?: string;
    relationship?: string;
}

export interface ResponsibleDoctorDto extends Serializable {
    firstName: string;
    id: number;
    lastName: string;
    licence: string;
}

export interface RoleAssignment extends Serializable {
    institutionId: number;
    role: ERole;
}

export interface RoomDto extends Serializable {
    description: string;
    id: number;
    roomNumber: string;
    sector: SectorDto;
    type: string;
}

export interface SectorDto extends Serializable {
    description: string;
    id: number;
    specialty: ClinicalSpecialtyDto;
}

export interface Serializable {
}

export interface SnomedDto extends Serializable {
    id: string;
    parentFsn: string;
    parentId: string;
    pt: string;
}

export interface TimeRangeDto {
    from: string;
    to: string;
}

export interface UserDto extends AbstractUserDto {
    email: string;
    id: number;
    personDto?: UserPersonDto;
}

export interface UserPersonDto {
    firstName: string;
    lastName: string;
}

export interface VInstitutionDto {
    lastDateVitalSign: Date;
    latitud: number;
    longitud: number;
    patientCount: number;
    patientWithCovidPresumtiveCount: number;
    patientWithVitalSignCount: number;
}

export interface VitalSignDto extends Serializable {
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    diastolicBloodPressure?: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure?: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface VitalSignsReportDto extends Serializable {
    bloodOxygenSaturation?: ReportClinicalObservationDto;
    diastolicBloodPressure?: ReportClinicalObservationDto;
    heartRate?: ReportClinicalObservationDto;
    respiratoryRate?: ReportClinicalObservationDto;
    systolicBloodPressure?: ReportClinicalObservationDto;
    temperature?: ReportClinicalObservationDto;
}

export const enum AppFeature {
    HABILITAR_ALTA_SIN_EPICRISIS = "HABILITAR_ALTA_SIN_EPICRISIS",
    MAIN_DIAGNOSIS_REQUIRED = "MAIN_DIAGNOSIS_REQUIRED",
    RESPONSIBLE_DOCTOR_REQUIRED = "RESPONSIBLE_DOCTOR_REQUIRED",
    HABILITAR_CARGA_FECHA_PROBABLE_ALTA = "HABILITAR_CARGA_FECHA_PROBABLE_ALTA",
    HABILITAR_GESTION_DE_TURNOS = "HABILITAR_GESTION_DE_TURNOS",
    HABILITAR_HISTORIA_CLINICA_AMBULATORIA = "HABILITAR_HISTORIA_CLINICA_AMBULATORIA",
}

export const enum EDocumentSearch {
    DIAGNOSIS = "DIAGNOSIS",
    DOCTOR = "DOCTOR",
    CREATEDON = "CREATEDON",
    ALL = "ALL",
}

export const enum ERole {
    ROOT = "ROOT",
    ADMINISTRADOR = "ADMINISTRADOR",
    ESPECIALISTA_MEDICO = "ESPECIALISTA_MEDICO",
    PROFESIONAL_DE_SALUD = "PROFESIONAL_DE_SALUD",
    ADMINISTRATIVO = "ADMINISTRATIVO",
    ENFERMERO_ADULTO_MAYOR = "ENFERMERO_ADULTO_MAYOR",
    ENFERMERO = "ENFERMERO",
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE = "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE",
    ADMINISTRADOR_AGENDA = "ADMINISTRADOR_AGENDA",
}
