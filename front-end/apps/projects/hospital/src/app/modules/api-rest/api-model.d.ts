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
    countryId: number;
    cuil: string;
    departmentId: number;
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
    phonePrefix: string;
    postcode: string;
    provinceId: number;
    quarter: string;
    religion: string;
    street: string;
}

export interface ARTCoverageDto extends CoverageDto {
}

export interface AbstractMasterdataDto<T> extends MasterDataInterface<T>, Serializable {
}

export interface AbstractUserDto extends Serializable {
}

export interface AccessDataDto {
    password: string;
    token: string;
    userId: number;
    username: string;
}

export interface AddressDto extends Serializable {
    apartment: string;
    city: CityDto;
    cityId: number;
    countryId: number;
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
    modificationReason?: string;
    notes?: DocumentObservationsDto;
    personalHistories: HealthHistoryConditionDto[];
    procedures?: HospitalizationProcedureDto[];
    riskFactors?: RiskFactorDto;
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
    rnos: number;
    sisaCode: string;
    specialty: string;
}

export interface AnthropometricDataDto extends Serializable {
    bloodType?: ClinicalObservationDto;
    bmi?: ClinicalObservationDto;
    headCircumference?: ClinicalObservationDto;
    height?: ClinicalObservationDto;
    weight?: ClinicalObservationDto;
}

export interface ApiErrorDto {
    errors: string[];
    message: string;
}

export interface ApiErrorMessageDto {
    args: { [index: string]: any };
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
    holiday: number;
    programmed: number;
    programmedAvailable: number;
    spontaneous: number;
}

export interface AppointmentDto extends CreateAppointmentDto {
    appointmentStateId: number;
    observation?: string;
    observationBy?: string;
    protected: boolean;
    stateChangeReason?: string;
}

export interface AppointmentListDto {
    appointmentBlockMotiveId: number;
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
    phonePrefix: string;
    protected: boolean;
}

export interface AppointmentSearchDto {
    aliasOrSpecialtyName: string;
    daysOfWeek: number[];
    endSearchTime: TimeDto;
    endingSearchDate: DateDto;
    initialSearchDate: DateDto;
    initialSearchTime: TimeDto;
}

export interface AppointmentShortSummaryDto {
    date: DateDto;
    doctorFullName: string;
    hour: TimeDto;
    institution: string;
}

export interface AppointmentTicketDto {
    date: string;
    doctorFullName: string;
    doctorsOffice: string;
    documentNumber: string;
    hour: string;
    institution: string;
    medicalCoverage: string;
    patientFullName: string;
}

export interface AssignedAppointmentDto {
    date: DateDto;
    hour: TimeDto;
    license: string;
    office: string;
    professionalName: string;
    specialties: string[];
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

export interface AuthorDto extends Serializable {
    firstName: string;
    id: number;
    lastName: string;
    licence: string;
    nameSelfDetermination: string;
}

export interface AvailabilityDto {
    date: Date;
    slots: string[];
}

export interface BMPatientDto extends APatientDto {
    id: number;
}

export interface BMPersonDto extends APersonDto {
    id: number;
}

export interface BackofficeBookingInstitutionDto extends Serializable {
    id: number;
}

export interface BackofficeClinicalSpecialtyMandatoryMedicalPracticeDto {
    clinicalSpecialtyId: number;
    id: number;
    mandatoryMedicalPracticeId: number;
    practiceRecommendations: string;
}

export interface BackofficeCoverageDto extends Serializable {
    acronym?: string;
    cuit: string;
    enabled: boolean;
    id: number;
    name: string;
    rnos?: number;
    type: number;
}

export interface BackofficeHealthInsurancePracticeDto {
    clinicalSpecialtyId: number;
    coverageInformation: string;
    id: number;
    mandatoryMedicalPracticeId: number;
    medicalCoverageId: number;
}

export interface BackofficeHealthcareProfessionalCompleteDto {
    firstName: string;
    id: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    personId: number;
}

export interface BackofficeMandatoryMedicalPracticeDto {
    description: string;
    id: number;
    mmpCode: string;
    snomedId: number;
}

export interface BackofficeMandatoryProfessionalPracticeFreeDaysDto {
    clinicalSpecialtyId: number;
    days: number[];
    healthcareProfessionalId: number;
    id: number;
    mandatoryMedicalPracticeId: number;
}

export interface BackofficeSnowstormDto {
    conceptId: string;
    id: string;
    term: string;
}

export interface BackofficeUserDto {
    enable: boolean;
    id: number;
    lastLogin: Date;
    personId: number;
    roles: BackofficeUserRoleDto[];
    twoFactorAuthenticationEnabled: boolean;
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
    nameSelfDetermination: string;
    otherLastNames: string;
    selfPerceivedGender: string;
}

export interface BasicPatientDto extends Serializable {
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    middleName: string;
    person: BasicDataPersonDto;
    typeId: number;
}

export interface BasicPersonalDataDto extends IBasicPersonalData {
    genderId: number;
    nameSelfDetermination: string;
    phonePrefix: string;
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

export interface BlockDto {
    appointmentBlockMotiveId: number;
    end: TimeDto;
    endDateDto: DateDto;
    fullBlock: boolean;
    init: TimeDto;
    initDateDto: DateDto;
}

export interface BookingAppointmentDto {
    coverageId: number;
    day: string;
    diaryId: number;
    hour: string;
    openingHoursId: number;
    phoneNumber: string;
    snomedId: number;
    specialtyId: number;
}

export interface BookingDiaryDto {
    appointmentDuration: number;
    doctorsOfficeDescription: string;
    doctorsOfficeId: number;
    endDate: Date;
    from: Date;
    id: number;
    openingHoursId: number;
    startDate: Date;
    to: Date;
}

export interface BookingDto {
    appointmentDataEmail: string;
    bookingAppointmentDto: BookingAppointmentDto;
    bookingPersonDto: BookingPersonDto;
}

export interface BookingHealthInsuranceDto {
    description: string;
    id: number;
}

export interface BookingInstitutionDto {
    description: string;
    id: number;
}

export interface BookingPersonDto {
    birthDate: string;
    email: string;
    firstName: string;
    genderId: number;
    idNumber: string;
    lastName: string;
}

export interface BookingProfessionalDto {
    coverage: boolean;
    id: number;
    name: string;
}

export interface BookingSpecialtyDto {
    description: string;
    id: number;
}

export interface BreathingDto extends Serializable {
    bloodOxygenSaturation?: NewEffectiveClinicalObservationDto;
    respiratoryRate?: NewEffectiveClinicalObservationDto;
    respiratoryRetractionId?: number;
    stridor?: boolean;
}

export interface CareLineDto extends Serializable {
    clinicalSpecialties: ClinicalSpecialtyDto[];
    description: string;
    id: number;
}

export interface CareLineProblemDto {
    careLineId: number;
    conceptSctid: string;
    id: number;
    snomedId: number;
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
    associatedProfessionalsInfo: ProfessionalPersonDto[];
    careLinesInfo: CareLineDto[];
    doctorsOfficeDescription: string;
    sectorDescription: string;
    sectorId: number;
    sectorName: string;
    specialtyName: string;
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

export interface CounterReferenceAllergyDto extends Serializable {
    categoryId: string;
    criticalityId: number;
    snomed: SnomedDto;
    startDate: DateDto;
    statusId?: string;
    verificationId?: string;
}

export interface CounterReferenceDto extends Serializable {
    allergies: CounterReferenceAllergyDto[];
    clinicalSpecialtyId: number;
    closureTypeId: number;
    counterReferenceNote: string;
    fileIds: number[];
    medications: CounterReferenceMedicationDto[];
    procedures: CounterReferenceProcedureDto[];
    referenceId: number;
}

export interface CounterReferenceMedicationDto extends Serializable {
    id?: number;
    note: string;
    snomed: SnomedDto;
    statusId?: string;
    suspended: boolean;
}

export interface CounterReferenceProcedureDto extends Serializable {
    performedDate?: DateDto;
    snomed: SnomedDto;
}

export interface CounterReferenceSummaryDto extends Serializable {
    clinicalSpecialty: string;
    closureType: string;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institution: string;
    note: string;
    performedDate: Date;
    procedures: CounterReferenceSummaryProcedureDto[];
    professional: ProfessionalPersonDto;
}

export interface CounterReferenceSummaryProcedureDto extends Serializable {
    snomed: SharedSnomedDto;
}

export interface CoverageDto extends Serializable {
    cuit: string;
    id: number;
    name: string;
    type: number;
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
    phonePrefix?: string;
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
    references: ReferenceDto[];
    riskFactors?: OutpatientRiskFactorDto;
}

export interface CreationableDto extends Serializable {
    createdOn: Date;
}

export interface DashboardRoleInfoDto {
    id: number;
    institution: number;
    value: string;
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
    category: string;
    creationDate: Date;
    doctor: DoctorInfoDto;
    healthCondition: HealthConditionInfoDto;
    id: number;
    link?: string;
    observations?: string;
    serviceRequestId: number;
    snomed: SnomedDto;
    source: string;
    statusId: string;
}

export interface DiagnosticReportInfoWithFilesDto extends DiagnosticReportInfoDto {
    files: FileDto[];
}

export interface DiaryADto {
    alias?: string;
    appointmentDuration: number;
    automaticRenewal?: boolean;
    careLines?: number[];
    clinicalSpecialtyId: number;
    diaryAssociatedProfessionalsId: number[];
    diaryOpeningHours: DiaryOpeningHoursDto[];
    doctorsOfficeId: number;
    endDate: string;
    healthcareProfessionalId: number;
    includeHoliday?: boolean;
    professionalAssignShift?: boolean;
    protectedAppointmentsPercentage: number;
    startDate: string;
}

export interface DiaryAvailabilityDto {
    diary: BookingDiaryDto;
    slots: AvailabilityDto;
}

export interface DiaryAvailableProtectedAppointmentsDto {
    clinicalSpecialty: ClinicalSpecialtyDto;
    date: DateDto;
    department: DepartmentDto;
    diaryId: number;
    doctorOffice: string;
    hour: TimeDto;
    institution: InstitutionBasicInfoDto;
    jointDiary: boolean;
    openingHoursId: number;
    overturnMode: boolean;
    professionalFullName: string;
}

export interface DiaryDto extends DiaryADto {
    id: number;
}

export interface DiaryListDto {
    alias: string;
    appointmentDuration: number;
    clinicalSpecialtyName: string;
    doctorsOfficeDescription: string;
    endDate: string;
    id: number;
    includeHoliday: boolean;
    professionalAssignShift: boolean;
    startDate: string;
}

export interface DiaryOpeningHoursDto extends Overlapping<DiaryOpeningHoursDto> {
    externalAppointmentsAllowed: boolean;
    medicalAttentionTypeId: number;
    openingHours: OpeningHoursDto;
    overturnCount?: number;
}

export interface DietDto extends IndicationDto {
    description: string;
}

export interface DoctorInfoDto {
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
}

export interface DoctorsOfficeDto {
    closingTime: string;
    description: string;
    id: number;
    openingTime: string;
}

export interface DocumentAppointmentDto {
    appointmentId: number;
    documentId: number;
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
    performedDate: DateDto;
    personalHistories: HealthHistoryConditionDto[];
    problems: ProblemDto[];
    procedures: ProcedureDto[];
    reasons: ReasonDto[];
    riskFactors: RiskFactorDto;
}

export interface DocumentFileDto {
    createdOn: Date;
    creationable: CreationableDto;
    filename: string;
    id: number;
    sourceId: number;
    sourceTypeId: number;
    typeId: number;
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

export interface DocumentReduceInfoDto extends Serializable {
    createdBy: number;
    createdOn: Date;
    sourceId: number;
    typeId: number;
}

export interface DocumentSearchDto extends Serializable {
    confirmed: boolean;
    createdOn: DateTimeDto;
    creator: ResponsibleDoctorDto;
    diagnosis: string[];
    documentType: string;
    editedOn: DateTimeDto;
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

export interface EmergencyCareEpisodeInProgressDto {
    id?: number;
    inProgress: boolean;
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
    nameSelfDetermination: string;
}

export interface EmergencyCareUserDto {
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
}

export interface EmptyAppointmentDto {
    alias: string;
    clinicalSpecialtyName: string;
    date: string;
    diaryId: number;
    doctorFirstName: string;
    doctorLastName: string;
    doctorsOfficeDescription: string;
    hour: string;
    openingHoursId: number;
    overturnMode: boolean;
    patientId: number;
}

export interface EpicrisisDto extends Serializable {
    allergies: AllergyConditionDto[];
    confirmed: boolean;
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: DiagnosisDto;
    medications: MedicationDto[];
    modificationReason?: string;
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
    diagnosis?: DiagnosisDto[];
    mainDiagnosis?: HealthConditionDto;
    notes?: DocumentObservationsDto;
}

export interface EvolutionNoteDto extends Serializable {
    allergies?: AllergyConditionDto[];
    anthropometricData?: AnthropometricDataDto;
    confirmed: boolean;
    diagnosis?: DiagnosisDto[];
    immunizations?: ImmunizationDto[];
    isNursingEvolutionNote?: boolean;
    mainDiagnosis?: HealthConditionDto;
    modificationReason?: string;
    notes?: DocumentObservationsDto;
    procedures?: HospitalizationProcedureDto[];
    riskFactors?: RiskFactorDto;
}

export interface ExternalClinicalHistoryDto extends Serializable {
    consultationDate: DateDto;
    id: number;
    institution?: string;
    notes: string;
    professionalName?: string;
    professionalSpecialty?: string;
}

export interface ExternalCoverageDto {
    cuit: string;
    id?: number;
    name: string;
    plan?: string;
    type: EMedicalCoverageTypeDto;
}

export interface ExternalPatientCoverageDto {
    active: boolean;
    affiliateNumber: string;
    condition?: number;
    medicalCoverage: ExternalCoverageDto;
    vigencyDate?: Date;
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

export interface FrequencyDto {
    dailyVolume?: number;
    duration?: TimeDto;
    flowDropsHour: number;
    flowMlHour: number;
    id?: number;
}

export interface FullySpecifiedNamesDto {
    lang: string;
    term: string;
}

export interface GenderDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface HCEAllergyDto extends ClinicalTermDto {
    categoryId: number;
    criticalityId: number;
}

export interface HCEAnthropometricDataDto extends Serializable {
    bloodType?: HCEEffectiveClinicalObservationDto;
    bmi?: HCEEffectiveClinicalObservationDto;
    headCircumference?: HCEEffectiveClinicalObservationDto;
    height?: HCEEffectiveClinicalObservationDto;
    weight?: HCEEffectiveClinicalObservationDto;
}

export interface HCEBasicPersonDataDto extends Serializable {
    birthDate: string;
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    nameSelfDetermination: string;
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

export interface HCEDocumentDataDto {
    filename: string;
    id: number;
}

export interface HCEEffectiveClinicalObservationDto extends HCEClinicalObservationDto {
    effectiveTime: string;
}

export interface HCEEvolutionSummaryDto extends Serializable {
    clinicalSpecialty: ClinicalSpecialtyDto;
    consultationId: number;
    document: HCEDocumentDataDto;
    evolutionNote: string;
    healthConditions: HCEHealthConditionDto[];
    procedures: HCEProcedureDto[];
    professional: HCEHealthcareProfessionalDto;
    reasons: HCEReasonDto[];
    startDate: string;
}

export interface HCEHealthConditionDto extends HCEPersonalHistoryDto {
    main: boolean;
    problemId: string;
    references: HCEReferenceDto[];
}

export interface HCEHealthcareProfessionalDto {
    id: number;
    licenseNumber: string;
    person: HCEBasicPersonDataDto;
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

export interface HCELast2RiskFactorsDto extends Serializable {
    current: HCERiskFactorDto;
    previous: HCERiskFactorDto;
}

export interface HCEMedicationDto extends ClinicalTermDto {
    suspended: boolean;
}

export interface HCEPersonalHistoryDto extends HCEClinicalTermDto {
    hasPendingReference: boolean;
    inactivationDate: string;
    severity: string;
    startDate: string;
}

export interface HCEProcedureDto extends HCEClinicalTermDto {
    performedDate?: string;
}

export interface HCEReasonDto {
    snomed: SnomedDto;
}

export interface HCEReferenceCounterReferenceFileDto extends Serializable {
    fileId: number;
    fileName: string;
}

export interface HCEReferenceDto {
    careLine: string;
    clinicalSpecialty: string;
    counterReference: HCESummaryCounterReferenceDto;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institution: string;
    note: string;
}

export interface HCERiskFactorDto extends Serializable {
    bloodGlucose?: HCEEffectiveClinicalObservationDto;
    bloodOxygenSaturation?: HCEEffectiveClinicalObservationDto;
    cardiovascularRisk?: HCEEffectiveClinicalObservationDto;
    diastolicBloodPressure?: HCEEffectiveClinicalObservationDto;
    glycosylatedHemoglobin?: HCEEffectiveClinicalObservationDto;
    heartRate?: HCEEffectiveClinicalObservationDto;
    respiratoryRate?: HCEEffectiveClinicalObservationDto;
    systolicBloodPressure?: HCEEffectiveClinicalObservationDto;
    temperature?: HCEEffectiveClinicalObservationDto;
}

export interface HCESummaryCounterReferenceDto {
    clinicalSpecialtyId: string;
    closureType: string;
    counterReferenceNote: string;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institution: string;
    performedDate: DateDto;
    procedures: CounterReferenceSummaryProcedureDto[];
    professional: ProfessionalPersonDto;
}

export interface HCEToothRecordDto extends Serializable {
    date: DateDto;
    snomed: SnomedDto;
    surfaceSctid?: string;
}

export interface HealthCareProfessionalGroupDto {
    healthcareProfessionalId: number;
    internmentEpisodeId: number;
    responsible: boolean;
}

export interface HealthConditionDto extends ClinicalTermDto {
    isAdded?: boolean;
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
    note: string;
    startDate?: string;
}

export interface HealthInsuranceDto extends CoverageDto {
    acronym: string;
    rnos: number;
}

export interface HealthcareProfessionalCompleteDto {
    id?: number;
    personId: number;
    professionalProfessions: ProfessionalProfessionsDto[];
}

export interface HealthcareProfessionalDto {
    id: number;
    licenseNumber: string;
    nameSelfDetermination: string;
    person: PersonBasicDataResponseDto;
    personId: number;
}

export interface HealthcareProfessionalHealthInsuranceDto extends Serializable {
    healthcareProfessionalId: number;
    id: number;
    medicalCoverageId: number;
}

export interface HealthcareProfessionalSpecialtyDto {
    clinicalSpecialty: ClinicalSpecialtyDto;
    healthcareProfessionalId?: number;
    id?: number;
    professionalProfessionId?: number;
}

export interface HolidayDto {
    date: DateDto;
    description: string;
}

export interface HospitalUserPersonInfoDto {
    email: string;
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
    personId: number;
}

export interface HospitalizationProcedureDto {
    id?: number;
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

export interface ImmunizationInfoDto {
    administrationDate: string;
    billable: boolean;
    condition: VaccineConditionDto;
    doctorInfo: string;
    dose: VaccineDoseInfoDto;
    id: number;
    institutionInfo: string;
    lotNumber: string;
    note: string;
    scheme: VaccineSchemeInfoDto;
    snomed: SnomedDto;
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

export interface IndicationDto {
    createdBy: string;
    createdOn: DateTimeDto;
    id: number;
    indicationDate: DateDto;
    patientId: number;
    professionalId: number;
    status: EIndicationStatus;
    type: EIndicationType;
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
    dischargeDate: Date;
    entryDate: Date;
    institutionId: number;
    noteId: number;
    patientId: number;
    patientMedicalCoverageId: number;
    responsibleContact?: ResponsibleContactDto;
    responsibleDoctorId: number;
}

export interface InternmentEpisodeBMDto extends InternmentEpisodeADto {
    id: number;
}

export interface InternmentEpisodeDto {
    bed: BedDto;
    doctor: ResponsibleDoctorDto;
    documentsSummary: DocumentsSummaryDto;
    hasPhysicalDischarge: boolean;
    id: number;
    patient: PatientDto;
}

export interface InternmentEpisodeProcessDto {
    id?: number;
    inProgress: boolean;
    patientHospitalized: boolean;
}

export interface InternmentGeneralStateDto extends Serializable {
    allergies: AllergyConditionDto[];
    anthropometricData: AnthropometricDataDto;
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    medications: MedicationDto[];
    personalHistories: HealthHistoryConditionDto[];
    riskFactors: Last2RiskFactorsDto;
}

export interface InternmentPatientDto {
    bedNumber: string;
    birthDate: Date;
    documentsSummary: DocumentsSummaryDto;
    firstName: string;
    genderId: number;
    hasAdministrativeDischarge: boolean;
    hasMedicalDischarge: boolean;
    hasPhysicalDischarge: boolean;
    identificationNumber: string;
    identificationTypeId: number;
    internmentId: number;
    lastName: string;
    nameSelfDetermination: string;
    patientId: number;
    roomNumber: string;
    sectorDescription: string;
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

export interface JWTokenDto {
    refreshToken: string;
    token: string;
}

export interface Last2RiskFactorsDto extends Serializable {
    current: RiskFactorDto;
    previous: RiskFactorDto;
}

export interface LicenseNumberDto {
    id: number;
    info: string;
    number: string;
    type: string;
}

export interface LicenseNumberTypeDto extends Serializable {
    description: string;
    id: number;
}

export interface LimitedPatientSearchDto {
    actualPatientSearchSize: number;
    patientList: PatientSearchDto[];
}

export interface LoggedPersonDto {
    avatar?: string;
    firstName: string;
    lastName: string;
    nameSelfDetermination: string;
}

export interface LoggedUserDto {
    email: string;
    id: number;
    personDto: LoggedPersonDto;
    previousLogin: DateTimeDto;
}

export interface LoginDto extends Serializable {
    password: string;
    username: string;
}

export interface MainDiagnosisDto extends Serializable {
    mainDiagnosis: HealthConditionDto;
    notes: DocumentObservationsDto;
}

export interface ManualClassificationDto {
    description: string;
    id: number;
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

export interface MedicalCoveragePlanDto {
    id: number;
    medicalCoverageId: number;
    plan: string;
}

export interface MedicalCoverageTypeDto extends Serializable {
    id: number;
    value: string;
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
}

export interface NewDosageDto extends Serializable {
    chronic: boolean;
    diary: boolean;
    duration?: number;
    event?: string;
    frequency?: number;
    periodUnit?: string;
    quantity?: QuantityDto;
    startDateTime?: DateTimeDto;
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

export interface NewRiskFactorsObservationDto extends Serializable {
    bloodGlucose?: NewEffectiveClinicalObservationDto;
    bloodOxygenSaturation?: NewEffectiveClinicalObservationDto;
    cardiovascularRisk?: NewEffectiveClinicalObservationDto;
    diastolicBloodPressure?: NewEffectiveClinicalObservationDto;
    glycosylatedHemoglobin?: NewEffectiveClinicalObservationDto;
    heartRate?: NewEffectiveClinicalObservationDto;
    respiratoryRate?: NewEffectiveClinicalObservationDto;
    systolicBloodPressure?: NewEffectiveClinicalObservationDto;
    temperature?: NewEffectiveClinicalObservationDto;
}

export interface NewServiceRequestListDto extends Serializable {
    medicalCoverageId: number;
    studiesDto: StudyDto[];
}

export interface NotifyPatientDto {
    appointmentId: number;
    doctorName: string;
    doctorsOfficeName: string;
    patientName: string;
    sectorId: number;
    topic: string;
}

export interface NursingAnthropometricDataDto extends Serializable {
    bloodType?: ClinicalObservationDto;
    bmi?: ClinicalObservationDto;
    headCircumference?: ClinicalObservationDto;
    height: ClinicalObservationDto;
    weight: ClinicalObservationDto;
}

export interface NursingConsultationDto extends Serializable {
    anthropometricData?: NursingAnthropometricDataDto;
    clinicalSpecialtyId: number;
    evolutionNote?: string;
    problem: NursingProblemDto;
    procedures?: NursingProcedureDto[];
    riskFactors?: NursingRiskFactorDto;
}

export interface NursingHealtConditionDto extends Serializable {
    id?: number;
    snomed: SnomedDto;
    statusId?: string;
    verificationId?: string;
}

export interface NursingProblemDto extends Serializable {
    severity?: string;
    snomed: SnomedDto;
    startDate?: string;
    statusId?: string;
}

export interface NursingProcedureDto extends Serializable {
    performedDate?: string;
    snomed: SnomedDto;
}

export interface NursingRecordDto {
    administrationTime: DateTimeDto;
    event: string;
    id: number;
    indication: IndicationDto;
    observation: string;
    scheduledAdministrationTime: DateTimeDto;
    status: ENursingRecordStatus;
    updateReason: string;
    updatedBy: string;
}

export interface NursingRiskFactorDto extends Serializable {
    bloodGlucose?: EffectiveClinicalObservationDto;
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    cardiovascularRisk?: EffectiveClinicalObservationDto;
    diastolicBloodPressure: EffectiveClinicalObservationDto;
    glycosylatedHemoglobin?: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface OAuthUserInfoDto {
    email: string;
    email_verified: boolean;
    family_name: string;
    given_name: string;
    name: string;
    preferred_username: string;
    sub: string;
}

export interface OauthConfigDto {
    clientId: string;
    enabled: boolean;
    issuerUrl: string;
    logoutUrl: string;
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
    references?: ReferenceDto[];
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
    severity?: string;
    snomed: SnomedDto;
    startDate?: DateDto;
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
    startDate?: DateDto;
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

export interface OtherIndicationDto extends IndicationDto {
    description: string;
    dosage?: NewDosageDto;
    otherIndicationTypeId: number;
    otherType?: string;
}

export interface OtherPharmacoDto {
    dosage: NewDosageDto;
    snomed: SharedSnomedDto;
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
    headCircumference?: ClinicalObservationDto;
    height: ClinicalObservationDto;
    weight: ClinicalObservationDto;
}

export interface OutpatientEvolutionSummaryDto extends Serializable {
    clinicalSpecialty: ClinicalSpecialtyDto;
    consultationID: number;
    evolutionNote: string;
    healthConditions: OutpatientSummaryHealthConditionDto[];
    procedures: OutpatientProcedureDto[];
    professional: HealthcareProfessionalDto;
    reasons: OutpatientReasonDto[];
    startDate: string;
}

export interface OutpatientFamilyHistoryDto {
    snomed: SnomedDto;
    startDate?: string;
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
    severity?: string;
    snomed: SnomedDto;
    startDate?: string;
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

export interface OutpatientRiskFactorDto extends Serializable {
    bloodGlucose?: EffectiveClinicalObservationDto;
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    cardiovascularRisk?: EffectiveClinicalObservationDto;
    diastolicBloodPressure: EffectiveClinicalObservationDto;
    glycosylatedHemoglobin?: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface OutpatientSummaryCounterReferenceDto {
    clinicalSpecialtyId: string;
    closureType: string;
    counterReferenceNote: string;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institution: string;
    performedDate: DateDto;
    procedures: CounterReferenceSummaryProcedureDto[];
    professional: ProfessionalPersonDto;
}

export interface OutpatientSummaryHealthConditionDto extends ClinicalTermDto {
    inactivationDate: string;
    main: boolean;
    problemId: string;
    references: OutpatientSummaryReferenceDto[];
    startDate: string;
}

export interface OutpatientSummaryReferenceDto {
    careLine: string;
    clinicalSpecialty: string;
    counterReference: OutpatientSummaryCounterReferenceDto;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institution: string;
    note: string;
}

export interface OutpatientUpdateImmunizationDto {
    administrationDate: string;
    snomed: SnomedDto;
}

export interface Overlapping<T> {
}

export interface ParenteralPlanDto extends IndicationDto {
    dosage: NewDosageDto;
    frequency: FrequencyDto;
    pharmacos: OtherPharmacoDto[];
    snomed: SharedSnomedDto;
    via?: number;
}

export interface PasswordDto {
    newPassword: string;
    password: string;
}

export interface PasswordResetDto {
    password: string;
    token: string;
}

export interface PasswordResetResponseDto {
    username: string;
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
    physicalDischargeDate: Date;
}

export interface PatientDto {
    birthDate: Date;
    firstName: string;
    fullName: string;
    id: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    nameSelfDetermination: string;
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
    condition: EPatientMedicalCoverageCondition;
    endDate?: string;
    id?: number;
    medicalCoverage: CoverageDto;
    planId?: number;
    planName?: string;
    startDate?: string;
    vigencyDate?: string;
}

export interface PatientPhotoDto {
    imageData: string;
    patientId: number;
}

export interface PatientSearchDto {
    activo: boolean;
    idPatient: number;
    nameSelfDetermination: string;
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
    roleAssignments: RoleAssignmentDto[];
}

export interface PersonBasicDataResponseDto extends Serializable {
    birthDate: string;
    cuil: string;
    firstName: string;
    lastName: string;
    photo: string;
}

export interface PersonDataDto {
    firstName?: string;
    identificationNumber: string;
    identificationType: string;
    lastName?: string;
    userId: number;
    username?: string;
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
    phonePrefix: string;
}

export interface PharmacoDto extends IndicationDto {
    dosage: NewDosageDto;
    foodRelationId: number;
    healthConditionId: number;
    note?: string;
    patientProvided: boolean;
    snomed: SharedSnomedDto;
    solvent?: OtherPharmacoDto;
    viaId: number;
}

export interface PharmacoSummaryDto extends IndicationDto, Serializable {
    dosage: NewDosageDto;
    snomed: SharedSnomedDto;
    via: string;
}

export interface PoliceInterventionDetailsDto extends Serializable {
    callDate: DateDto;
    callTime: TimeDto;
    firstName: string;
    lastName: string;
    plateNumber: string;
}

export interface PracticeDto {
    coverage: boolean;
    coverageText: string;
    description: string;
    id: number;
    snomedId: number;
}

export interface PreferredTermDto {
    lang: string;
    term: string;
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
    planId?: number;
    planName?: string;
    startDate: string;
}

export interface PrivateHealthInsuranceDto extends CoverageDto {
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

export interface ProfessionCompleteDto {
    allLicenses: LicenseNumberDto[];
    description: string;
    id: number;
    licenses: LicenseNumberDto[];
    professionId: number;
    specialties: ProfessionSpecialtyDto[];
}

export interface ProfessionSpecialtyDto {
    id: number;
    licenses: LicenseNumberDto[];
    specialty: ClinicalSpecialtyDto;
}

export interface ProfessionalAvailabilityDto {
    availability: DiaryAvailabilityDto[];
    professional: BookingProfessionalDto;
}

export interface ProfessionalCompleteDto {
    allLicenses: LicenseNumberDto[];
    completeLicenseInfo: string;
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
    personId: number;
    professions: ProfessionCompleteDto[];
}

export interface ProfessionalDto {
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    nameSelfDetermination: string;
    phoneNumber: string;
}

export interface ProfessionalInfoDto {
    clinicalSpecialties: ClinicalSpecialtyDto[];
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    nameSelfDetermination: string;
    phoneNumber: string;
}

export interface ProfessionalLicenseNumberDto extends Serializable {
    healthcareProfessionalSpecialtyId?: number;
    id?: number;
    licenseNumber: string;
    professionalProfessionId: number;
    typeId: number;
}

export interface ProfessionalPersonDto extends Serializable {
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
}

export interface ProfessionalProfessionBackofficeDto {
    clinicalSpecialtyId: number;
    deleted: boolean;
    healthcareProfessionalId: number;
    id: number;
    personId: number;
    professionalSpecialtyId: number;
}

export interface ProfessionalProfessionsDto {
    healthcareProfessionalId?: number;
    id?: number;
    profession: ProfessionalSpecialtyDto;
    specialties: HealthcareProfessionalSpecialtyDto[];
}

export interface ProfessionalSpecialtyDto {
    description: string;
    id: number;
}

export interface ProfessionalsByClinicalSpecialtyDto {
    clinicalSpecialty: ClinicalSpecialtyDto;
    professionalsIds: number[];
}

export interface ProvinceDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface PublicAppointmentClinicalSpecialty {
    name: string;
    sctid: string;
}

export interface PublicAppointmentDoctorDto {
    licenseNumber: string;
    person: PublicAppointmentPersonDto;
}

export interface PublicAppointmentInstitution {
    cuit: string;
    id: number;
    sisaCode: string;
}

export interface PublicAppointmentListDto {
    clinicalSpecialty: PublicAppointmentClinicalSpecialty;
    date: string;
    doctor: PublicAppointmentDoctorDto;
    hour: string;
    id: number;
    institution: PublicAppointmentInstitution;
    medicalCoverage: PublicAppointmentMedicalCoverage;
    overturn: boolean;
    patient: PublicAppointmentPatientDto;
    phone: string;
    status: PublicAppointmentStatus;
}

export interface PublicAppointmentMedicalCoverage {
    affiliateNumber: string;
    cuit: string;
    name: string;
}

export interface PublicAppointmentPatientDto {
    id: number;
    person: PublicAppointmentPersonDto;
}

export interface PublicAppointmentPersonDto {
    firstName: string;
    genderId: number;
    identificationNumber: string;
    lastName: string;
}

export interface PublicAppointmentStatus extends Serializable {
    description: string;
    id: number;
}

export interface PublicInfoDto {
    features: AppFeature[];
    flavor: string;
}

export interface QuantityDto extends Serializable {
    unit: string;
    value: number;
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

export interface ReferenceCounterReferenceFileDto extends Serializable {
    fileId: number;
    fileName: string;
}

export interface ReferenceDto extends Serializable {
    careLineId?: number;
    clinicalSpecialtyId: number;
    consultation?: boolean;
    destinationInstitutionId: number;
    fileIds: number[];
    note?: string;
    problems: ReferenceProblemDto[];
    procedure?: boolean;
}

export interface ReferenceGetDto extends Serializable {
    careLine: CareLineDto;
    clinicalSpecialty: ClinicalSpecialtyDto;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    note: ReferenceSummaryNoteDto;
    problems: ReferenceProblemDto[];
    professional: ProfessionalPersonDto;
    referenceDate: DateDto;
}

export interface ReferenceProblemDto extends Serializable {
    id?: number;
    snomed: SharedSnomedDto;
}

export interface ReferenceSummaryDto {
    date: DateDto;
    institution: InstitutionInfoDto;
    professionalFullName: string;
    referenceId: number;
}

export interface ReferenceSummaryNoteDto extends Serializable {
    description: string;
    id: number;
}

export interface RefreshTokenDto {
    refreshToken: string;
}

export interface ReportClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime: Date;
}

export interface RequiredPatientDataDto {
    birthDate: Date;
    email: string;
    firstName: string;
    genderId: number;
    identificationNumber: string;
    identificationTypeId: number;
    institutionId: number;
    lastName: string;
    phoneNumber: string;
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
    lastName: string;
    licenses: string[];
    nameSelfDetermination: string;
    userId: number;
}

export interface RiskFactorDto extends Serializable {
    bloodGlucose?: EffectiveClinicalObservationDto;
    bloodOxygenSaturation?: EffectiveClinicalObservationDto;
    cardiovascularRisk?: EffectiveClinicalObservationDto;
    diastolicBloodPressure?: EffectiveClinicalObservationDto;
    glycosylatedHemoglobin?: EffectiveClinicalObservationDto;
    heartRate?: EffectiveClinicalObservationDto;
    respiratoryRate?: EffectiveClinicalObservationDto;
    systolicBloodPressure?: EffectiveClinicalObservationDto;
    temperature?: EffectiveClinicalObservationDto;
}

export interface RiskFactorObservationDto extends Serializable {
    loincCode: string;
    riskFactorObservation: NewEffectiveClinicalObservationDto;
}

export interface RiskFactorsReportDto extends Serializable {
    bloodGlucose?: ReportClinicalObservationDto;
    bloodOxygenSaturation?: ReportClinicalObservationDto;
    cardiovascularRisk?: ReportClinicalObservationDto;
    diastolicBloodPressure?: ReportClinicalObservationDto;
    glycosylatedHemoglobin?: ReportClinicalObservationDto;
    heartRate?: ReportClinicalObservationDto;
    respiratoryRate?: ReportClinicalObservationDto;
    systolicBloodPressure?: ReportClinicalObservationDto;
    temperature?: ReportClinicalObservationDto;
}

export interface RoleAssignmentDto {
    institutionId: number;
    role: ERole;
    roleDescription: string;
}

export interface RoleDto {
    description: string;
    id: number;
}

export interface RoleInfoDto {
    id: number;
    institution: number;
    value: string;
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

export interface ServiceRequestCategoryDto {
    description: string;
    id: string;
}

export interface SharedSnomedDto extends Serializable {
    parentFsn: string;
    parentId: string;
    pt: string;
    sctid: string;
}

export interface SharedSnowstormSearchDto {
    items: SharedSnowstormSearchItemDto[];
    limit: number;
    searchAfter: string;
    total: number;
}

export interface SharedSnowstormSearchItemDto {
    active: boolean;
    conceptId: string;
    id: string;
    pt: string;
}

export interface SnomedDto extends Serializable {
    id?: number;
    parentFsn?: string;
    parentId?: string;
    pt: string;
    sctid: string;
}

export interface SnomedEclDto {
    key: SnomedECL;
    value: string;
}

export interface SnomedResponseDto extends Serializable {
    items: SnomedDto[];
    total: number;
}

export interface SnomedSearchDto {
    items: SnomedSearchItemDto[];
    total: number;
}

export interface SnomedSearchItemDto {
    conceptId: string;
    fsn: FullySpecifiedNamesDto;
    id: string;
    pt: PreferredTermDto;
}

export interface SnomedTemplateDto {
    concepts: SnomedSearchItemDto[];
    description: string;
}

export interface SnvsEventDto {
    description: string;
    eventId: number;
    groupEventId: number;
}

export interface SnvsEventManualClassificationsDto {
    manualClassifications: ManualClassificationDto[];
    snvsEvent: SnvsEventDto;
}

export interface SnvsReportDto {
    eventId: number;
    groupEventId: number;
    lastUpdate?: DateDto;
    manualClassificationId?: number;
    problem: SnvsSnomedDto;
    responseCode?: number;
    sisaRegisteredId?: number;
    status?: string;
}

export interface SnvsSnomedDto extends Serializable {
    pt: string;
    sctid: string;
}

export interface SnvsToReportDto {
    eventId: number;
    groupEventId: number;
    manualClassificationId: number;
    problem: SnvsSnomedDto;
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
    riskFactors: NewRiskFactorsObservationDto;
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
    riskFactors: NewRiskFactorsObservationDto;
}

export interface TriageNoAdministrativeDto extends TriageDto {
    notes?: string;
}

export interface TriagePediatricDto extends TriageNoAdministrativeDto {
    appearance?: AppearanceDto;
    breathing?: BreathingDto;
    circulation?: CirculationDto;
}

export interface TwoFactorAuthenticationDto {
    sharedSecret: string;
    sharedSecretBarCode: string;
}

export interface TwoFactorAuthenticationLoginDto {
    code: string;
}

export interface UpdateAppointmentDateDto {
    appointmentId: number;
    date: DateTimeDto;
    openingHoursId: number;
}

export interface UpdateAppointmentDto {
    appointmentId: number;
    appointmentStateId: number;
    overturn: boolean;
    patientId: number;
    patientMedicalCoverageId?: number;
    phoneNumber?: string;
}

export interface UpdateConceptsResultDto {
    conceptsLoaded: number;
    eclKey: string;
    erroneousConcepts: number;
    errorMessages: string[];
}

export interface UserDataDto {
    enable?: boolean;
    id?: number;
    lastLogin?: Date;
    username?: string;
}

export interface UserDto extends AbstractUserDto {
    email: string;
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
    personDto: UserPersonDto;
    personId: number;
    previousLogin: Date;
}

export interface UserInfoDto {
    enabled: boolean;
    id: number;
    password: string;
    previousLogin: Date;
    username: string;
}

export interface UserPersonDto extends Serializable {
    firstName: string;
    id?: number;
    lastName: string;
    nameSelfDetermination: string;
}

export interface UserRoleDto {
    institutionId: number;
    roleDescription?: string;
    roleId: number;
    userId: number;
}

export interface UserSharedInfoDto {
    id: number;
    username: string;
}

export interface VInstitutionDto {
    lastDateRiskFactor: Date;
    latitude: number;
    longitude: number;
    patientCount: number;
    patientWithCovidPresumtiveCount: number;
    patientWithRiskFactorCount: number;
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

export interface VerificationCodeDto {
    code: string;
}

export const enum AppFeature {
    HABILITAR_ALTA_SIN_EPICRISIS = "HABILITAR_ALTA_SIN_EPICRISIS",
    MAIN_DIAGNOSIS_REQUIRED = "MAIN_DIAGNOSIS_REQUIRED",
    RESPONSIBLE_DOCTOR_REQUIRED = "RESPONSIBLE_DOCTOR_REQUIRED",
    HABILITAR_CARGA_FECHA_PROBABLE_ALTA = "HABILITAR_CARGA_FECHA_PROBABLE_ALTA",
    HABILITAR_GESTION_DE_TURNOS = "HABILITAR_GESTION_DE_TURNOS",
    HABILITAR_HISTORIA_CLINICA_AMBULATORIA = "HABILITAR_HISTORIA_CLINICA_AMBULATORIA",
    HABILITAR_EDITAR_PACIENTE_COMPLETO = "HABILITAR_EDITAR_PACIENTE_COMPLETO",
    HABILITAR_MODULO_GUARDIA = "HABILITAR_MODULO_GUARDIA",
    HABILITAR_MODULO_PORTAL_PACIENTE = "HABILITAR_MODULO_PORTAL_PACIENTE",
    HABILITAR_CONFIGURACION = "HABILITAR_CONFIGURACION",
    HABILITAR_BUS_INTEROPERABILIDAD = "HABILITAR_BUS_INTEROPERABILIDAD",
    HABILITAR_ODONTOLOGY = "HABILITAR_ODONTOLOGY",
    HABILITAR_REPORTES = "HABILITAR_REPORTES",
    HABILITAR_INFORMES = "HABILITAR_INFORMES",
    HABILITAR_LLAMADO = "HABILITAR_LLAMADO",
    HABILITAR_HISTORIA_CLINICA_EXTERNA = "HABILITAR_HISTORIA_CLINICA_EXTERNA",
    HABILITAR_SERVICIO_RENAPER = "HABILITAR_SERVICIO_RENAPER",
    RESTRINGIR_DATOS_EDITAR_PACIENTE = "RESTRINGIR_DATOS_EDITAR_PACIENTE",
    HABILITAR_INTERCAMBIO_TEMAS = "HABILITAR_INTERCAMBIO_TEMAS",
    HABILITAR_CREACION_USUARIOS = "HABILITAR_CREACION_USUARIOS",
    HABILITAR_REPORTE_EPIDEMIOLOGICO = "HABILITAR_REPORTE_EPIDEMIOLOGICO",
    AGREGAR_MEDICOS_ADICIONALES = "AGREGAR_MEDICOS_ADICIONALES",
    HABILITAR_DESCARGA_DOCUMENTOS_PDF = "HABILITAR_DESCARGA_DOCUMENTOS_PDF",
    HABILITAR_DATOS_AUTOPERCIBIDOS = "HABILITAR_DATOS_AUTOPERCIBIDOS",
    HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA = "HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA",
    HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF = "HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF",
    HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS = "HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS",
    HABILITAR_MAIL_RESERVA_TURNO = "HABILITAR_MAIL_RESERVA_TURNO",
    LIBERAR_API_RESERVA_TURNOS = "LIBERAR_API_RESERVA_TURNOS",
    BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS = "BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS",
    OCULTAR_LISTADO_PROFESIONES_WEBAPP = "OCULTAR_LISTADO_PROFESIONES_WEBAPP",
    HABILITAR_MODULO_ENF_EN_DESARROLLO = "HABILITAR_MODULO_ENF_EN_DESARROLLO",
    HABILITAR_2FA = "HABILITAR_2FA",
    HABILITAR_EXTENSIONES_WEB_COMPONENTS = "HABILITAR_EXTENSIONES_WEB_COMPONENTS",
    HABILITAR_NOTIFICACIONES_TURNOS = "HABILITAR_NOTIFICACIONES_TURNOS",
    HABILITAR_GUARDADO_CON_CONFIRMACION_CONSULTA_AMBULATORIA = "HABILITAR_GUARDADO_CON_CONFIRMACION_CONSULTA_AMBULATORIA",
	HABILITAR_REPORTES_ESTADISTICOS = "HABILITAR_REPORTES_ESTADISTICOS",
	HABILITAR_VISUALIZACION_DE_CARDS = "HABILITAR_VISUALIZACION_DE_CARDS",
}

export const enum EDocumentSearch {
    DIAGNOSIS = "DIAGNOSIS",
    DOCTOR = "DOCTOR",
    CREATED_ON = "CREATED_ON",
}

export const enum EIndicationStatus {
    INDICATED = "INDICATED",
    SUSPENDED = "SUSPENDED",
    IN_PROGRESS = "IN_PROGRESS",
    COMPLETED = "COMPLETED",
    REJECTED = "REJECTED",
}

export const enum EIndicationType {
    PHARMACO = "PHARMACO",
    DIET = "DIET",
    PARENTERAL_PLAN = "PARENTERAL_PLAN",
    OTHER_INDICATION = "OTHER_INDICATION",
}

export const enum EMedicalCoverageTypeDto {
    PREPAGA = "PREPAGA",
    OBRASOCIAL = "OBRASOCIAL",
    ART = "ART",
}

export const enum ENursingRecordStatus {
    COMPLETED = "COMPLETED",
    REJECTED = "REJECTED",
    PENDING = "PENDING",
}

export const enum EOdontologyTopicDto {
    NUEVA_CONSULTA = "NUEVA_CONSULTA",
}

export const enum EPatientMedicalCoverageCondition {
    VOLUNTARIA = "VOLUNTARIA",
    OBLIGATORIA = "OBLIGATORIA",
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
    ADMINISTRADOR_DE_CAMAS = "ADMINISTRADOR_DE_CAMAS",
    PERSONAL_DE_IMAGENES = "PERSONAL_DE_IMAGENES",
    PERSONAL_DE_LABORATORIO = "PERSONAL_DE_LABORATORIO",
    PERSONAL_DE_FARMACIA = "PERSONAL_DE_FARMACIA",
    PERSONAL_DE_ESTADISTICA = "PERSONAL_DE_ESTADISTICA",
    PARTIALLY_AUTHENTICATED = "PARTIALLY_AUTHENTICATED",
}

export const enum ESurfacePositionDto {
    INTERNAL = "INTERNAL",
    EXTERNAL = "EXTERNAL",
    LEFT = "LEFT",
    RIGHT = "RIGHT",
    CENTRAL = "CENTRAL",
}

export const enum SnomedECL {
    BLOOD_TYPE = "BLOOD_TYPE",
    PERSONAL_RECORD = "PERSONAL_RECORD",
    FAMILY_RECORD = "FAMILY_RECORD",
    ALLERGY = "ALLERGY",
    HOSPITAL_REASON = "HOSPITAL_REASON",
    VACCINE = "VACCINE",
    MEDICINE = "MEDICINE",
    PROCEDURE = "PROCEDURE",
    CONSULTATION_REASON = "CONSULTATION_REASON",
    DIAGNOSIS = "DIAGNOSIS",
}
