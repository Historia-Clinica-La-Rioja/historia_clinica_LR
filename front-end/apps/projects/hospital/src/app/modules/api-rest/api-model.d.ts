/* tslint:disable */
/* eslint-disable */

export interface AAdditionalDoctorDto {
    fullName: string;
    generalPractitioner: boolean;
    id?: number;
    phoneNumber: string;
}

export interface AEmergencyCareDto extends Serializable {
    ambulanceCompanyId?: string;
    emergencyCareTypeId?: number;
    entranceTypeId?: number;
    hasPoliceIntervention?: boolean;
    patient?: AEmergencyCarePatientDto;
    policeInterventionDetails?: PoliceInterventionDetailsDto;
    reasons?: SnomedDto[];
}

export interface AEmergencyCarePatientDto extends Serializable {
    id?: number;
    patientMedicalCoverageId?: number;
}

export interface AMedicalDischargeDto extends MedicalDischargeDto {
    dischargeTypeId: number;
    problems: OutpatientProblemDto[];
}

export interface APatientDto extends APersonDto {
    comments: string;
    generalPractitioner: AAdditionalDoctorDto;
    identityVerificationStatusId: number;
    pamiDoctor: AAdditionalDoctorDto;
    typeId: number;
}

export interface APersonDto {
    apartment: string;
    birthDate: Date;
    cityId: number;
    cuil: string;
    educationLevelId: number;
    email: string;
    ethnicityId: number;
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
    occupationId: number;
    otherGenderSelfDetermination?: string;
    otherLastNames: string;
    phoneNumber: string;
    postcode: string;
    quarter: string;
    religion: string;
    street: string;
}

export interface AbstractMasterdataDto<T> extends MasterDataInterface<T>, Serializable {
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
    latitude: number;
    longitude: number;
    number: string;
    postcode: string;
    province: ProvinceDto;
    provinceId: number;
    quarter: string;
    street: string;
}

export interface AdministrativeDischargeDto {
    administrativeDischargeOn: DateTimeDto;
    ambulanceCompanyId: string;
    hospitalTransportId: number;
}

export interface AllergyConditionDto extends HealthConditionDto {
    categoryId: number;
    criticalityId: number;
    date: string;
}

export interface AllergyIntoleranceDto {
    category: string;
    clinicalStatus: FhirCodeDto;
    criticality: string;
    id: string;
    sctidCode: string;
    sctidTerm: string;
    startDate: Date;
    type: string;
    verificationStatus: FhirCodeDto;
}

export interface AnamnesisDto extends Serializable {
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
    procedures?: HospitalizationProcedureDto[];
    vitalSigns?: VitalSignDto;
}

export interface AnamnesisSummaryDto extends DocumentSummaryDto {
}

export interface AnnexIIDto {
    affiliateNumber: string;
    appointmentState: string;
    attentionDate: Date;
    completePatientName: string;
    consultationDate: Date;
    documentNumber: string;
    documentType: string;
    establishment: string;
    existsConsultation: boolean;
    hasProcedures: boolean;
    medicalCoverage: string;
    patientAge: number;
    patientGender: string;
    problems: string;
    reportDate: Date;
    sisaCode: string;
    specialty: string;
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

export interface ApiKeyInfoDto {
    id: number;
}

export interface AppearanceDto extends Serializable {
    bodyTemperatureId?: number;
    cryingExcessive?: boolean;
    muscleHypertoniaId?: number;
}

export interface ApplicationVersionDto {
    version: string;
}

export interface AppointmentBasicPatientDto {
    id: number;
    person: BasicPersonalDataDto;
    typeId: number;
}

export interface AppointmentDailyAmountDto {
    date: string;
    programmed: number;
    programmedAvailable: number;
    spontaneous: number;
}

export interface AppointmentDto extends CreateAppointmentDto {
    appointmentStateId: number;
    stateChangeReason?: string;
}

export interface AppointmentListDto {
    appointmentStateId: number;
    date: string;
    healthInsuranceId: number;
    hour: string;
    id: number;
    medicalAttentionTypeId: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    overturn: boolean;
    patient: AppointmentBasicPatientDto;
    phoneNumber: string;
}

export interface AttentionTypeReportDto {
    appointments: AttentionTypeReportItemDto[];
    medicalAttentionTypeId: number;
    openingHourFrom: Date;
    openingHourTo: Date;
}

export interface AttentionTypeReportItemDto {
    appointmentState: string;
    firstName: string;
    hour: Date;
    identificationNumber: string;
    identificationType: string;
    lastName: string;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    middleNames: string;
    otherLastNames: string;
    patientId: number;
    patientMedicalCoverageId: number;
}

export interface BMPatientDto extends APatientDto {
    id: number;
}

export interface BMPersonDto extends APersonDto {
    department: DepartmentDto;
    id: number;
    province: ProvinceDto;
}

export interface BackofficeHealthcareProfessionalCompleteDto {
    clinicalSpecialtyId: number;
    deleted: boolean;
    id: number;
    licenseNumber: string;
    personId: number;
    professionalSpecialtyId: number;
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
    birthDate: Date;
    firstName: string;
    gender: GenderDto;
    id: number;
    identificationNumber: string;
    identificationType: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    otherLastNames: string;
}

export interface BasicPatientDto extends Serializable {
    id: number;
    person: BasicDataPersonDto;
    typeId: number;
}

export interface BasicPersonalDataDto extends IBasicPersonalData {
    genderId: number;
}

export interface BedCategoriesDataDto {
    category: BedCategoryDto;
    freeBeds: number;
    occupiedBeds: number;
}

export interface BedCategoryDto extends AbstractMasterdataDto<number> {
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

export interface BedSummaryDto {
    bed: BedDto;
    probableDischargeDate?: string;
    sector: SectorSummaryDto;
}

export interface BreathingDto extends Serializable {
    bloodOxygenSaturation?: NewEffectiveClinicalObservationDto;
    respiratoryRate?: NewEffectiveClinicalObservationDto;
    respiratoryRetractionId?: number;
    stridor?: boolean;
}

export interface ChangeStateMedicationRequestDto extends Serializable {
    dayQuantity?: number;
    medicationsIds: number[];
    observations?: string;
}

export interface CirculationDto extends Serializable {
    heartRate: NewEffectiveClinicalObservationDto;
    perfusionId: number;
}

export interface CityDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface ClinicalObservationDto extends Serializable {
    id?: number;
    value?: string;
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

export interface CompleteRequestDto {
    fileIds?: number[];
    link?: string;
    observations?: string;
}

export interface ConditionDto {
    clinicalStatus: FhirCodeDto;
    createdOn: Date;
    id: string;
    sctidCode: string;
    sctidTerm: string;
    severityCode: FhirCodeDto;
    startDate: Date;
    verificationStatus: FhirCodeDto;
}

export interface ConsultationsDto extends Serializable {
    completeProfessionalName: string;
    consultationDate: Date;
    documentId: number;
    id: number;
    specialty: string;
}

export interface CoverageDto extends Serializable {
    id: number;
    name: string;
    type: "HealthInsuranceDto" | "PrivateHealthInsuranceDto";
}

export interface CreateAppointmentDto {
    date: string;
    diaryId: number;
    hour: string;
    openingHoursId: number;
    overturn: boolean;
    patientId: number;
    patientMedicalCoverageId?: number;
    phoneNumber?: string;
}

export interface CreateOutpatientDto {
    allergies: OutpatientAllergyConditionDto[];
    anthropometricData?: OutpatientAnthropometricDataDto;
    clinicalSpecialtyId?: number;
    evolutionNote?: string;
    familyHistories: OutpatientFamilyHistoryDto[];
    medications: OutpatientMedicationDto[];
    problems: OutpatientProblemDto[];
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    vitalSigns?: OutpatientVitalSignDto;
}

export interface DateDto {
    day: number;
    month: number;
    year: number;
}

export interface DateTimeDto {
    date: DateDto;
    time: TimeDto;
}

export interface DentalActionDto extends ClinicalTermDto {
    diagnostic: boolean;
    surface: SnomedDto;
    tooth: SnomedDto;
}

export interface DepartmentDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface DependencyDto extends Serializable {
    code: string;
    description: string;
    id: number;
}

export interface DiagnosesGeneralStateDto extends DiagnosisDto {
    main: boolean;
}

export interface DiagnosisDto extends HealthConditionDto {
    presumptive?: boolean;
}

export interface DiagnosticReportDto extends ClinicalTermDto {
    effectiveTime: Date;
    encounterId: number;
    files: FileDto[];
    healthCondition: HealthConditionDto;
    healthConditionId: number;
    link: string;
    noteId: number;
    observations: string;
    userId: number;
}

export interface DiagnosticReportInfoDto {
    doctor: DoctorInfoDto;
    healthCondition: HealthConditionInfoDto;
    id: number;
    link?: string;
    observations?: string;
    serviceRequestId: number;
    snomed: SnomedDto;
    statusId: string;
    totalDays: number;
}

export interface DiagnosticReportInfoWithFilesDto extends DiagnosticReportInfoDto {
    files: FileDto[];
}

export interface DiaryADto {
    appointmentDuration: number;
    automaticRenewal?: boolean;
    diaryOpeningHours: DiaryOpeningHoursDto[];
    doctorsOfficeId: number;
    endDate: string;
    healthcareProfessionalId: number;
    includeHoliday?: boolean;
    professionalAssignShift?: boolean;
    startDate: string;
}

export interface DiaryDto extends DiaryADto {
    id: number;
}

export interface DiaryListDto {
    appointmentDuration: number;
    doctorsOfficeDescription: string;
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

export interface DoctorInfoDto {
    firstName: string;
    id: number;
    lastName: string;
}

export interface DoctorsOfficeDto {
    closingTime: string;
    description: string;
    id: number;
    openingTime: string;
}

export interface DocumentDto {
    allergies: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto;
    clinicalSpecialtyId: number;
    dentalActions: DentalActionDto[];
    diagnosis: DiagnosisDto[];
    diagnosticReports: DiagnosticReportDto[];
    documentSource: number;
    documentType: number;
    encounterId: number;
    familyHistories: HealthHistoryConditionDto[];
    id: number;
    immunizations: ImmunizationDto[];
    institutionId: number;
    mainDiagnosis: HealthConditionDto;
    medicalCoverageId: number;
    medications: MedicationDto[];
    notes: DocumentObservationsDto;
    patientId: number;
    personalHistories: HealthHistoryConditionDto[];
    problems: ProblemDto[];
    procedures: ProcedureDto[];
    reasons: ReasonDto[];
    vitalSigns: VitalSignDto;
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
    procedures: ProcedureReduced[];
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

export interface DosageInfoDto extends Serializable {
    chronic: boolean;
    dailyInterval: boolean;
    duration: number;
    expired: boolean;
    frequency: number;
    id: number;
    periodUnit: string;
    startDate: DateDto;
}

export interface DrawingsDto extends Serializable {
    central?: string;
    external?: string;
    internal?: string;
    left?: string;
    right?: string;
    whole?: string;
}

export interface ECAdministrativeDto extends Serializable {
    administrative: NewEmergencyCareDto;
    triage: TriageAdministrativeDto;
}

export interface ECAdultGynecologicalDto extends Serializable {
    administrative: NewEmergencyCareDto;
    triage: TriageAdultGynecologicalDto;
}

export interface ECPediatricDto extends Serializable {
    administrative: NewEmergencyCareDto;
    triage: TriagePediatricDto;
}

export interface EducationLevelDto extends Serializable {
    code: number;
    description: string;
    id: number;
}

export interface EffectiveClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: string;
}

export interface EmergencyCareDto extends Serializable {
    ambulanceCompanyId: string;
    emergencyCareType: MasterDataDto;
    entranceType: MasterDataDto;
    hasPoliceIntervention: boolean;
    patient: EmergencyCarePatientDto;
    policeInterventionDetails: PoliceInterventionDetailsDto;
    reasons: SnomedDto[];
}

export interface EmergencyCareEpisodeListTriageDto {
    color: string;
    id: number;
    name: string;
}

export interface EmergencyCareListDto extends Serializable {
    creationDate: DateTimeDto;
    doctorsOffice: DoctorsOfficeDto;
    id: number;
    patient: EmergencyCarePatientDto;
    state: MasterDataDto;
    triage: EmergencyCareEpisodeListTriageDto;
    type: MasterDataDto;
}

export interface EmergencyCarePatientDto extends Serializable {
    id: number;
    patientMedicalCoverageId: number;
    person: EmergencyCarePersonDto;
    typeId: number;
}

export interface EmergencyCarePersonDto {
    firstName: string;
    identificationNumber: string;
    lastName: string;
}

export interface EmergencyCareUserDto {
    firstName: string;
    id: number;
    lastName: string;
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

export interface EthnicityDto extends Serializable {
    id: number;
    pt: string;
    sctid: string;
}

export interface EvaluationNoteSummaryDto extends DocumentSummaryDto {
}

export interface EvolutionDiagnosisDto extends Serializable {
    diagnosesId?: number[];
    notes?: DocumentObservationsDto;
}

export interface EvolutionNoteDto extends Serializable {
    allergies?: AllergyConditionDto[];
    anthropometricData?: AnthropometricDataDto;
    confirmed: boolean;
    diagnosis?: DiagnosisDto[];
    immunizations?: ImmunizationDto[];
    mainDiagnosis?: HealthConditionDto;
    notes?: DocumentObservationsDto;
    procedures?: HospitalizationProcedureDto[];
    vitalSigns?: VitalSignDto;
}

export interface ExternalClinicalHistoryDto extends Serializable {
    consultationDate: DateDto;
    id: number;
    institution?: string;
    notes: string;
    professionalName?: string;
    professionalSpecialty?: string;
}

export interface FhirAddressDto {
    address: string;
    city: string;
    country: string;
    postcode: string;
    province: string;
}

export interface FhirCodeDto {
    theCode: string;
    theDisplay: string;
}

export interface FileDto {
    fileId: number;
    fileName: string;
}

export interface FormVDto {
    address: string;
    affiliateNumber: string;
    cie10Codes: string;
    completePatientName: string;
    consultationDate: Date;
    documentNumber: string;
    documentType: string;
    establishment: string;
    medicalCoverage: string;
    patientAge: number;
    patientGender: string;
    problems: string;
    reportDate: Date;
    sisaCode: string;
}

export interface GenderDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface HCEAllergyDto extends ClinicalTermDto {
    categoryId: number;
    criticalityId: number;
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

export interface HCEDiagnoseDto extends ClinicalTermDto {
    main: boolean;
}

export interface HCEEffectiveClinicalObservationDto extends HCEClinicalObservationDto {
    effectiveTime: string;
}

export interface HCEHospitalizationHistoryDto {
    alternativeDiagnoses: HCEDiagnoseDto[];
    dischargeDate: string;
    entryDate: string;
    mainDiagnose: HCEDiagnoseDto;
    sourceId: number;
}

export interface HCEImmunizationDto extends Serializable {
    administrationDate: string;
    condition?: VaccineConditionDto;
    doctor?: ProfessionalInfoDto;
    dose?: VaccineDoseInfoDto;
    id: number;
    institution?: InstitutionInfoDto;
    lotNumber: string;
    note: string;
    scheme?: VaccineSchemeInfoDto;
    snomed: SnomedDto;
    statusId: string;
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
    severity: string;
    startDate: string;
}

export interface HCEToothRecordDto extends Serializable {
    date: DateDto;
    snomed: SnomedDto;
    surfaceSctid?: string;
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

export interface HealthConditionInfoDto extends Serializable {
    id: number;
    snomed: SnomedDto;
}

export interface HealthConditionNewConsultationDto extends Serializable {
    id: number;
    inactivationDate: Date;
    isChronic: boolean;
    main: boolean;
    noteId: number;
    patientId: number;
    problemId: string;
    sctidCode: string;
    severity: string;
    snomed: SnomedDto;
    startDate: Date;
    statusId: string;
    verificationStatusId: string;
}

export interface HealthHistoryConditionDto extends HealthConditionDto {
    date: string;
    note: string;
}

export interface HealthInsuranceDto extends CoverageDto {
    acronym: string;
    rnos: number;
    type: "HealthInsuranceDto";
}

export interface HealthcareProfessionalDto {
    id: number;
    licenseNumber: string;
    person: PersonBasicDataResponseDto;
}

export interface HospitalizationProcedureDto {
    performedDate?: string;
    snomed: SnomedDto;
}

export interface IBasicPersonalData {
    firstName: string;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    phoneNumber: string;
}

export interface IdentificationTypeDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface IdentifierDto extends Serializable {
    system: string;
    value: string;
}

export interface ImmunizationDto extends ClinicalTermDto {
    administrationDate: string;
    billable?: boolean;
    conditionId?: number;
    doctorInfo?: string;
    dose?: VaccineDoseInfoDto;
    institutionId?: number;
    institutionInfo?: string;
    lotNumber?: string;
    note: string;
    schemeId?: number;
}

export interface ImmunizationInteroperabilityDto {
    administrationDate: Date;
    batchNumber: string;
    doseNumber: number;
    expirationDate: Date;
    id: string;
    immunizationCode: string;
    immunizationTerm: string;
    primarySource: boolean;
    series: string;
    status: string;
    vaccineCode: string;
    vaccineTerm: string;
}

export interface ImmunizePatientDto {
    clinicalSpecialtyId: number;
    immunizations: ImmunizationDto[];
}

export interface InstitutionAddressDto extends Serializable {
    addressId: number;
    apartment: string;
    city: CityDto;
    floor: string;
    number: string;
    street: string;
}

export interface InstitutionBasicInfoDto extends Serializable {
    id: number;
    name: string;
}

export interface InstitutionDto extends Serializable {
    id: number;
    institutionAddressDto: InstitutionAddressDto;
    name: string;
    website: string;
}

export interface InstitutionInfoDto extends Serializable {
    id: number;
    name: string;
    sisaCode: string;
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

export interface LimitedPatientSearchDto {
    actualPatientSearchSize: number;
    patientList: PatientSearchDto[];
}

export interface LoginDto extends Serializable {
    password: string;
    username: string;
}

export interface MainDiagnosisDto extends Serializable {
    mainDiagnosis: HealthConditionDto;
    notes: DocumentObservationsDto;
}

export interface MasterDataDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface MasterDataInterface<T> {
    description: string;
    id: T;
}

export interface MedicalCoverageDto {
    acronym: string;
    dateQuery: string;
    id: number;
    name: string;
    rnos: string;
    service: string;
}

export interface MedicalDischargeDto {
    autopsy: boolean;
    medicalDischargeOn: DateTimeDto;
}

export interface MedicalRequestDto {
    healthConditionSnomed: SnomedDto;
    observations: string;
    startDate: DateDto;
    statusId: number;
}

export interface MedicationDto extends ClinicalTermDto {
    note: string;
    suspended: boolean;
}

export interface MedicationInfoDto extends Serializable {
    createdOn: DateDto;
    doctor: DoctorInfoDto;
    dosage: DosageInfoDto;
    hasRecipe: boolean;
    healthCondition: HealthConditionInfoDto;
    id: number;
    medicationRequestId: number;
    observations: string;
    snomed: SnomedDto;
    statusId: string;
    totalDays: number;
}

export interface MedicationIngredientDto {
    active: boolean;
    presentationUnit: string;
    presentationValue: number;
    sctidCode: string;
    sctidTerm: string;
    unitMeasure: string;
    unitValue: number;
}

export interface MedicationInteroperabilityDto {
    doseQuantityCode: string;
    doseQuantityUnit: string;
    doseQuantityValue: number;
    effectiveTimeEnd: Date;
    effectiveTimeStart: Date;
    formCode: string;
    formTerm: string;
    id: string;
    ingredients: MedicationIngredientDto[];
    routeCode: string;
    routeTerm: string;
    sctidCode: string;
    sctidTerm: string;
    statementId: string;
    status: string;
    unitTime: string;
}

export interface MqttMetadataDto {
    message: string;
    qos: number;
    retained: boolean;
    topic: string;
    type: string;
}

export interface NewDosageDto extends Serializable {
    chronic: boolean;
    diary: boolean;
    duration?: number;
    frequency?: number;
}

export interface NewEffectiveClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: DateTimeDto;
}

export interface NewEmergencyCareDto extends AEmergencyCareDto {
    doctorsOfficeId?: number;
}

export interface NewMedicalRequestDto {
    healthConditionId: number;
    observations: string;
}

export interface NewServiceRequestListDto extends Serializable {
    medicalCoverageId: number;
    studiesDto: StudyDto[];
}

export interface NewVitalSignsObservationDto extends Serializable {
    bloodOxygenSaturation?: NewEffectiveClinicalObservationDto;
    diastolicBloodPressure?: NewEffectiveClinicalObservationDto;
    heartRate?: NewEffectiveClinicalObservationDto;
    respiratoryRate?: NewEffectiveClinicalObservationDto;
    systolicBloodPressure?: NewEffectiveClinicalObservationDto;
    temperature?: NewEffectiveClinicalObservationDto;
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

export interface OdontogramQuadrantDto {
    code: number;
    left: boolean;
    permanent: boolean;
    snomed: OdontologySnomedDto;
    teeth: ToothDto[];
    top: boolean;
}

export interface OdontologyAllergyConditionDto extends Serializable {
    categoryId: string;
    criticalityId: number;
    snomed: SnomedDto;
    startDate: DateDto;
    statusId?: string;
    verificationId?: string;
}

export interface OdontologyConceptDto extends Serializable {
    applicableToSurface: boolean;
    applicableToTooth: boolean;
    snomed: OdontologySnomedDto;
}

export interface OdontologyConsultationDto extends Serializable {
    allergies?: OdontologyAllergyConditionDto[];
    clinicalSpecialtyId: number;
    dentalActions?: OdontologyDentalActionDto[];
    diagnostics?: OdontologyDiagnosticDto[];
    evolutionNote?: string;
    medications?: OdontologyMedicationDto[];
    permanentTeethPresent?: number;
    personalHistories?: OdontologyPersonalHistoryDto[];
    procedures?: OdontologyProcedureDto[];
    reasons?: OdontologyReasonDto[];
    temporaryTeethPresent?: number;
}

export interface OdontologyConsultationIndicesDto extends Serializable {
    ceoIndex: number;
    cpoIndex: number;
    date: DateDto;
    permanentC: number;
    permanentO: number;
    permanentP: number;
    permanentTeethPresent: number;
    temporaryC: number;
    temporaryE: number;
    temporaryO: number;
    temporaryTeethPresent: number;
}

export interface OdontologyDentalActionDto extends Serializable {
    diagnostic: boolean;
    snomed: SnomedDto;
    surfacePosition?: ESurfacePositionDto;
    tooth: SnomedDto;
}

export interface OdontologyDiagnosticDto extends Serializable {
    chronic: boolean;
    endDate?: DateDto;
    severity: string;
    snomed: SnomedDto;
    startDate: DateDto;
}

export interface OdontologyMedicationDto {
    id?: number;
    note: string;
    snomed: SnomedDto;
    statusId?: string;
    suspended: boolean;
}

export interface OdontologyPersonalHistoryDto extends Serializable {
    snomed: SnomedDto;
    startDate: DateDto;
    statusId?: string;
    verificationId?: string;
}

export interface OdontologyProcedureDto extends Serializable {
    performedDate?: DateDto;
    snomed: SnomedDto;
}

export interface OdontologyReasonDto extends Serializable {
    snomed: SnomedDto;
}

export interface OdontologySnomedDto {
    id: number;
    pt: string;
    sctid: string;
}

export interface OpeningHoursDto extends TimeRangeDto {
    dayWeekId: number;
    id?: number;
}

export interface OrganizationDto extends Serializable {
    address: FhirAddressDto;
    custodian: string;
    id: string;
    identifier: IdentifierDto;
    name: string;
    phoneNumber: string;
}

export interface OutpatientAllergyConditionDto {
    categoryId: string;
    criticalityId: number;
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

export interface OutpatientEvolutionSummaryDto extends Serializable {
    clinicalSpecialty: ClinicalSpecialtyDto;
    consultationID: number;
    evolutionNote: string;
    healthConditions: OutpatientSummaryHealthConditionDto[];
    medic: HealthcareProfessionalDto;
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    startDate: string;
}

export interface OutpatientFamilyHistoryDto {
    snomed: SnomedDto;
    startDate: string;
    statusId?: string;
    verificationId?: string;
}

export interface OutpatientImmunizationDto {
    administrationDate: string;
    clinicalSpecialtyId?: number;
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
    severity: string;
    snomed: SnomedDto;
    startDate: string;
    statusId?: string;
    verificationId?: string;
}

export interface OutpatientProcedureDto {
    performedDate?: string;
    snomed: SnomedDto;
}

export interface OutpatientReasonDto {
    snomed: SnomedDto;
}

export interface OutpatientSummaryHealthConditionDto extends ClinicalTermDto {
    inactivationDate: string;
    main: boolean;
    problemId: string;
    startDate: string;
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
    fullName: string;
    id: number;
    lastName: string;
}

export interface PatientInfoDto {
    age: number;
    genderId: number;
    id: number;
}

export interface PatientInteroperabilityDto {
    birthdate: string;
    firstname: string;
    fullAddress: FhirAddressDto;
    gender: string;
    id: string;
    identificationNumber: string;
    lastname: string;
    middlenames: string;
    otherLastName: string;
    phoneNumber: string;
}

export interface PatientMedicalCoverageDto {
    active: boolean;
    affiliateNumber?: string;
    id?: number;
    medicalCoverage: CoverageDtoUnion;
    privateHealthInsuranceDetails?: PrivateHealthInsuranceDetailsDto;
    vigencyDate?: string;
}

export interface PatientPhotoDto {
    imageData: string;
    patientId: number;
}

export interface PatientSearchDto {
    activo: boolean;
    idPatient: number;
    person: BMPersonDto;
    ranking: number;
}

export interface PatientSummaryDto {
    allergies: AllergyIntoleranceDto[];
    conditions: ConditionDto[];
    immunizations: ImmunizationInteroperabilityDto[];
    medications: MedicationInteroperabilityDto[];
    organization: OrganizationDto;
    patient: PatientInteroperabilityDto;
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
    cuil: string;
    firstName: string;
    lastName: string;
    photo: string;
}

export interface PersonOccupationDto extends Serializable {
    code: number;
    description: string;
    id: number;
}

export interface PersonPhotoDto {
    imageData: string;
    personId?: number;
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

export interface PoliceInterventionDetailsDto extends Serializable {
    callDate: DateDto;
    callTime: TimeDto;
    firstName: string;
    lastName: string;
    plateNumber: string;
}

export interface PrescriptionDto extends Serializable {
    hasRecipe: boolean;
    items: PrescriptionItemDto[];
    medicalCoverageId: number;
}

export interface PrescriptionItemDto extends Serializable {
    categoryId?: string;
    dosage?: NewDosageDto;
    healthConditionId: number;
    observations?: string;
    snomed: SnomedDto;
}

export interface PrivateHealthInsuranceDetailsDto {
    endDate: string;
    id: number;
    startDate: string;
}

export interface PrivateHealthInsuranceDto extends CoverageDto {
    plan: string;
    type: "PrivateHealthInsuranceDto";
}

export interface ProbableDischargeDateDto {
    probableDischargeDate: string;
}

export interface ProblemDto extends HealthConditionDto {
    chronic: boolean;
    endDate: Date;
    severity: string;
    startDate: Date;
}

export interface ProcedureDto {
    performedDate?: string;
    snomed: SnomedDto;
}

export interface ProcedureReduced {
    performedDate: Date;
    procedure: string;
}

export interface ProfessionalDto {
    completeName: string;
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    phoneNumber: string;
}

export interface ProfessionalInfoDto {
    clinicalSpecialties: ClinicalSpecialtyDto[];
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    phoneNumber: string;
}

export interface ProfessionalsByClinicalSpecialtyDto {
    clinicalSpecialty: ClinicalSpecialtyDto;
    professionalsIds: number[];
}

export interface ProvinceDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface PublicInfoDto {
    features: AppFeature[];
    flavor: string;
}

export interface ReasonDto {
    snomed: SnomedDto;
}

export interface RecaptchaPublicConfigDto {
    enabled: boolean;
    siteKey: string;
}

export interface ReducedPatientDto {
    patientTypeId: number;
    personalDataDto: BasicPersonalDataDto;
}

export interface RefreshTokenDto {
    refreshToken: string;
}

export interface ReportClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: Date;
}

export interface ResponseAnamnesisDto extends AnamnesisDto {
    id: number;
}

export interface ResponseEmergencyCareDto extends EmergencyCareDto {
    creationDate: DateTimeDto;
    doctorsOffice: DoctorsOfficeDto;
    emergencyCareState: MasterDataDto;
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
}

export interface SectorSummaryDto {
    ageGroup: string;
    ageGroupId: number;
    careType: string;
    careTypeId: number;
    clinicalSpecialties: ClinicalSpecialtyDto[];
    description: string;
    id: number;
    organizationType: string;
    organizationTypeId: number;
    sectorType: string;
    sectorTypeId: number;
}

export interface SelfPerceivedGenderDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface Serializable {
}

export interface SnomedDto extends Serializable {
    id?: number;
    parentFsn?: string;
    parentId?: string;
    pt: string;
    sctid: string;
}

export interface SnomedResponseDto extends Serializable {
    items: SnomedDto[];
    total: number;
}

export interface StudyDto extends Serializable {
    diagosticReportCategoryId: string;
    healthConditionId: number;
    observations?: string;
    snomed: SnomedDto;
}

export interface TimeDto {
    hours: number;
    minutes: number;
    seconds?: number;
}

export interface TimeRangeDto {
    from: string;
    to: string;
}

export interface ToothDrawingsDto extends Serializable {
    drawings: DrawingsDto;
    toothSctid: string;
}

export interface ToothDto {
    code: number;
    snomed: OdontologySnomedDto;
}

export interface ToothSurfacesDto {
    central: OdontologySnomedDto;
    external: OdontologySnomedDto;
    internal: OdontologySnomedDto;
    left: OdontologySnomedDto;
    right: OdontologySnomedDto;
}

export interface TriageAdministrativeDto extends TriageDto {
}

export interface TriageAdultGynecologicalDto extends TriageNoAdministrativeDto {
    vitalSigns: NewVitalSignsObservationDto;
}

export interface TriageAppearanceDto extends Serializable {
    bodyTemperature: MasterDataDto;
    cryingExcessive: boolean;
    muscleHypertonia: MasterDataDto;
}

export interface TriageBreathingDto extends Serializable {
    bloodOxygenSaturation: NewEffectiveClinicalObservationDto;
    respiratoryRate: NewEffectiveClinicalObservationDto;
    respiratoryRetraction: MasterDataDto;
    stridor: boolean;
}

export interface TriageCategoryDto extends Serializable {
    color: TriageColorDto;
    description: string;
    id: number;
    name: string;
}

export interface TriageCirculationDto extends Serializable {
    heartRate: NewEffectiveClinicalObservationDto;
    perfusion: MasterDataDto;
}

export interface TriageColorDto extends Serializable {
    code: string;
    name: string;
}

export interface TriageDto extends Serializable {
    categoryId: number;
    doctorsOfficeId?: number;
}

export interface TriageListDto extends Serializable {
    appearance: TriageAppearanceDto;
    breathing: TriageBreathingDto;
    category: TriageCategoryDto;
    circulation: TriageCirculationDto;
    createdBy: EmergencyCareUserDto;
    creationDate: DateTimeDto;
    doctorsOffice: DoctorsOfficeDto;
    id: number;
    notes: string;
    vitalSigns: NewVitalSignsObservationDto;
}

export interface TriageNoAdministrativeDto extends TriageDto {
    notes?: string;
}

export interface TriagePediatricDto extends TriageNoAdministrativeDto {
    appearance?: AppearanceDto;
    breathing?: BreathingDto;
    circulation?: CirculationDto;
}

export interface UIComponentDto {
    actions: UIComponentDto[];
    args: { [index: string]: any };
    children: UIComponentDto[];
    type: string;
}

export interface UILabelDto {
    key?: string;
    text?: string;
}

export interface UIMenuItemDto {
    icon: string;
    id: string;
    label: UILabelDto;
}

export interface UIPageDto {
    content: UIComponentDto[];
    type: string;
}

export interface UserDto extends AbstractUserDto {
    email: string;
    id: number;
    personDto?: UserPersonDto;
}

export interface UserPersonDto extends Serializable {
    firstName: string;
    lastName: string;
}

export interface VInstitutionDto {
    lastDateVitalSign: Date;
    latitude: number;
    longitude: number;
    patientCount: number;
    patientWithCovidPresumtiveCount: number;
    patientWithVitalSignCount: number;
}

export interface VMedicalDischargeDto extends MedicalDischargeDto {
    dischargeType: MasterDataDto;
    snomedPtProblems: string[];
}

export interface VaccineConditionDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface VaccineConditionsDto {
    description: string;
    id: number;
    schemes: VaccineSchemeDto[];
}

export interface VaccineDoseInfoDto {
    description: string;
    order: number;
}

export interface VaccineInformationDto {
    conditions: VaccineConditionsDto[];
    description: string;
    id: number;
}

export interface VaccineSchemeDto {
    description: string;
    doses: VaccineDoseInfoDto[];
    id: number;
}

export interface VaccineSchemeInfoDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface VitalSignDto extends Serializable {
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    diastolicBloodPressure?: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure?: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface VitalSignObservationDto extends Serializable {
    loincCode: string;
    vitalSignObservation: NewEffectiveClinicalObservationDto;
}

export interface VitalSignsReportDto extends Serializable {
    bloodOxygenSaturation?: ReportClinicalObservationDto;
    diastolicBloodPressure?: ReportClinicalObservationDto;
    heartRate?: ReportClinicalObservationDto;
    respiratoryRate?: ReportClinicalObservationDto;
    systolicBloodPressure?: ReportClinicalObservationDto;
    temperature?: ReportClinicalObservationDto;
}

export type CoverageDtoUnion = HealthInsuranceDto | PrivateHealthInsuranceDto;

export const enum AppFeature {
    HABILITAR_ALTA_SIN_EPICRISIS = "HABILITAR_ALTA_SIN_EPICRISIS",
    MAIN_DIAGNOSIS_REQUIRED = "MAIN_DIAGNOSIS_REQUIRED",
    RESPONSIBLE_DOCTOR_REQUIRED = "RESPONSIBLE_DOCTOR_REQUIRED",
    HABILITAR_CARGA_FECHA_PROBABLE_ALTA = "HABILITAR_CARGA_FECHA_PROBABLE_ALTA",
    HABILITAR_GESTION_DE_TURNOS = "HABILITAR_GESTION_DE_TURNOS",
    HABILITAR_HISTORIA_CLINICA_AMBULATORIA = "HABILITAR_HISTORIA_CLINICA_AMBULATORIA",
    HABILITAR_UPDATE_DOCUMENTS = "HABILITAR_UPDATE_DOCUMENTS",
    HABILITAR_EDITAR_PACIENTE_COMPLETO = "HABILITAR_EDITAR_PACIENTE_COMPLETO",
    HABILITAR_MODULO_GUARDIA = "HABILITAR_MODULO_GUARDIA",
    HABILITAR_MODULO_PORTAL_PACIENTE = "HABILITAR_MODULO_PORTAL_PACIENTE",
    HABILITAR_CONFIGURACION = "HABILITAR_CONFIGURACION",
    HABILITAR_BUS_INTEROPERABILIDAD = "HABILITAR_BUS_INTEROPERABILIDAD",
    HABILITAR_ODONTOLOGY = "HABILITAR_ODONTOLOGY",
    HABILITAR_REPORTES = "HABILITAR_REPORTES",
    HABILITAR_VACUNAS_V2 = "HABILITAR_VACUNAS_V2",
    HABILITAR_INFORMES = "HABILITAR_INFORMES",
    HABILITAR_LLAMADO = "HABILITAR_LLAMADO",
    HABILITAR_HISTORIA_CLINICA_EXTERNA = "HABILITAR_HISTORIA_CLINICA_EXTERNA",
    HABILITAR_SERVICIO_RENAPER = "HABILITAR_SERVICIO_RENAPER",
    RESTRINGIR_DATOS_EDITAR_PACIENTE = "RESTRINGIR_DATOS_EDITAR_PACIENTE",
    HABILITAR_INTERCAMBIO_TEMAS = "HABILITAR_INTERCAMBIO_TEMAS",
}

export const enum EDocumentSearch {
    DIAGNOSIS = "DIAGNOSIS",
    DOCTOR = "DOCTOR",
    CREATED_ON = "CREATED_ON",
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
    API_CONSUMER = "API_CONSUMER",
    ESPECIALISTA_EN_ODONTOLOGIA = "ESPECIALISTA_EN_ODONTOLOGIA",
}

export const enum ESurfacePositionDto {
    INTERNAL = "INTERNAL",
    EXTERNAL = "EXTERNAL",
    LEFT = "LEFT",
    RIGHT = "RIGHT",
    CENTRAL = "CENTRAL",
}
