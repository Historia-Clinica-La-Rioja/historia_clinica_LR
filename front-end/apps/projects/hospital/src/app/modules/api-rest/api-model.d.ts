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
    auditType: EAuditType;
    comments: string;
    generalPractitioner: AAdditionalDoctorDto;
    identityVerificationStatusId: number;
    message?: string;
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
    fileIds: number[];
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
    personAge?: PersonAgeDto;
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

export interface AdminUserDto {
    enable: boolean;
    id: number;
    lastLogin: Date;
    personId: number;
    twoFactorAuthenticationEnabled: boolean;
    username: string;
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

export interface AnalgesicTechniqueDto extends AnestheticSubstanceDto {
    catheter?: boolean;
    catheterNote?: string;
    injectionNote?: string;
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

export interface AnestheticHistoryDto {
    stateId?: number;
    zoneId?: number;
}

export interface AnestheticReportDto {
    analgesicTechniques?: AnalgesicTechniqueDto[];
    anestheticAgents?: AnestheticSubstanceDto[];
    anestheticChart?: string;
    anestheticHistory?: AnestheticHistoryDto;
    anestheticPlans?: AnestheticSubstanceDto[];
    anestheticTechniques?: AnestheticTechniqueDto[];
    anthropometricData?: AnthropometricDataDto;
    antibioticProphylaxis?: AnestheticSubstanceDto[];
    diagnosis?: DiagnosisDto[];
    fluidAdministrations?: AnestheticSubstanceDto[];
    foodIntake?: FoodIntakeDto;
    histories?: HealthConditionDto[];
    mainDiagnosis?: DiagnosisDto;
    measuringPoints?: MeasuringPointDto[];
    medications?: MedicationDto[];
    nonAnestheticDrugs?: AnestheticSubstanceDto[];
    postAnesthesiaStatus?: PostAnesthesiaStatusDto;
    preMedications?: AnestheticSubstanceDto[];
    procedureDescription?: ProcedureDescriptionDto;
    riskFactors?: RiskFactorDto;
    surgeryProcedures?: HospitalizationProcedureDto[];
}

export interface AnestheticSubstanceDto extends ClinicalTermDto {
    dosage?: NewDosageDto;
    viaId?: number;
    viaNote?: string;
}

export interface AnestheticTechniqueDto extends ClinicalTermDto {
    breathingId?: number;
    circuitId?: number;
    techniqueId?: number;
    trachealIntubation?: boolean;
    trachealIntubationMethodIds?: number[];
}

export interface AnnexIIDto {
    appointmentState: string;
    attentionDate: Date;
    completePatientName: string;
    consultationDate: Date;
    documentNumber: string;
    documentType: string;
    establishment: string;
    existsConsultation: boolean;
    formalPatientName: string;
    hasProcedures: boolean;
    medicalCoverage: string;
    missingProcedures: number;
    patientAge: number;
    patientGender: string;
    problems: string;
    procedures: AnnexIIProcedureDto[];
    proceduresEgressDate: Date;
    proceduresIngressDate: Date;
    proceduresTotal: number;
    rnos: number;
    showProcedures: boolean;
    sisaCode: string;
    specialty: string;
}

export interface AnnexIIProcedureDto {
    amount: number;
    codeNomenclator: string;
    description: string;
    descriptionNomenclator: string;
    patientRate: number;
    rate: number;
}

export interface AnthropometricDataDto extends Serializable {
    bloodType?: ClinicalObservationDto;
    bmi?: ClinicalObservationDto;
    headCircumference?: ClinicalObservationDto;
    height?: ClinicalObservationDto;
    weight?: ClinicalObservationDto;
}

export interface AnthropometricGraphicDataDto {
    datasetInfo: GraphicDatasetInfoDto[];
    evolutionZScoreValues: string[];
    graphicRange: EAnthropometricGraphicRange;
    xaxisLabel: string;
    xaxisRange: string[];
    xaxisRangeLabels: string[];
    yaxisLabel: string;
}

export interface AnthropometricGraphicDto {
    anthropometricGraphicOption: EAnthropometricGraphicOption;
    anthropometricGraphicType: EAnthropometricGraphicType;
}

export interface AnthropometricGraphicEnablementDto {
    hasAnthropometricData: boolean;
    hasValidAge: boolean;
    hasValidGender: boolean;
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
    branch: string;
    commitId: string;
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

export interface AppointmentDataDto {
    appointmentId: number;
    authorFullName: string;
    createdOn: DateTimeDto;
    date: DateDto;
    hour: TimeDto;
    institution: InstitutionInfoDto;
    patientEmail: string;
    phoneNumber: string;
    phonePrefix: string;
    professionalFullName: string;
    state: number;
}

export interface AppointmentDto extends CreateAppointmentDto {
    appointmentStateId: number;
    associatedReferenceClosureType: EReferenceClosureType;
    callLink: string;
    diaryLabelDto?: DiaryLabelDto;
    hasAppointmentChilds?: boolean;
    hasAssociatedReference: boolean;
    id: number;
    observation?: string;
    observationBy?: string;
    parentAppointmentId?: number;
    protected: boolean;
    recurringTypeDto: RecurringTypeDto;
    stateChangeReason?: string;
    transcribedOrderData?: TranscribedServiceRequestSummaryDto;
    updatedOn: DateTimeDto;
}

export interface AppointmentEquipmentShortSummaryDto {
    date: DateDto;
    equipmentName: string;
    hour: TimeDto;
    institution: string;
}

export interface AppointmentListDto {
    appointmentBlockMotiveId: number;
    appointmentStateId: number;
    createdOn: Date;
    date: string;
    diaryLabelDto: DiaryLabelDto;
    expiredRegister: boolean;
    healthInsuranceId: number;
    hour: string;
    id: number;
    medicalAttentionTypeId: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    overturn: boolean;
    patient: AppointmentBasicPatientDto;
    patientEmail: string;
    phoneNumber: string;
    phonePrefix: string;
    professionalPersonDto: ProfessionalPersonDto;
    protected: boolean;
    updatedOn: DateTimeDto;
}

export interface AppointmentOrderDetailImageDto {
    creationDate: Date;
    healthCondition: string;
    idServiceRequest: number;
    observations: string;
    professional: ProfessionalDto;
    professionalOrderTranscribed: string;
}

export interface AppointmentSearchDto {
    aliasOrSpecialtyName?: string;
    daysOfWeek: number[];
    endSearchTime: TimeDto;
    endingSearchDate: DateDto;
    initialSearchDate: DateDto;
    initialSearchTime: TimeDto;
    modality: EAppointmentModality;
    practiceId?: number;
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

export interface AppointmentTicketImageDto {
    date: string;
    documentNumber: string;
    hour: string;
    institution: string;
    medicalCoverage: string;
    patientFullName: string;
    sectorName: string;
    studyDescription: string;
}

export interface AssignedAppointmentDto {
    associatedReferenceClosureType: EReferenceClosureType;
    clinicalSpecialtyName: string;
    date: DateDto;
    hasAssociatedReference: boolean;
    hour: TimeDto;
    id: number;
    license: string;
    office: string;
    professionalName: string;
}

export interface AttentionPlacesQuantityDto {
    bed: number;
    doctorsOffice: number;
    shockroom: number;
}

export interface AttentionTypeReportDto {
    appointments: AttentionTypeReportItemDto[];
    medicalAttentionTypeId: number;
    openingHourFrom: Date;
    openingHourTo: Date;
}

export interface AttentionTypeReportItemDto {
    appointmentState: string;
    completePhone: string;
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

export interface AuditablePatientInfoDto {
    createdBy: string;
    createdOn: DateTimeDto;
    institutionName: string;
    message: string;
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
    id: number;
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
    educationLevel: string;
    email: string;
    ethnicity: string;
    files: PersonFileDto[];
    firstName: string;
    gender: GenderDto;
    genderId: number;
    id: number;
    identificationNumber: string;
    identificationType: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    nameSelfDetermination: string;
    occupation: string;
    otherLastNames: string;
    personAge: PersonAgeDto;
    religion: string;
    selfPerceivedGender: string;
}

export interface BasicPatientDto extends Serializable {
    birthDate: Date;
    firstName: string;
    gender: GenderDto;
    id: number;
    identificationNumber: string;
    identificationType: string;
    lastName: string;
    middleName: string;
    person: BasicDataPersonDto;
    typeId: number;
}

export interface BasicPersonalDataDto extends IBasicPersonalData {
    cuil?: string;
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
    bedNurse: BedNurseDto;
    patient: BasicPatientDto;
    probableDischargeDate: string;
}

export interface BedNurseDto {
    fullName: string;
    identificationNumber: string;
    personId: number;
    userId: number;
}

export interface BedSummaryDto {
    bed: BedDto;
    probableDischargeDate?: string;
    sector: SectorSummaryDto;
    sectorType: SectorTypeDto;
}

export interface BlockDto {
    appointmentBlockMotiveId: number;
    end: TimeDto;
    endDateDto: DateDto;
    fullBlock: boolean;
    init: TimeDto;
    initDateDto: DateDto;
}

export interface BookedAppointmentDto {
    date: DateDto;
    hour: TimeDto;
    office: string;
    professionalName: string;
    specialties: string[];
}

export interface BookingAppointmentDto {
    coverageId: number;
    day: string;
    diaryId: number;
    hour: string;
    openingHoursId: number;
    phoneNumber: string;
    phonePrefix: string;
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

export interface BookingInstitutionExtendedDto {
    address: string;
    aliases: string[];
    city: string;
    clinicalSpecialtiesNames: string[];
    department: string;
    dependency: string;
    description: string;
    id: number;
    sisaCode: string;
}

export interface BookingPersonDto {
    birthDate: string;
    email: string;
    firstName: string;
    genderId: number;
    idNumber: string;
    lastName: string;
    phoneNumber: string;
    phonePrefix: string;
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

export interface CHDocumentSummaryDto {
    documentType: string;
    encounterType: string;
    endDate: string;
    id: number;
    institution: string;
    problems: string;
    professional: string;
    startDate: string;
}

export interface CHSearchFilterDto {
    documentTypeList: ECHDocumentType[];
    encounterTypeList: ECHEncounterType[];
}

export interface CareLineDto extends Serializable {
    clinicalSpecialties: ClinicalSpecialtyDto[];
    description: string;
    id: number;
}

export interface CareLineProblemDto {
    careLineId: number;
    conceptId: number;
    id: number;
    snomedId: number;
}

export interface ChangeStateMedicationRequestDto extends Serializable {
    dayQuantity?: number;
    medicationsIds: number[];
    observations?: string;
}

export interface CipresOutpatientConsultationDto {
    anthropometricData: SharedAnthropometricDataDto;
    clinicalSpecialtyId: number;
    clinicalSpecialtySctid: string;
    date: string;
    id: number;
    institutionId: number;
    institutionSisaCode: string;
    medications: SharedSnomedDto[];
    patient: BasicPatientDto;
    problems: SharedSnomedDto[];
    procedures: SharedSnomedDto[];
    riskFactor: SharedRiskFactorDto;
}

export interface CirculationDto extends Serializable {
    heartRate: NewEffectiveClinicalObservationDto;
    perfusionId: number;
}

export interface CityDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface ClinicHistoryAccessDto {
    observations: string;
    reason: EClinicHistoryAccessReason;
    scope: number;
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

export interface Comparable<T> {
}

export interface CompleteDiaryDto extends DiaryDto {
    associatedProfessionalsInfo: ProfessionalPersonDto[];
    careLinesInfo: CareLineDto[];
    doctorsOfficeDescription: string;
    hierarchicalUnitAlias: string;
    practicesInfo: SnomedDto[];
    sectorDescription: string;
    sectorId: number;
    sectorName: string;
    specialtyName: string;
}

export interface CompleteEquipmentDiaryDto extends EquipmentDiaryDto {
    sectorDescription: string;
    sectorId: number;
}

export interface CompletePatientDto extends BasicPatientDto {
    auditablePatientInfo: AuditablePatientInfoDto;
    generalPractitioner?: AAdditionalDoctorDto;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    pamiDoctor?: AAdditionalDoctorDto;
    patientLastEditInfoDto: PatientLastEditInfoDto;
    patientType: PatientType;
}

export interface CompletePersonDto {
    apartment: string;
    birthDate: Date;
    city: string;
    cityBahraCode: string;
    country: string;
    department: string;
    email: string;
    firstName: string;
    floor: string;
    genderId: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    number: string;
    otherLastNames: string;
    phoneNumber: string;
    phonePrefix: string;
    postcode: string;
    quarter: string;
    street: string;
}

export interface CompleteReferenceDto extends ReferenceDto {
    doctorId: number;
    encounterId: number;
    institutionId: number;
    patientId: number;
    patientMedicalCoverageId: number;
    sourceTypeId: number;
}

export interface CompleteReferenceStudyDto {
    categoryId: string;
    doctorId: number;
    encounterId: number;
    healthConditionId: number;
    institutionId: number;
    patientId: number;
    patientMedicalCoverageId?: number;
    practice: SharedSnomedDto;
    sourceTypeId: number;
}

export interface CompleteRequestDto {
    fileIds?: number[];
    link?: string;
    observations?: string;
    referenceClosure?: ReferenceClosureDto;
}

export interface ConclusionDto extends HealthConditionDto {
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

export interface ConsultationResponseDto {
    encounterId: number;
    orderIds: number[];
}

export interface ConsultationsDto extends Serializable {
    completeProfessionalName: string;
    consultationDate: Date;
    documentId: number;
    id: number;
    specialty: string;
}

export interface CoordinationActionDto<T> {
    organizations: T[];
    other: string;
    within: boolean;
}

export interface CoordinationInsideHealthSectorDto {
    healthInstitutionOrganization: CoordinationActionDto<EHealthInstitutionOrganization>;
    healthSystemOrganization: CoordinationActionDto<EHealthSystemOrganization>;
    wereInternmentIndicated: EIntermentIndicationStatus;
}

export interface CoordinationOutsideHealthSectorDto {
    municipalGovernmentDevices: EMunicipalGovernmentDevice[];
    nationalGovernmentDevices: ENationalGovernmentDevice[];
    provincialGovernmentDevices: EProvincialGovernmentDevice[];
    withOtherSocialOrganizations: boolean;
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
    hierarchicalUnitId?: number;
    medications: CounterReferenceMedicationDto[];
    patientMedicalCoverageId?: number;
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
    applicantHealthcareProfessionalEmail?: string;
    appointmentOptionId?: number;
    date: string;
    diaryId: number;
    expiredReasonId?: number;
    expiredReasonText?: string;
    hour: string;
    id?: number;
    modality: EAppointmentModality;
    openingHoursId: number;
    orderData?: DiagnosticReportInfoDto;
    overturn: boolean;
    patientEmail?: string;
    patientId: number;
    patientMedicalCoverageId?: number;
    phoneNumber?: string;
    phonePrefix?: string;
    referenceId?: number;
}

export interface CreateCustomAppointmentDto {
    createAppointmentDto: CreateAppointmentDto;
    customRecurringAppointmentDto: CustomRecurringAppointmentDto;
}

export interface CreateOutpatientDto {
    allergies: OutpatientAllergyConditionDto[];
    anthropometricData?: OutpatientAnthropometricDataDto;
    clinicalSpecialtyId?: number;
    evolutionNote?: string;
    familyHistories: OutpatientFamilyHistoryDto[];
    hierarchicalUnitId?: number;
    involvedHealthcareProfessionalIds: number[];
    medications: OutpatientMedicationDto[];
    patientMedicalCoverageId?: number;
    personalHistories?: OutpatientPersonalHistoryDto[];
    problems: OutpatientProblemDto[];
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    references: ReferenceDto[];
    riskFactors?: OutpatientRiskFactorDto;
}

export interface CreationableDto extends Serializable {
    createdOn: Date;
}

export interface CustomRecurringAppointmentDto {
    endDate: Date;
    repeatEvery: number;
    weekDayId: number;
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

export interface DateTimeDto extends Comparable<DateTimeDto> {
    date: DateDto;
    time: TimeDto;
}

export interface DeleteApiKeyDto {
    name: string;
}

export interface DentalActionDto extends ClinicalTermDto {
    diagnostic: boolean;
    surface: SnomedDto;
    tooth: SnomedDto;
}

export interface DepartmentDto extends AbstractMasterdataDto<number> {
    id: number;
    provinceId: number;
}

export interface DepartmentInstitutionDto {
    departmentId: number;
    id: number;
    name: string;
    provinceId: number;
}

export interface DependencyDto extends Serializable {
    code: string;
    description: string;
    id: number;
}

export interface DetailsOrderImageDto {
    isReportRequired: boolean;
    observations: string;
}

export interface DiagnosesGeneralStateDto extends DiagnosisDto {
    main: boolean;
}

export interface DiagnosisDto extends HealthConditionDto {
    presumptive?: boolean;
    type?: ProblemTypeEnum;
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
    coverageDto?: PatientMedicalCoverageDto;
    creationDate: Date;
    doctor: DoctorInfoDto;
    healthCondition: HealthConditionInfoDto;
    id: number;
    link?: string;
    observations?: string;
    referenceRequestDto?: ReferenceRequestDto;
    serviceRequestId: number;
    snomed: SnomedDto;
    source: string;
    sourceId: number;
    statusId: string;
}

export interface DiagnosticReportInfoWithFilesDto extends DiagnosticReportInfoDto {
    files: FileDto[];
}

export interface DiagnosticReportSummaryDto {
    diagnosticReportId: number;
    pt: string;
}

export interface DiaryADto {
    alias?: string;
    appointmentDuration: number;
    automaticRenewal?: boolean;
    careLines?: number[];
    clinicalSpecialtyId?: number;
    diaryAssociatedProfessionalsId: number[];
    diaryLabelDto: DiaryLabelDto[];
    diaryOpeningHours: DiaryOpeningHoursDto[];
    doctorsOfficeId: number;
    endDate: string;
    healthcareProfessionalId: number;
    hierarchicalUnitId?: number;
    includeHoliday?: boolean;
    institutionId: number;
    practicesId?: number[];
    predecessorProfessionalId?: number;
    professionalAssignShift?: boolean;
    protectedAppointmentsPercentage?: number;
    startDate: string;
}

export interface DiaryAvailabilityDto {
    diary: BookingDiaryDto;
    slots: AvailabilityDto;
}

export interface DiaryAvailableAppointmentsDto {
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
    practice: SnomedDto;
    professionalFullName: string;
}

export interface DiaryDto extends DiaryADto {
    id: number;
}

export interface DiaryLabelDto {
    colorId: number;
    description: string;
    diaryId: number;
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
    practices: string[];
    professionalAssignShift: boolean;
    startDate: string;
}

export interface DiaryOpeningHoursDto extends Overlapping<DiaryOpeningHoursDto> {
    externalAppointmentsAllowed: boolean;
    medicalAttentionTypeId: number;
    onSiteAttentionAllowed?: boolean;
    openingHours: OpeningHoursDto;
    overturnCount?: number;
    patientVirtualAttentionAllowed?: boolean;
    protectedAppointmentsAllowed: boolean;
    regulationProtectedAppointmentsAllowed?: boolean;
    secondOpinionVirtualAttentionAllowed?: boolean;
}

export interface DiaryOpeningHoursFreeTimesDto {
    freeTimes: TimeDto[];
    openingHoursId: number;
}

export interface DietDto extends IndicationDto {
    description: string;
}

export interface DigitalSignatureCallbackRequestDto {
    documentId: number;
    documento: string;
    hash: string;
    metadata: { [index: string]: string };
    personId: number;
    status: DigitalSignatureStatusDto;
}

export interface DigitalSignatureDataDto {
    cuil: string;
    documents: DigitalSignatureDocumentContentDto[];
    institutionId: number;
    personId: number;
}

export interface DigitalSignatureDocumentContentDto {
    content: FilePathBo;
    hash: string;
    id: number;
}

export interface DigitalSignatureDocumentDto extends Serializable {
    createdOn: Date;
    documentId: number;
    documentTypeDto: DocumentTypeDto;
    patientFullName: string;
    professionalFullName: string;
    snomedConcepts: SnomedConceptDto[];
    sourceTypeDto: SourceTypeDto;
}

export interface DigitalSignatureStatusDto {
    msg: string;
    success: boolean;
}

export interface DoctorInfoDto {
    firstName: string;
    id: number;
    lastName: string;
    nameSelfDetermination: string;
}

export interface DoctorsOfficeDto {
    available: boolean;
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
    clinicalSpecialtyName: string;
    dentalActions: DentalActionDto[];
    diagnosis: DiagnosisDto[];
    diagnosticReports: DiagnosticReportDto[];
    documentSource: number;
    documentType: number;
    encounterId: number;
    familyHistories: HealthHistoryConditionDto[];
    healthcareProfessionals: DocumentHealthcareProfessionalDto[];
    id: number;
    immunizations: ImmunizationDto[];
    institutionId: number;
    mainDiagnosis: HealthConditionDto;
    medicalCoverageId: number;
    medications: MedicationDto[];
    notes: DocumentObservationsDto;
    patientId: number;
    performedDate: DateDto;
    personalHistories: PersonalHistoryDto[];
    problems: ProblemDto[];
    procedures: ProcedureDto[];
    reasons: ReasonDto[];
    riskFactors: RiskFactorDto;
}

export interface DocumentElectronicSignatureProfessionalStatusDto {
    professionalCompleteName: string;
    status: EElectronicSignatureStatus;
}

export interface DocumentFileDataDto {
    filename: string;
    id: number;
    signatureStatus: SignatureStatusTypeDto;
}

export interface DocumentFileDto {
    createdOn: Date;
    creationable: CreationableDto;
    filename: string;
    id: number;
    signatureStatusId: number;
    sourceId: number;
    sourceTypeId: number;
    typeId: number;
}

export interface DocumentFileSummaryDto extends Serializable {
    document: DocumentFileDataDto;
    documentType: DocumentTypeDto;
    sourceId: number;
    sourceType: SourceTypeDto;
    startDate: DateTimeDto;
}

export interface DocumentHealthcareProfessionalDto {
    comments?: string;
    healthcareProfessional: HCEHealthcareProfessionalDto;
    id?: number;
    type: EProfessionType;
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
    signatureStatus: ESignatureStatus;
    sourceId: number;
    typeId: number;
}

export interface DocumentRequestDto {
    documentId: number;
    requestId: number;
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

export interface DocumentTemplateDto {
    name: string;
}

export interface DocumentTypeDto {
    description: string;
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
    dosesByDay: number;
    dosesByUnit: number;
    duration: number;
    expired: boolean;
    frequency: number;
    id: number;
    periodUnit: string;
    quantityDto: QuantityDto;
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

export interface DuplicatePatientDto {
    birthdate: Date;
    firstName: string;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    numberOfCandidates: number;
    otherLastNames: string;
}

export interface EAnthropometricGraphicLabel {
    id: number;
    value: string;
}

export interface EAnthropometricGraphicOption {
    id: number;
    value: string;
}

export interface EAnthropometricGraphicType {
    id: number;
    value: string;
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

export interface EReferenceClosureType {
    description: string;
    id: number;
}

export interface EReferencePriority {
    description: string;
    id: number;
}

export interface EducationLevelDto extends Serializable {
    code: number;
    description: string;
    id: number;
}

export interface EffectiveClinicalObservationDto extends ClinicalObservationDto {
    effectiveTime?: string;
}

export interface ElectronicJointSignatureInstitutionProfessionalDto {
    clinicalSpecialties: string[];
    completeName: string;
    healthcareProfessionalId: number;
    license: ElectronicJointSignatureLicenseDto;
}

export interface ElectronicJointSignatureLicenseDto {
    number: string;
    type: ELicenseNumberType;
}

export interface ElectronicJointSignatureProfessionalsDto {
    professionalsThatDidNotSignAmount: number;
    professionalsThatSignedNames: string[];
}

export interface ElectronicSignatureInvolvedDocumentDto {
    documentCreationDate: DateTimeDto;
    documentId: number;
    documentTypeId: number;
    patientCompleteName: string;
    problems: string[];
    responsibleProfessionalCompleteName: string;
    signatureStatus: EElectronicSignatureStatus;
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

export interface EmergencyCareEpisodeFilterDto {
    identificationNumber: string;
    mustBeEmergencyCareTemporal: boolean;
    mustBeTemporal: boolean;
    patientFirstName: string;
    patientId: number;
    patientLastName: string;
    triageCategoryId: number;
    typeId: number;
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

export interface EmergencyCareEpisodeNotificationDto {
    doctorName: string;
    episodeId: number;
    patientName: string;
    placeName: string;
    topic: string;
    triageCategory: SharedTriageCategoryDto;
}

export interface EmergencyCareEvolutionNoteClinicalData {
    allergies: OutpatientAllergyConditionDto[];
    anthropometricData: OutpatientAnthropometricDataDto;
    diagnosis: DiagnosisDto[];
    evolutionNote: string;
    familyHistories: OutpatientFamilyHistoryDto[];
    mainDiagnosis: HealthConditionDto;
    medications: OutpatientMedicationDto[];
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    riskFactors: OutpatientRiskFactorDto;
}

export interface EmergencyCareEvolutionNoteDocumentDto {
    clinicalSpecialtyName: string;
    documentId: number;
    emergencyCareEvolutionNoteClinicalData: EmergencyCareEvolutionNoteClinicalData;
    fileName: string;
    performedDate: DateTimeDto;
    professional: HealthcareProfessionalDto;
}

export interface EmergencyCareEvolutionNoteDto {
    allergies: OutpatientAllergyConditionDto[];
    anthropometricData: OutpatientAnthropometricDataDto;
    clinicalSpecialtyId: number;
    diagnosis: DiagnosisDto[];
    evolutionNote: string;
    familyHistories: OutpatientFamilyHistoryDto[];
    mainDiagnosis: HealthConditionDto;
    medications: OutpatientMedicationDto[];
    patientId: number;
    procedures: OutpatientProcedureDto[];
    reasons: OutpatientReasonDto[];
    riskFactors: OutpatientRiskFactorDto;
}

export interface EmergencyCareHistoricDocumentDto {
    evolutionNotes: EmergencyCareEvolutionNoteDocumentDto[];
    triages: TriageDocumentDto[];
}

export interface EmergencyCareListDto extends Serializable {
    bed: BedDto;
    creationDate: DateTimeDto;
    doctorsOffice: DoctorsOfficeDto;
    id: number;
    patient: EmergencyCarePatientDto;
    relatedProfessional: ProfessionalPersonDto;
    shockroom: ShockroomDto;
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
    doctorFullName: string;
    doctorsOfficeDescription: string;
    hour: string;
    openingHoursId: number;
    overturnMode: boolean;
    patientId: number;
    practice: SnomedDto;
}

export interface EpicrisisDto extends Serializable {
    allergies: AllergyConditionDto[];
    confirmed: boolean;
    diagnosis: DiagnosisDto[];
    externalCause?: ExternalCauseDto;
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: DiagnosisDto;
    medications: MedicationDto[];
    modificationReason?: string;
    notes?: EpicrisisObservationsDto;
    obstetricEvent?: ObstetricEventDto;
    otherProblems: HealthConditionDto[];
    personalHistories: HealthHistoryConditionDto[];
    procedures: HospitalizationProcedureDto[];
}

export interface EpicrisisGeneralStateDto extends Serializable {
    allergies: AllergyConditionDto[];
    diagnosis: DiagnosisDto[];
    familyHistories: HealthHistoryConditionDto[];
    immunizations: ImmunizationDto[];
    mainDiagnosis: HealthConditionDto;
    medications: MedicationDto[];
    otherProblems: HealthConditionDto[];
    personalHistories: HealthHistoryConditionDto[];
    procedures: HospitalizationProcedureDto[];
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

export interface EpisodeDocumentDto {
    consentId: number;
    episodeDocumentTypeId: number;
    file: MultipartFile;
    internmentEpisodeId: number;
}

export interface EpisodeDocumentResponseDto {
    createdOn: DateDto;
    description: string;
    fileName: string;
    id: number;
}

export interface EpisodeDocumentTypeDto {
    consentId: number;
    description: string;
    id: number;
    richTextBody: string;
}

export interface EquipmentAppointmentListDto {
    appointmentStateId: number;
    date: string;
    derivedTo: InstitutionBasicInfoDto;
    healthInsuranceId: number;
    hour: string;
    id: number;
    medicalCoverageAffiliateNumber: string;
    medicalCoverageName: string;
    overturn: boolean;
    patient: AppointmentBasicPatientDto;
    protected: boolean;
    reportStatusId: number;
    serviceRequestId: number;
    studies?: string[];
    studyName: string;
    transcribedOrderAttachedFiles?: OrderImageFileInfoDto[];
    transcribedServiceRequestId: number;
}

export interface EquipmentDiaryADto {
    appointmentDuration: number;
    automaticRenewal?: boolean;
    endDate: string;
    equipmentDiaryOpeningHours: EquipmentDiaryOpeningHoursDto[];
    equipmentId: number;
    includeHoliday?: boolean;
    startDate: string;
}

export interface EquipmentDiaryDto extends EquipmentDiaryADto {
    id: number;
}

export interface EquipmentDiaryOpeningHoursDto extends Overlapping<EquipmentDiaryOpeningHoursDto> {
    externalAppointmentsAllowed: boolean;
    medicalAttentionTypeId: number;
    onSiteAttentionAllowed?: boolean;
    openingHours: EquipmentOpeningHoursDto;
    overturnCount?: number;
    protectedAppointmentsAllowed: boolean;
    regulationProtectedAppointmentsAllowed?: boolean;
}

export interface EquipmentDto extends Serializable {
    id: number;
    modalityId: number;
    name: string;
}

export interface EquipmentOpeningHoursDto extends TimeRangeDto {
    dayWeekId: number;
    id?: number;
}

export interface ErrorProblemDto extends ProblemInfoDto {
    errorObservations: string;
    errorReasonId: number;
    id: number;
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

export interface ExternalCauseDto {
    eventLocation?: EEventLocation;
    externalCauseType?: EExternalCauseType;
    id?: number;
    snomed?: SnomedDto;
}

export interface ExternalClinicalHistoryDto {
    consultationDate: Date;
    institution?: string;
    notes: string;
    patientDocumentNumber: string;
    patientDocumentType: number;
    patientGender: number;
    professionalName?: string;
    professionalSpecialty?: string;
}

export interface ExternalClinicalHistorySummaryDto extends Serializable {
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

export interface ExternalTemporaryHealthcareProfessionalDto {
    firstName: string;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    licenseNumber: string;
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

export interface FileCreationDto extends Serializable {
    createdOn: Date;
}

export interface FileDto {
    fileId: number;
    fileName: string;
}

export interface FileInfoDto {
    contentType: string;
    createdOn: Date;
    creationable: FileCreationDto;
    generatedBy: string;
    id: number;
    name: string;
    originalPath: string;
    relativePath: string;
    size: number;
    source: string;
    uuidfile: string;
}

export interface FilePathBo {
    fullPath: Path;
    relativePath: string;
}

export interface FilterOptionDto {
    description: string;
    id: number;
}

export interface FoodIntakeDto {
    clockTime: TimeDto;
}

export interface FormVDto {
    address: string;
    affiliateNumber: string;
    bedNumber: string;
    cie10Codes: string;
    completePatientName: string;
    completeProfessionalName: string;
    consultationDate: Date;
    documentNumber: string;
    documentType: string;
    establishment: string;
    establishmentProvinceCode: string;
    formalPatientName: string;
    hcnId: number;
    licenses: string[];
    medicalCoverage: string;
    medicalCoverageCondition: string;
    patientAge: number;
    patientGender: string;
    problems: string;
    reportDate: Date;
    roomNumber: string;
    sisaCode: string;
}

export interface FreeAppointmentSearchFilterDto {
    date: DateDto;
    modality: EAppointmentModality;
    mustBeProtected: boolean;
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

export interface GenerateApiKeyDto {
    name: string;
}

export interface GeneratedApiKeyDto {
    apiKey: string;
    name: string;
}

export interface GraphicDatasetInfoDto {
    intersections: GraphicDatasetIntersectionDto[];
    label: EAnthropometricGraphicLabel;
}

export interface GraphicDatasetIntersectionDto {
    x: string;
    y: string;
}

export interface GroupAppointmentResponseDto {
    appointmentId: number;
    appointmentStateId: number;
    identificationNumber: string;
    patientFullName: string;
    patientId: number;
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
    fullName: string;
    id: number;
    identificationNumber: string;
}

export interface HCEClinicalObservationDto extends Serializable {
    id?: number;
    value: string;
}

export interface HCEClinicalTermDto {
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

export interface HCEErrorProblemDto {
    observations: string;
    reason: string;
    timePerformed: DateTimeDto;
}

export interface HCEEvolutionSummaryDto {
    clinicalSpecialty: ClinicalSpecialtyDto;
    consultationId: number;
    document: HCEDocumentDataDto;
    electronicJointSignatureProfessionals: ElectronicJointSignatureProfessionalsDto;
    evolutionNote: string;
    healthConditions: HCEProblemDto[];
    institutionName: string;
    procedures: HCEProcedureDto[];
    professional: HCEHealthcareProfessionalDto;
    reasons: HCEReasonDto[];
    startDate: DateTimeDto;
}

export interface HCEHealthConditionDto extends HCEClinicalTermDto {
    canBeMarkAsError?: boolean;
    createdOn?: DateTimeDto;
    hasPendingReference: boolean;
    inactivationDate?: string;
    institutionName?: string;
    note?: string;
    professionalName?: string;
    severity?: string;
    startDate?: string;
}

export interface HCEHealthcareProfessionalDto {
    id: number;
    licenseNumber: string;
    person: HCEBasicPersonDataDto;
}

export interface HCEHospitalizationHistoryDto {
    alternativeDiagnoses: HCEDiagnoseDto[];
    dischargeDate: DateTimeDto;
    entryDate: DateTimeDto;
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

export interface HCEPersonalHistoryDto extends HCEHealthConditionDto {
    type?: string;
}

export interface HCEProblemDto extends HCEHealthConditionDto {
    errorProblem?: HCEErrorProblemDto;
    isMarkedAsError?: boolean;
    main: boolean;
    problemId: string;
    references: HCEReferenceDto[];
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
    cancelled?: boolean;
    careLine: string;
    clinicalSpecialties: string[];
    counterReference: HCESummaryCounterReferenceDto;
    destinationInstitutionName: string;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
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

export interface HealthCoordinationDto {
    coordinationInsideHealthSector: CoordinationInsideHealthSectorDto;
    coordinationOutsideHealthSector: CoordinationOutsideHealthSectorDto;
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

export interface HierarchicalUnitDto {
    id: number;
    name: string;
    typeId: number;
}

export interface HierarchicalUnitStaffDto {
    hierarchicalUnitAlias: string;
    hierarchicalUnitId: number;
    id: number;
    responsible: boolean;
}

export interface HierarchicalUnitTypeDto {
    description: string;
    id: number;
}

export interface HierarchicalUnitVo {
    id: number;
    name: string;
    typeId: number;
}

export interface HistoricClinicHistoryDownloadDto {
    downloadDate: DateTimeDto;
    id: number;
    institutionId: number;
    patientId: number;
    user: string;
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
    username: string;
}

export interface HospitalizationProcedureDto {
    id?: number;
    isPrimary?: boolean;
    note?: string;
    performedDate?: string;
    snomed: SnomedDto;
    type?: ProcedureTypeEnum;
}

export interface IBasicPersonalData {
    birthDate: Date;
    firstName: string;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    otherLastNames: string;
    phoneNumber: string;
}

export interface IdentificationTypeDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface IdentifierDto extends Serializable {
    system: string;
    value: string;
}

export interface ImageNetworkProductivityFilterDto {
    clinicalSpecialtyId: number;
    from: DateDto;
    healthcareProfessionalId: number;
    to: DateDto;
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

export interface InformerObservationDto {
    actionTime: DateTimeDto;
    conclusions: ConclusionDto[];
    confirmed: boolean;
    createdBy: string;
    evolutionNote: string;
    id: number;
}

export interface InputStreamSource {
    inputStream: any;
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
    address: string;
    email: string;
    id: number;
    name: string;
    phone: string;
    sisaCode: string;
}

export interface InstitutionReportDto {
    institutionReportPlaces: EInstitutionReportPlace[];
    otherInstitutionReportPlace: string;
    reportReasons: EInstitutionReportReason[];
    reportWasDoneByInstitution: boolean;
}

export interface InstitutionUserPersonDto {
    completeLastName: string;
    completeName: string;
    id: number;
    identificationNumber: string;
    institutionId: number;
    personId: number;
}

export interface InstitutionUserRoleDto {
    rolesId: number[];
}

export interface InstitutionalGroupDto {
    id: number;
    institutions: string;
    name: string;
    typeId: number;
}

export interface InstitutionalGroupInstitutionDto {
    departmentName: string;
    id: number;
    institutionId: number;
    institutionName: string;
    institutionalGroupId: number;
}

export interface InstitutionalGroupRuleDto {
    clinicalSpecialtyId: number;
    comment: string;
    id: number;
    institutionalGroupId: number;
    regulated: boolean;
    ruleId: number;
    ruleLevel: string;
    ruleName: string;
    snomedId: number;
}

export interface InstitutionalGroupTypeDto extends Serializable {
    id: number;
    value: string;
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

export interface Iterable<T> {
}

export interface ItsCoveredResponseDto {
    covered: number;
    message: string;
}

export interface JWTokenDto {
    refreshToken: string;
    token: string;
}

export interface Last2RiskFactorsDto extends Serializable {
    current: RiskFactorDto;
    previous: RiskFactorDto;
}

export interface LicenseDataDto {
    licenseNumber: string;
    licenseType: number;
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
    cuil?: string;
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

export interface ManagerUserPersonDto {
    completeName: string;
    id: number;
    identificationNumber: string;
    personId: number;
    role: string;
    searchText: string;
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

export interface MeasuringPointDto {
    bloodPressureMax?: number;
    bloodPressureMin?: number;
    bloodPulse?: number;
    co2EndTidal?: number;
    date: DateDto;
    o2Saturation?: number;
    time: TimeDto;
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
    hceDocumentData: HCEDocumentDataDto;
    healthCondition: HealthConditionInfoDto;
    id: number;
    isDigital: boolean;
    medicationRequestId: number;
    observations: string;
    prescriptionLineState: number;
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

export interface MergedPatientSearchDto {
    auditType: EAuditType;
    idPatient: number;
    nameSelfDetermination: string;
    numberOfMergedPatients: number;
    patientTypeId: number;
    person: BMPersonDto;
}

export interface ModalityDto {
    acronym: string;
    description: string;
    id: number;
}

export interface MqttMetadataDto {
    message: string;
    qos: number;
    retained: boolean;
    topic: string;
}

export interface MultipartFile extends InputStreamSource {
    bytes: any;
    contentType?: string;
    empty: boolean;
    name: string;
    originalFilename?: string;
    resource: Resource;
    size: number;
}

export interface MultipleDigitalSignatureCallbackRequestDto {
    documentos: DigitalSignatureCallbackRequestDto[];
}

export interface NewDosageDto extends Serializable {
    chronic: boolean;
    diary: boolean;
    dosesByDay?: number;
    dosesByUnit?: number;
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

export interface NewbornDto {
    birthConditionType: EBirthCondition;
    genderId: EGender;
    id?: number;
    weight: number;
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
    hierarchicalUnitId?: number;
    patientMedicalCoverageId?: number;
    problem: NursingProblemDto;
    procedures?: NursingProcedureDto[];
    riskFactors?: NursingRiskFactorDto;
}

export interface NursingConsultationInfoDto {
    billable: boolean;
    clinicalSpecialtyId: number;
    doctorId: number;
    id: number;
    institutionId: number;
    patientId: number;
    patientMedicalCoverageId: number;
    performedDate: Date;
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

export interface ObstetricEventDto {
    currentPregnancyEndDate?: DateDto;
    gestationalAge?: number;
    newborns: NewbornDto[];
    pregnancyTerminationType?: EPregnancyTermination;
    previousPregnancies?: number;
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

export interface OdontologyConsultationDto {
    allergies?: OdontologyAllergyConditionDto[];
    clinicalSpecialtyId: number;
    dentalActions?: OdontologyDentalActionDto[];
    diagnostics?: OdontologyDiagnosticDto[];
    evolutionNote?: string;
    hierarchicalUnitId?: number;
    medications?: OdontologyMedicationDto[];
    patientMedicalCoverageId?: number;
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

export interface OdontologyConsultationInfoDto {
    billable: boolean;
    clinicalSpecialtyId: number;
    doctorId: number;
    id: number;
    institutionId: number;
    patientId: number;
    patientMedicalCoverageId: number;
    performedDate: Date;
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

export interface OdontologyDiagnosticProcedureInfoDto {
    cie10Codes: string;
    diagnostic: boolean;
    id: number;
    noteId: number;
    patientId: number;
    performedDate: Date;
    snomedId: number;
    surfaceId: number;
    toothId: number;
}

export interface OdontologyMedicationDto {
    id?: number;
    note: string;
    snomed: SnomedDto;
    statusId?: string;
    suspended: boolean;
}

export interface OdontologyPersonalHistoryDto {
    inactivationDate?: string;
    note?: string;
    snomed: SnomedDto;
    startDate: string;
    typeId: number;
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

export interface OrderImageFileInfoDto {
    id: number;
    name: string;
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

export interface OutpatientPersonalHistoryDto {
    inactivationDate?: string;
    note?: string;
    snomed: SnomedDto;
    startDate: string;
    typeId: number;
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

export interface PacServerProtocolDto extends Serializable {
    description: string;
    id: number;
}

export interface PacServerTypeDto extends Serializable {
    description: string;
    id: number;
}

export interface PacsListDto {
    urls: string[];
}

export interface PageDto<T> {
    content: T[];
    totalElementsAmount: number;
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

export interface Path extends Comparable<Path>, Iterable<Path>, Watchable {
}

export interface PatientAppointmentHistoryDto {
    city: string;
    clinicalSpecialty: string;
    dateTime: DateTimeDto;
    doctorName: string;
    institution: string;
    practices: SnomedDto[];
    service: string;
    statusId: number;
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

export interface PatientGenderAgeDto {
    age: PersonAgeDto;
    gender: GenderDto;
    id: number;
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

export interface PatientLastEditInfoDto {
    updatedBy: string;
    updatedOn: Date;
    wasEdited: boolean;
}

export interface PatientMedicalCoverageDto {
    active: boolean;
    affiliateNumber?: string;
    condition: EPatientMedicalCoverageCondition;
    conditionValue?: string;
    endDate?: string;
    id?: number;
    medicalCoverage: CoverageDto;
    medicalCoverageName?: string;
    planId?: number;
    planName?: string;
    startDate?: string;
    vigencyDate?: string;
}

export interface PatientPersonalInfoDto {
    birthdate: Date;
    firstName: string;
    genderId: number;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    middleNames: string;
    nameSelfDetermination: string;
    otherLastNames: string;
    patientId: number;
    phoneNumber: string;
    phonePrefix: string;
    typeId: number;
}

export interface PatientPhotoDto {
    imageData: string;
    patientId: number;
}

export interface PatientRegistrationSearchDto {
    auditType: EAuditType;
    idPatient: number;
    nameSelfDetermination: string;
    patientTypeId: number;
    person: BMPersonDto;
    ranking: number;
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

export interface PatientToMergeDto {
    activePatientId: number;
    oldPatientsIds: number[];
    registrationDataPerson: BasicPersonalDataDto;
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

export interface PersonAgeDto {
    days: number;
    months: number;
    totalDays: number;
    years: number;
}

export interface PersonBasicDataResponseDto extends Serializable {
    birthDate: string;
    cuil: string;
    firstName: string;
    identificationNumber: string;
    identificationTypeId: number;
    lastName: string;
    photo: string;
}

export interface PersonDataDto {
    firstName?: string;
    fullName: string;
    identificationNumber: string;
    identificationType: string;
    lastName?: string;
    userId: number;
    username?: string;
}

export interface PersonFileDto {
    fileId: number;
    fileName: string;
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

export interface PersonalHistoryDto extends HealthConditionDto {
    inactivationDate?: string;
    note?: string;
    startDate: string;
    typeId?: number;
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
    note?: string;
    snomed: SharedSnomedDto;
    viaId: number;
}

export interface PoliceInterventionDetailsDto extends Serializable {
    callDate: DateDto;
    callTime: TimeDto;
    firstName: string;
    lastName: string;
    plateNumber: string;
}

export interface PostAnesthesiaStatusDto {
    circulatoryDepression?: boolean;
    cornealReflex?: boolean;
    curated?: boolean;
    intentionalSensitivity?: boolean;
    internment?: boolean;
    internmentPlace?: EInternmentPlace;
    note?: string;
    obeyOrders?: boolean;
    pharyngealCannula?: boolean;
    respiratoryDepression?: boolean;
    talk?: boolean;
    trachealCannula?: boolean;
    vomiting?: boolean;
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
    clinicalSpecialtyId?: number;
    hasRecipe: boolean;
    isArchived?: boolean;
    isPostDated?: boolean;
    items: PrescriptionItemDto[];
    medicalCoverageId?: number;
    observations?: string;
    repetitions?: number;
}

export interface PrescriptionItemDto extends Serializable {
    categoryId?: string;
    dosage?: NewDosageDto;
    healthConditionId: number;
    observations?: string;
    prescriptionLineNumber: number;
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

export interface ProblemInfoDto {
    appointmentsId?: number[];
    diagnosticReportsId?: number[];
    referencesId?: number[];
    serviceRequestsId?: number[];
}

export interface ProcedureDescriptionDto {
    anesthesiaEndDate?: DateDto;
    anesthesiaEndTime?: TimeDto;
    anesthesiaStartDate?: DateDto;
    anesthesiaStartTime?: TimeDto;
    asa?: number;
    nasogastricTube?: boolean;
    note?: string;
    surgeryEndDate?: DateDto;
    surgeryEndTime?: TimeDto;
    surgeryStartDate?: DateDto;
    surgeryStartTime?: TimeDto;
    urinaryCatheter?: boolean;
    venousAccess?: boolean;
}

export interface ProcedureDto {
    performedDate?: string;
    snomed: SnomedDto;
}

export interface ProcedureParameterDto {
    id: number;
    inputCount: number;
    loincId: number;
    orderNumber: number;
    procedureTemplateId: number;
    snomedGroupId: number;
    textOptions: string[];
    typeId: number;
    unitsOfMeasureIds: number[];
}

export interface ProcedureReduced {
    performedDate: Date;
    procedure: string;
}

export interface ProcedureTemplateDto {
    associatedPractices: SnomedPracticeDto[];
    description: string;
    id: number;
    statusId: number;
    uuid: string;
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
    middleNames: string;
    nameSelfDetermination: string;
    otherLastNames: string;
    personId: number;
    professions: ProfessionCompleteDto[];
    professionsAsString: string;
}

export interface ProfessionalDto {
    firstName: string;
    fullName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    middleNames: string;
    nameSelfDetermination: string;
    otherLastNames: string;
    phoneNumber: string;
}

export interface ProfessionalInfoDto {
    clinicalSpecialties: ClinicalSpecialtyDto[];
    firstName: string;
    id: number;
    identificationNumber: string;
    lastName: string;
    licenceNumber: string;
    middleNames: string;
    nameSelfDetermination: string;
    otherLastNames: string;
    phoneNumber: string;
}

export interface ProfessionalLicenseNumberDto extends Serializable {
    healthcareProfessionalSpecialtyId?: number;
    id?: number;
    licenseNumber: string;
    professionalProfessionId: number;
    typeId: number;
}

export interface ProfessionalLicenseNumberValidationResponseDto {
    healthcareProfessionalCompleteContactData: boolean;
    healthcareProfessionalHasLicenses: boolean;
    healthcareProfessionalLicenseNumberValid: boolean;
    patientEmail?: string;
    twoFactorAuthenticationEnabled: boolean;
}

export interface ProfessionalPersonDto extends Serializable {
    firstName: string;
    fullName: string;
    id: number;
    lastName: string;
    middleNames: string;
    nameSelfDetermination: string;
    otherLastNames: string;
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

export interface ProfessionalRegistrationNumbersDto {
    firstName: string;
    healthcareProfessionalId: number;
    lastName: string;
    license: ProfessionalLicenseNumberDto[];
    nameSelfDetermination?: string;
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
    id: number;
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

export interface RecurringTypeDto {
    id: number;
    value: string;
}

export interface ReducedPatientDto {
    patientTypeId: number;
    personalDataDto: BasicPersonalDataDto;
}

export interface ReferenceAppointmentDto {
    appointmentId: number;
    appointmentStateId: number;
    authorFullName: string;
    createdOn: DateTimeDto;
    date: DateTimeDto;
    institution: ReferenceInstitutionDto;
    professionalFullName: string;
}

export interface ReferenceAppointmentStateDto {
    appointmentStateId: number;
    referenceClosureTypeId: number;
    referenceId: number;
}

export interface ReferenceClosureDto {
    clinicalSpecialtyId: number;
    closureTypeId: number;
    counterReferenceNote: string;
    fileIds: number[];
    referenceId: number;
}

export interface ReferenceCompleteDataDto {
    appointment: ReferenceAppointmentDto;
    forwarding: ReferenceForwardingDto;
    observation: ReferenceObservationDto;
    patient: ReferencePatientDto;
    reference: ReferenceDataDto;
    regulation: ReferenceRegulationDto;
}

export interface ReferenceCounterReferenceFileDto extends Serializable {
    fileId: number;
    fileName: string;
}

export interface ReferenceDataDto {
    careLine: CareLineDto;
    clinicalSpecialtyOrigin: ClinicalSpecialtyDto;
    closureType: EReferenceClosureType;
    consultation: boolean;
    createdBy: number;
    date: DateTimeDto;
    destinationClinicalSpecialties: ClinicalSpecialtyDto[];
    encounterId: number;
    files: ReferenceCounterReferenceFileDto[];
    id: number;
    institutionDestination: ReferenceInstitutionDto;
    institutionOrigin: ReferenceInstitutionDto;
    note: string;
    patientMedicalCoverageId: number;
    priority: EReferencePriority;
    problems: string[];
    procedure: SharedSnomedDto;
    procedureCategory: string;
    professionalFullName: string;
}

export interface ReferenceDto extends Serializable {
    careLineId?: number;
    clinicalSpecialtyIds?: number[];
    consultation?: boolean;
    destinationInstitutionId: number;
    fileIds: number[];
    note?: string;
    phoneNumber?: string;
    phonePrefix?: string;
    priority: number;
    problems: ReferenceProblemDto[];
    procedure?: boolean;
    study?: ReferenceStudyDto;
}

export interface ReferenceForwardingDto {
    createdBy: string;
    date: DateTimeDto;
    id: number;
    observation: string;
    type: EReferenceForwardingType;
    userId: number;
}

export interface ReferenceInstitutionDto {
    departmentId: number;
    departmentName: string;
    description: string;
    id: number;
    provinceName: string;
}

export interface ReferenceObservationDto {
    createdBy: string;
    date: DateTimeDto;
    observation: string;
}

export interface ReferencePatientDto {
    email: string;
    identificationNumber: string;
    identificationType: string;
    patientFullName: string;
    patientId: number;
    phoneNumber: string;
    phonePrefix: string;
}

export interface ReferencePhoneDto {
    phoneNumber: string;
    phonePrefix: string;
}

export interface ReferenceProblemDto extends Serializable {
    id?: number;
    snomed: SharedSnomedDto;
}

export interface ReferenceRegulationDto {
    createdOn: DateTimeDto;
    professionalName: string;
    reason: string;
    referenceId: number;
    ruleId: number;
    ruleLevel: string;
    state: EReferenceRegulationState;
}

export interface ReferenceReportDto {
    attentionState: EReferenceAttentionState;
    careLine: string;
    clinicalSpecialtyOrigin: string;
    closureType: EReferenceClosureType;
    date: DateTimeDto;
    destinationClinicalSpecialties: string[];
    forwardingType: string;
    id: number;
    identificationNumber: string;
    identificationType: string;
    institutionDestination: string;
    institutionOrigin: string;
    patientFullName: string;
    priority: EReferencePriority;
    problems: string[];
    procedure: string;
    regulationState: EReferenceRegulationState;
}

export interface ReferenceRequestDto extends Serializable {
    careLineDescription: string;
    careLineId: number;
    clinicalSpecialties: ClinicalSpecialtyDto[];
    closureDateTime: DateTimeDto;
    closureTypeDescription: string;
    closureTypeId: number;
    id: number;
    observation: string;
    priority: string;
    professionalInfo: ProfessionalCompleteDto;
}

export interface ReferenceServiceRequestProcedureDto {
    category: string;
    procedure: SharedSnomedDto;
    serviceRequestId: number;
}

export interface ReferenceStudyDto {
    categoryId: string;
    practice: SharedSnomedDto;
    problem: SharedSnomedDto;
}

export interface ReferenceSummaryDto {
    date: DateDto;
    id: number;
    institution: string;
    professionalFullName: string;
}

export interface ReferenceSummaryNoteDto extends Serializable {
    description: string;
    id: number;
}

export interface RefreshTokenDto {
    refreshToken: string;
}

export interface RejectDocumentElectronicJointSignatureDto {
    description: string;
    documentIds: number[];
    rejectReason: ERejectDocumentElectronicJointSignatureReason;
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

export interface Resource extends InputStreamSource {
    description: string;
    file: any;
    filename?: string;
    open: boolean;
    readable: boolean;
    uri: URI;
    url: URL;
}

export interface ResponseAnamnesisDto extends AnamnesisDto {
    id: number;
}

export interface ResponseEmergencyCareDto extends EmergencyCareDto {
    bed: BedDto;
    creationDate: DateTimeDto;
    doctorsOffice: DoctorsOfficeDto;
    emergencyCareState: MasterDataDto;
    endDate: DateTimeDto;
    id: number;
    institutionName: string;
    shockroom: ShockroomDto;
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
    hematocrit?: EffectiveClinicalObservationDto;
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

export interface RuleDto {
    clinicalSpecialtyId: number;
    id: number;
    level: number;
    name: string;
    snomedId: number;
}

export interface SavedBookingAppointmentDto {
    appointmentId: number;
    bookingPersonId: number;
    uuid: string;
}

export interface SavedEpisodeDocumentResponseDto {
    createdOn: DateDto;
    episodeDocumentTypeId: number;
    fileName: string;
    filePath: string;
    id: number;
    internmentEpisodeId: number;
    uuidFile: string;
}

export interface SectorDto {
    description: string;
    hasDoctorsOffice: boolean;
    id: number;
    type: number;
}

export interface SectorSummaryDto {
    ageGroup: string;
    ageGroupId: number;
    careType: string;
    careTypeId: number;
    clinicalSpecialties: ClinicalSpecialtyDto[];
    description: string;
    hierarchicalUnit: HierarchicalUnitVo[];
    id: number;
    organizationType: string;
    organizationTypeId: number;
    sectorType: string;
    sectorTypeId: number;
}

export interface SectorTypeDto {
    description: string;
    id: number;
}

export interface SecurityForceRelatedDto {
    belongsToSecurityForces: boolean;
    inDuty: boolean;
    securityForceTypes: ESecurityForceType;
}

export interface SelfPerceivedGenderDto extends AbstractMasterdataDto<number> {
    id: number;
}

export interface SendUsageReportDto {
    code: string;
}

export interface Serializable {
}

export interface ServiceRequestCategoryDto {
    description: string;
    id: string;
}

export interface SexualViolenceDto {
    implementedActions: ESexualViolenceAction[];
    wasSexualViolence: boolean;
}

export interface SharedAddressDto {
    apartment: string;
    bahraCode: string;
    cityName: string;
    countryName: string;
    departmentName: string;
    floor: string;
    number: string;
    postCode: string;
    street: string;
}

export interface SharedAnthropometricDataDto {
    bloodType: string;
    bmi: string;
    headCircumference: string;
    height: string;
    weight: string;
}

export interface SharedRiskFactorDto {
    diastolicBloodPressure: string;
    systolicBloodPressure: string;
}

export interface SharedRuleDto {
    clinicalSpecialtyId: number;
    id: number;
    level: number;
    name: string;
    snomedId: number;
}

export interface SharedSnomedDto extends Serializable {
    id?: number;
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

export interface SharedTriageCategoryDto {
    colorCode: string;
    name: string;
}

export interface ShockroomDto {
    available: boolean;
    description: string;
    id: number;
}

export interface SignatureStatusTypeDto {
    description: string;
    id: number;
}

export interface SipPlusUrlDataDto {
    embedSystem: string;
    token: string;
    urlBase: string;
}

export interface SnomedConceptDto {
    isMainHealthCondition: boolean;
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

export interface SnomedPracticeDto {
    id: number;
    pt: string;
    sctid: string;
}

export interface SnomedProblemDto {
    conceptId: number;
    conceptPt: string;
    conceptSctid: string;
    groupDescription: string;
    groupId: number;
    id: number;
}

export interface SnomedProcedureDto {
    conceptId: number;
    conceptPt: string;
    conceptSctid: string;
    groupDescription: string;
    groupId: number;
    id: number;
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

export interface SourceTypeDto {
    description: string;
    id: number;
}

export interface StudyAppointmentDto {
    actionTime: DateTimeDto;
    completionInstitution: InstitutionBasicInfoDto;
    imageId?: string;
    informerObservations?: InformerObservationDto;
    isAvailableInPACS: boolean;
    patientFullName: string;
    patientId: number;
    sizeImage?: number;
    statusId: number;
    technicianObservations?: string;
}

export interface StudyDto extends Serializable {
    diagosticReportCategoryId: string;
    healthConditionId: number;
    observations?: string;
    snomed: SnomedDto;
}

export interface StudyFileInfoDto {
    token: string;
    url: string;
    uuid: string;
}

export interface StudyIntanceUIDDto {
    uid: string;
}

export interface StudyOrderReportInfoDto {
    creationDate: Date;
    diagnosticReportId: number;
    doctor: DoctorInfoDto;
    hasActiveAppointment: boolean;
    hceDocumentDataDto?: HCEDocumentDataDto;
    healthCondition: string;
    imageId?: string;
    isAvailableInPACS: boolean;
    serviceRequestId: number;
    snomed: string;
    source: string;
    status: boolean;
    viewReport: boolean;
}

export interface StudyTranscribedOrderReportInfoDto {
    creationDate: Date;
    diagnosticReports: string[];
    hceDocumentDataDto?: HCEDocumentDataDto;
    healthCondition: string;
    imageId?: string;
    isAvailableInPACS: boolean;
    professionalName: string;
    snomed: string;
    status: boolean;
    viewReport: boolean;
}

export interface StudyWithoutOrderReportInfoDto {
    hceDocumentDataDto?: HCEDocumentDataDto;
    imageId?: string;
    isAvailableInPACS: boolean;
    snomed: string;
    status: boolean;
    viewReport: boolean;
}

export interface SurgicalReportDto extends Serializable {
    anesthesia?: HospitalizationProcedureDto[];
    confirmed: boolean;
    cultures?: HospitalizationProcedureDto[];
    description?: string;
    drainages?: HospitalizationProcedureDto[];
    endDateTime?: DateTimeDto;
    frozenSectionBiopsies?: HospitalizationProcedureDto[];
    healthcareProfessionals?: DocumentHealthcareProfessionalDto[];
    modificationReason?: string;
    postoperativeDiagnosis?: DiagnosisDto[];
    preoperativeDiagnosis?: DiagnosisDto[];
    procedures?: HospitalizationProcedureDto[];
    prosthesisDescription?: string;
    startDateTime?: DateTimeDto;
    surgeryProcedures?: HospitalizationProcedureDto[];
}

export interface TemplateNamesDto {
    id: number;
    name: string;
}

export interface TerminologyCSVDto {
    ecl: SnomedECL;
    kind: ETerminologyKind;
    url: string;
}

export interface TerminologyECLStatusDto {
    ecl: SnomedECL;
    successful: TerminologyQueueItemDto;
}

export interface TerminologyQueueItemDto {
    createdOn: DateTimeDto;
    downloadedError: string;
    downloadedFile: boolean;
    downloadedOn: DateTimeDto;
    ecl: SnomedECL;
    id: number;
    ingestedError: string;
    ingestedOn: DateTimeDto;
    url: string;
}

export interface TextTemplateDto extends DocumentTemplateDto {
    text: string;
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

export interface TokenDto {
    token: string;
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

export interface TranscribedServiceRequestDto {
    diagnosticReports: SnomedDto[];
    healthCondition: SnomedDto;
    healthcareProfessionalName: string;
    institutionName: string;
    observations?: string;
    oldTranscribedOrderId?: number;
}

export interface TranscribedServiceRequestSummaryDto {
    diagnosticReports: DiagnosticReportSummaryDto[];
    serviceRequestId: number;
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

export interface TriageDocumentDto {
    documentId: number;
    fileName: string;
    triage: TriageListDto;
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

export interface URI extends Comparable<URI>, Serializable {
}

export interface URL extends Serializable {
}

export interface UnsatisfiedAppointmentDemandDto {
    aliasOrSpecialtyName: string;
    daysOfWeek: number[];
    endSearchTime: TimeDto;
    endingSearchDate: DateDto;
    initialSearchDate: DateDto;
    initialSearchTime: TimeDto;
    modality: EAppointmentModality;
    practiceId: number;
}

export interface UpdateAppointmentDateDto {
    appointmentId: number;
    date: DateTimeDto;
    modality: EAppointmentModality;
    openingHoursId: number;
    patientEmail?: string;
    recurringAppointmentTypeId?: number;
}

export interface UpdateAppointmentDto {
    appointmentId: number;
    appointmentStateId: number;
    overturn: boolean;
    patientId: number;
    patientMedicalCoverageId?: number;
    phoneNumber?: string;
    phonePrefix?: string;
}

export interface UsageReportStatusDto {
    domainId: string;
    isAllowedToSend: boolean;
}

export interface UserApiKeyDto {
    name: string;
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
    middleNames: string;
    nameSelfDetermination: string;
    othersLastNames: string;
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

export interface VaccineConsultationInfoDto {
    billable: boolean;
    clinicalSpecialtyId: number;
    doctorId: number;
    id: number;
    institutionId: number;
    patientId: number;
    patientMedicalCoverageId: number;
    performedDate: Date;
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

export interface ValidatedLicenseDataDto {
    licenseNumber: string;
    licenseType: number;
    validLicenseNumber: boolean;
    validLicenseType: boolean;
}

export interface ValidatedLicenseNumberDto {
    isValid: boolean;
    licenseNumber: string;
}

export interface VerificationCodeDto {
    code: string;
}

export interface VictimKeeperReportDto {
    reportPlaces: EVictimKeeperReportPlace[];
    werePreviousEpisodesWithVictimOrKeeper: boolean;
}

export interface ViewerUrlDto {
    url: string;
}

export interface ViolenceEpisodeDetailDto {
    episodeDate: DateDto;
    riskLevel: EViolenceEvaluationRiskLevel;
    violenceModalitySnomedList: SnomedDto[];
    violenceTowardsUnderage: ViolenceTowardsUnderageDto;
    violenceTypeSnomedList: SnomedDto[];
}

export interface ViolenceReportActorDto<T> {
    actorPersonalData: ViolenceReportPersonDto;
    otherRelationshipWithVictim: string;
    relationshipWithVictim: T;
}

export interface ViolenceReportAggressorDto {
    aggressorData: ViolenceReportActorDto<EAggressorRelationship>;
    hasBeenTreated: boolean;
    hasGuns: boolean;
    hasPreviousEpisodes: ECriminalRecordStatus;
    livesWithVictim: ELiveTogetherStatus;
    relationshipLength: ERelationshipLength;
    securityForceRelatedData: SecurityForceRelatedDto;
    violenceViolenceFrequency: EViolenceFrequency;
}

export interface ViolenceReportDisabilityDto {
    disabilityCertificateStatus: EDisabilityCertificateStatus;
    hasDisability: boolean;
}

export interface ViolenceReportDto {
    aggressorData: ViolenceReportAggressorDto[];
    episodeData: ViolenceEpisodeDetailDto;
    implementedActions: ViolenceReportImplementedActionsDto;
    observation: string;
    victimData: ViolenceReportVictimDto;
}

export interface ViolenceReportFilterDto {
    institutionId: number;
    modalityId: number;
    situationId: number;
    typeId: number;
}

export interface ViolenceReportFilterOptionDto {
    institutions: FilterOptionDto[];
    modalities: FilterOptionDto[];
    situations: FilterOptionDto[];
    types: FilterOptionDto[];
}

export interface ViolenceReportImplementedActionsDto {
    healthCoordination: HealthCoordinationDto;
    institutionReport: InstitutionReportDto;
    sexualViolence: SexualViolenceDto;
    victimKeeperReport: VictimKeeperReportDto;
}

export interface ViolenceReportIncomeInformationDto {
    hasIncome: boolean;
    worksAtFormalSector: boolean;
}

export interface ViolenceReportInstitutionalizedDto {
    institutionalizedDetails: string;
    isInstitutionalized: boolean;
}

export interface ViolenceReportPersonDto {
    address: string;
    age: number;
    firstName: string;
    lastName: string;
    municipality: DepartmentDto;
}

export interface ViolenceReportSituationDto {
    initialDate: DateDto;
    lastModificationDate: DateDto;
    riskLevel: EViolenceEvaluationRiskLevel;
    situationId: number;
    violenceModalities: string[];
    violenceTypes: string[];
}

export interface ViolenceReportSituationEvolutionDto {
    createdOn: DateTimeDto;
    episodeDate: DateDto;
    evolutionId: number;
    professionalFullName: string;
    situationId: number;
}

export interface ViolenceReportVictimDto {
    canReadAndWrite: boolean;
    disabilityData: ViolenceReportDisabilityDto;
    hasSocialPlan: boolean;
    incomeData: ViolenceReportIncomeInformationDto;
    institutionalizedData: ViolenceReportInstitutionalizedDto;
    keeperData: ViolenceReportActorDto<EKeeperRelationship>;
    lackOfLegalCapacity: boolean;
}

export interface ViolenceTowardsUnderageDto {
    schoolLevel: ESchoolLevel;
    schooled: boolean;
    type: EViolenceTowardsUnderageType;
}

export interface VirtualConsultationAvailableProfessionalAmountDto {
    professionalAmount: number;
    virtualConsultationId: number;
}

export interface VirtualConsultationDto {
    availableProfessionalsAmount?: number;
    callLink?: string;
    careLine: string;
    clinicalSpecialty: string;
    creationDateTime: DateTimeDto;
    id: number;
    institutionData: VirtualConsultationInstitutionDataDto;
    motive: string;
    patientData: VirtualConsultationPatientDataDto;
    priority: EVirtualConsultationPriority;
    problem: string;
    responsibleData: VirtualConsultationResponsibleDataDto;
    status: EVirtualConsultationStatus;
}

export interface VirtualConsultationEventDto {
    event: EVirtualConsultationEvent;
    virtualConsultationId: number;
}

export interface VirtualConsultationFilterDto {
    availability?: boolean;
    careLineId?: number;
    clinicalSpecialtyId?: number;
    institutionId?: number;
    priority?: EVirtualConsultationPriority;
    responsibleHealthcareProfessionalId?: number;
    status?: EVirtualConsultationStatus;
}

export interface VirtualConsultationInstitutionDataDto {
    id: number;
    name: string;
}

export interface VirtualConsultationNotificationDataDto {
    callLink: string;
    clinicalSpecialty: string;
    creationDateTime: DateTimeDto;
    institutionName: string;
    patientData: PatientDto;
    priority: EVirtualConsultationPriority;
    responsibleFirstName: string;
    responsibleLastName: string;
    responsibleUserId: number;
    virtualConsultationId: number;
}

export interface VirtualConsultationPatientDataDto {
    age: number;
    gender: string;
    id: number;
    lastName: string;
    name: string;
}

export interface VirtualConsultationRequestDto {
    careLineId: number;
    clinicalSpecialtyId: number;
    motive: SnomedDto;
    patientId: number;
    priority: EVirtualConsultationPriority;
    problem?: SnomedDto;
}

export interface VirtualConsultationResponsibleDataDto {
    available: boolean;
    firstName?: string;
    healthcareProfessionalId: number;
    lastName?: string;
}

export interface VirtualConsultationResponsibleProfessionalAvailabilityDto {
    available: boolean;
    healthcareProfessionalId: number;
    institutionId: number;
}

export interface VirtualConsultationStatusDataDto {
    status: EVirtualConsultationStatus;
    virtualConsultationId: number;
}

export interface VirtualConsultationStatusDto {
    status: EVirtualConsultationStatus;
}

export interface Watchable {
}

export interface WeekDayDto {
    description: string;
    id: number;
}

export interface WorklistDto {
    actionTime: DateTimeDto;
    appointmentId: number;
    completionInstitution: InstitutionBasicInfoDto;
    patientFullName: string;
    patientId: number;
    patientIdentificationNumber: string;
    patientIdentificationTypeId: number;
    statusId: number;
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
    HABILITAR_RECUPERAR_PASSWORD = "HABILITAR_RECUPERAR_PASSWORD",
    HABILITAR_DESARROLLO_RED_IMAGENES = "HABILITAR_DESARROLLO_RED_IMAGENES",
    HABILITAR_SIP_PLUS = "HABILITAR_SIP_PLUS",
    HABILITAR_VALIDACION_MATRICULAS_SISA = "HABILITAR_VALIDACION_MATRICULAS_SISA",
    HABILITAR_RECETA_DIGITAL = "HABILITAR_RECETA_DIGITAL",
    HABILITAR_PRESCRIPCION_RECETA = "HABILITAR_PRESCRIPCION_RECETA",
    HABILITAR_MODULO_AUDITORIA = "HABILITAR_MODULO_AUDITORIA",
    HABILITAR_CAMPOS_CIPRES_EPICRISIS = "HABILITAR_CAMPOS_CIPRES_EPICRISIS",
    HABILITAR_IMPRESION_HISTORIA_CLINICA_EN_DESARROLLO = "HABILITAR_IMPRESION_HISTORIA_CLINICA_EN_DESARROLLO",
    HABILITAR_API_CONSUMER = "HABILITAR_API_CONSUMER",
    HABILITAR_TELEMEDICINA = "HABILITAR_TELEMEDICINA",
    HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO = "HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO",
    HABILITAR_OBLIGATORIEDAD_UNIDADES_JERARQUICAS = "HABILITAR_OBLIGATORIEDAD_UNIDADES_JERARQUICAS",
    HABILITAR_FIRMA_DIGITAL = "HABILITAR_FIRMA_DIGITAL",
    HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION = "HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION",
    HABILITAR_TURNOS_CENTRO_LLAMADO = "HABILITAR_TURNOS_CENTRO_LLAMADO",
    HABILITAR_AUDITORIA_DE_ACCESO_EN_HC = "HABILITAR_AUDITORIA_DE_ACCESO_EN_HC",
    HABILITAR_MONITOREO_CIPRES = "HABILITAR_MONITOREO_CIPRES",
    HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO = "HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO",
    HABILITAR_AGENDA_DINAMICA = "HABILITAR_AGENDA_DINAMICA",
    ROLES_API_PUBLICA_EN_DESARROLLO = "ROLES_API_PUBLICA_EN_DESARROLLO",
    HABILITAR_RECURRENCIA_EN_DESARROLLO = "HABILITAR_RECURRENCIA_EN_DESARROLLO",
    HABILITAR_FIRMA_CONJUNTA = "HABILITAR_FIRMA_CONJUNTA",
    HABILITAR_ACTUALIZACION_AGENDA = "HABILITAR_ACTUALIZACION_AGENDA",
    HABILITAR_ADMINISTRADOR_DATOS_PERSONALES = "HABILITAR_ADMINISTRADOR_DATOS_PERSONALES",
    HABILITAR_ANEXO_II_MENDOZA = "HABILITAR_ANEXO_II_MENDOZA",
    HABILITAR_GRAFICOS_EVOLUCIONES_ANTROPOMETRICAS_EN_DESARROLLO = "HABILITAR_GRAFICOS_EVOLUCIONES_ANTROPOMETRICAS_EN_DESARROLLO",
    HABILITAR_VISTA_COBERTURA_TURNOS = "HABILITAR_VISTA_COBERTURA_TURNOS",
    HABILITAR_LIMITE_TURNOS_PERSONA_PROFESIONAL = "HABILITAR_LIMITE_TURNOS_PERSONA_PROFESIONAL",
    HABILITAR_NOTA_EVOLUCION_GUARDIA_ROL_ENFERMERO = "HABILITAR_NOTA_EVOLUCION_GUARDIA_ROL_ENFERMERO",
}

export const enum EAggressorRelationship {
    PARTNER = "PARTNER",
    EX_PARTNER = "EX_PARTNER",
    FATHER = "FATHER",
    STEPFATHER = "STEPFATHER",
    MOTHER = "MOTHER",
    STEPMOTHER = "STEPMOTHER",
    SON = "SON",
    DAUGHTER = "DAUGHTER",
    SIBLING = "SIBLING",
    SUPERIOR = "SUPERIOR",
    ACQUAINTANCE = "ACQUAINTANCE",
    NO_RELATIONSHIP = "NO_RELATIONSHIP",
    NO_INFORMATION = "NO_INFORMATION",
    DOES_NOT_ANSWER = "DOES_NOT_ANSWER",
}

export const enum EAnthropometricGraphicRange {
    SIX_MONTHS = "SIX_MONTHS",
    FIVE_YEARS = "FIVE_YEARS",
    TEN_YEARS = "TEN_YEARS",
    NINETEEN_YEARS = "NINETEEN_YEARS",
    WEIGHT_FOR_LENGTH = "WEIGHT_FOR_LENGTH",
    WEIGHT_FOR_HEIGHT = "WEIGHT_FOR_HEIGHT",
    TWO_TO_FIVE_YEARS = "TWO_TO_FIVE_YEARS",
    TWO_TO_NINETEEN_YEARS = "TWO_TO_NINETEEN_YEARS",
    TWO_YEARS = "TWO_YEARS",
}

export const enum EAppointmentModality {
    NO_MODALITY = "NO_MODALITY",
    ON_SITE_ATTENTION = "ON_SITE_ATTENTION",
    PATIENT_VIRTUAL_ATTENTION = "PATIENT_VIRTUAL_ATTENTION",
    SECOND_OPINION_VIRTUAL_ATTENTION = "SECOND_OPINION_VIRTUAL_ATTENTION",
}

export const enum EAuditType {
    UNAUDITED = "UNAUDITED",
    TO_AUDIT = "TO_AUDIT",
    AUDITED = "AUDITED",
}

export const enum EBirthCondition {
    BORN_ALIVE = "BORN_ALIVE",
    FETAL_DEATH = "FETAL_DEATH",
}

export const enum ECHDocumentType {
    EPICRISIS = "EPICRISIS",
    REPORTS = "REPORTS",
    MEDICAL_PRESCRIPTIONS = "MEDICAL_PRESCRIPTIONS",
    CLINICAL_NOTES = "CLINICAL_NOTES",
    OTHER = "OTHER",
    NOT_SUPPORTED = "NOT_SUPPORTED",
}

export const enum ECHEncounterType {
    HOSPITALIZATION = "HOSPITALIZATION",
    EMERGENCY_CARE = "EMERGENCY_CARE",
    OUTPATIENT = "OUTPATIENT",
}

export const enum EClinicHistoryAccessReason {
    MEDICAL_EMERGENCY = "MEDICAL_EMERGENCY",
    PROFESSIONAL_CONSULTATION = "PROFESSIONAL_CONSULTATION",
    PATIENT_CONSULTATION = "PATIENT_CONSULTATION",
    AUDIT = "AUDIT",
}

export const enum ECriminalRecordStatus {
    YES = "YES",
    WITH_OTHER_PEOPLE = "WITH_OTHER_PEOPLE",
    NO = "NO",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum EDisabilityCertificateStatus {
    HAS_CERTIFICATE = "HAS_CERTIFICATE",
    HAS_NOT_CERTIFICATE = "HAS_NOT_CERTIFICATE",
    PENDING = "PENDING",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum EDocumentSearch {
    DIAGNOSIS = "DIAGNOSIS",
    DOCTOR = "DOCTOR",
    CREATED_ON = "CREATED_ON",
    DOCUMENT_TYPE = "DOCUMENT_TYPE",
}

export const enum EElectronicSignatureStatus {
    PENDING = "PENDING",
    SIGNED = "SIGNED",
    REJECTED = "REJECTED",
    OUTDATED = "OUTDATED",
}

export const enum EEventLocation {
    DOMICILIO_PARTICULAR = "DOMICILIO_PARTICULAR",
    VIA_PUBLICA = "VIA_PUBLICA",
    LUGAR_DE_TRABAJO = "LUGAR_DE_TRABAJO",
    OTRO = "OTRO",
}

export const enum EExternalCauseType {
    ACCIDENT = "ACCIDENT",
    SELF_INFLICTED_INJURY = "SELF_INFLICTED_INJURY",
    AGRESSION = "AGRESSION",
    IGNORED = "IGNORED",
}

export const enum EGender {
    FEMALE = "FEMALE",
    MALE = "MALE",
    X = "X",
}

export const enum EHealthInstitutionOrganization {
    MEDICAL_CLINIC = "MEDICAL_CLINIC",
    PEDIATRICS = "PEDIATRICS",
    GYNECOLOGY_OBSTETRICS = "GYNECOLOGY_OBSTETRICS",
    SOCIAL_WORK = "SOCIAL_WORK",
    MENTAL_HEALTH = "MENTAL_HEALTH",
    NURSING = "NURSING",
    SAPS = "SAPS",
    EDA = "EDA",
    EMERGENCY_CARE = "EMERGENCY_CARE",
    COMMITTEE = "COMMITTEE",
    MANAGEMENT = "MANAGEMENT",
    OTHERS = "OTHERS",
}

export const enum EHealthSystemOrganization {
    PROVINCIAL_HOSPITAL = "PROVINCIAL_HOSPITAL",
    SANITARY_REGION = "SANITARY_REGION",
    UPA = "UPA",
    CPA = "CPA",
    POSTAS = "POSTAS",
    SIES = "SIES",
    VACCINATION_CENTER = "VACCINATION_CENTER",
    CETEC = "CETEC",
    MINISTRY_CENTER = "MINISTRY_CENTER",
    CAPS = "CAPS",
    OTHERS = "OTHERS",
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

export const enum EInstitutionReportPlace {
    POLICE_STATION = "POLICE_STATION",
    POLICE_STATION_WOMEN_OFFICE = "POLICE_STATION_WOMEN_OFFICE",
    PUBLIC_PROSECUTORS_OFFICE = "PUBLIC_PROSECUTORS_OFFICE",
    FAMILY_COURT = "FAMILY_COURT",
    PEACE_COURT = "PEACE_COURT",
    DIGITAL_SECURITY_REPORT = "DIGITAL_SECURITY_REPORT",
    OTHER = "OTHER",
}

export const enum EInstitutionReportReason {
    SERIOUS_EXTREMELY_INJURIES = "SERIOUS_EXTREMELY_INJURIES",
    AGAINST_CHILDHOOD_ADOLESCENCE = "AGAINST_CHILDHOOD_ADOLESCENCE",
    OTHER = "OTHER",
}

export const enum EIntermentIndicationStatus {
    YES = "YES",
    AS_PROTECTIVE_MEASURE = "AS_PROTECTIVE_MEASURE",
    NO = "NO",
}

export const enum EInternmentPlace {
    FLOOR = "FLOOR",
    INTENSIVE_CARE_UNIT = "INTENSIVE_CARE_UNIT",
}

export const enum EKeeperRelationship {
    MOTHER = "MOTHER",
    FATHER = "FATHER",
    GRANDPARENT = "GRANDPARENT",
    UNCLE_OR_AUNT = "UNCLE_OR_AUNT",
    BROTHER_OR_SISTER = "BROTHER_OR_SISTER",
    RELATED = "RELATED",
    OTHER = "OTHER",
}

export const enum ELicenseNumberType {
    NATIONAL = "NATIONAL",
    PROVINCE = "PROVINCE",
}

export const enum ELiveTogetherStatus {
    YES = "YES",
    SAME_SPACE = "SAME_SPACE",
    NO = "NO",
    NOT_NOW = "NOT_NOW",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum EMedicalCoverageTypeDto {
    PREPAGA = "PREPAGA",
    OBRASOCIAL = "OBRASOCIAL",
    ART = "ART",
}

export const enum EMunicipalGovernmentDevice {
    GENDER_DIVERSITY = "GENDER_DIVERSITY",
    LOCAL_COMMITTEE_AGAINST_VIOLENCE = "LOCAL_COMMITTEE_AGAINST_VIOLENCE",
    PROTECTION_CHILDREN_TEENS = "PROTECTION_CHILDREN_TEENS",
    DIRECTORATE_CHILDHOOD = "DIRECTORATE_CHILDHOOD",
    SOCIAL_DEVELOPMENT_AREA = "SOCIAL_DEVELOPMENT_AREA",
    PREVENTION_TREATMENT = "PREVENTION_TREATMENT",
    EDUCATIONAL_INSTITUTION = "EDUCATIONAL_INSTITUTION",
}

export const enum ENationalGovernmentDevice {
    WOMEN_GENDER_DIVERSITY_MINISTRY = "WOMEN_GENDER_DIVERSITY_MINISTRY",
    CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY = "CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY",
    SOCIAL_DEVELOPMENT_MINISTRY = "SOCIAL_DEVELOPMENT_MINISTRY",
    SEDRONAR = "SEDRONAR",
    ANSES = "ANSES",
    CIVIL_REGISTRY = "CIVIL_REGISTRY",
    EDUCATIONAL_INSTITUTION = "EDUCATIONAL_INSTITUTION",
    SECURITY_FORCES = "SECURITY_FORCES",
    JUSTICE_MINISTRY = "JUSTICE_MINISTRY",
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

export const enum EPregnancyTermination {
    VAGINAL = "VAGINAL",
    CESAREAN = "CESAREAN",
    UNDEFINED = "UNDEFINED",
}

export const enum EProfessionType {
    SURGEON = "SURGEON",
    SURGEON_ASSISTANT = "SURGEON_ASSISTANT",
    ANESTHESIOLOGIST = "ANESTHESIOLOGIST",
    CARDIOLOGIST = "CARDIOLOGIST",
    SURGICAL_INSTRUMENT_TECHNICIAN = "SURGICAL_INSTRUMENT_TECHNICIAN",
    OBSTETRICIAN = "OBSTETRICIAN",
    PEDIATRICIAN = "PEDIATRICIAN",
    PATHOLOGIST = "PATHOLOGIST",
    TRANSFUSIONIST = "TRANSFUSIONIST",
}

export const enum EProvincialGovernmentDevice {
    WOMEN_GENDER_DIVERSITY_MINISTRY = "WOMEN_GENDER_DIVERSITY_MINISTRY",
    PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE = "PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE",
    COMMUNITY_DEVELOPMENT_MINISTRY = "COMMUNITY_DEVELOPMENT_MINISTRY",
    EDUCATIONAL_INSTITUTION = "EDUCATIONAL_INSTITUTION",
    SECURITY_FORCES = "SECURITY_FORCES",
    JUDICIAL_SYSTEM = "JUDICIAL_SYSTEM",
    PAROLE_BOARD = "PAROLE_BOARD",
    JUVENILE_JUSTICE_INSTITUTION = "JUVENILE_JUSTICE_INSTITUTION",
    JUSTICE_MINISTRY = "JUSTICE_MINISTRY",
}

export const enum EReferenceAttentionState {
    PENDING = "PENDING",
    ASSIGNED = "ASSIGNED",
    SERVED = "SERVED",
    ABSENT = "ABSENT",
}

export const enum EReferenceForwardingType {
    REGIONAL = "REGIONAL",
    DOMAIN = "DOMAIN",
}

export const enum EReferenceRegulationState {
    WAITING_APPROVAL = "WAITING_APPROVAL",
    APPROVED = "APPROVED",
    REJECTED = "REJECTED",
    SUGGESTED_REVISION = "SUGGESTED_REVISION",
}

export const enum ERejectDocumentElectronicJointSignatureReason {
    WRONG_PROFESSIONAL = "WRONG_PROFESSIONAL",
    OTHER = "OTHER",
}

export const enum ERelationshipLength {
    UP_TO_SIX_MONTHS = "UP_TO_SIX_MONTHS",
    ONE_YEAR = "ONE_YEAR",
    MORE_THAN_ONE_YEAR = "MORE_THAN_ONE_YEAR",
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
    PERFIL_EPIDEMIO_MESO = "PERFIL_EPIDEMIO_MESO",
    PERFIL_EPIDEMIO_INSTITUCION = "PERFIL_EPIDEMIO_INSTITUCION",
    ADMINISTRATIVO_RED_DE_IMAGENES = "ADMINISTRATIVO_RED_DE_IMAGENES",
    PRESCRIPTOR = "PRESCRIPTOR",
    ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR = "ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR",
    AUDITOR_MPI = "AUDITOR_MPI",
    TECNICO = "TECNICO",
    PERSONAL_DE_LEGALES = "PERSONAL_DE_LEGALES",
    INFORMADOR = "INFORMADOR",
    API_FACTURACION = "API_FACTURACION",
    API_TURNOS = "API_TURNOS",
    API_PACIENTES = "API_PACIENTES",
    API_RECETAS = "API_RECETAS",
    API_SIPPLUS = "API_SIPPLUS",
    API_USERS = "API_USERS",
    VIRTUAL_CONSULTATION_PROFESSIONAL = "VIRTUAL_CONSULTATION_PROFESSIONAL",
    VIRTUAL_CONSULTATION_RESPONSIBLE = "VIRTUAL_CONSULTATION_RESPONSIBLE",
    API_IMAGENES = "API_IMAGENES",
    API_ORQUESTADOR = "API_ORQUESTADOR",
    ADMINISTRADOR_DE_ACCESO_DOMINIO = "ADMINISTRADOR_DE_ACCESO_DOMINIO",
    GESTOR_DE_ACCESO_DE_DOMINIO = "GESTOR_DE_ACCESO_DE_DOMINIO",
    GESTOR_DE_ACCESO_LOCAL = "GESTOR_DE_ACCESO_LOCAL",
    GESTOR_DE_ACCESO_REGIONAL = "GESTOR_DE_ACCESO_REGIONAL",
    ABORDAJE_VIOLENCIAS = "ABORDAJE_VIOLENCIAS",
    AUDITORIA_DE_ACCESO = "AUDITORIA_DE_ACCESO",
    GESTOR_CENTRO_LLAMADO = "GESTOR_CENTRO_LLAMADO",
    ADMINISTRADOR_DE_DATOS_PERSONALES = "ADMINISTRADOR_DE_DATOS_PERSONALES",
}

export const enum ESchoolLevel {
    NURSERY_SCHOOL = "NURSERY_SCHOOL",
    KINDERGARTEN = "KINDERGARTEN",
    ELEMENTARY_SCHOOL = "ELEMENTARY_SCHOOL",
    HIGH_SCHOOL = "HIGH_SCHOOL",
}

export const enum ESecurityForceType {
    EX_COMBATANT = "EX_COMBATANT",
    ARMED_FORCES = "ARMED_FORCES",
    FEDERAL_POLICE = "FEDERAL_POLICE",
    PROVINCIAL_POLICE = "PROVINCIAL_POLICE",
    PRIVATE_SECURITY = "PRIVATE_SECURITY",
    PENITENTIARY_SERVICE = "PENITENTIARY_SERVICE",
    OTHER = "OTHER",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum ESexualViolenceAction {
    STI_LABORATORY_PRESCRIPTION = "STI_LABORATORY_PRESCRIPTION",
    HIV_STI_HEPATITIS_PROPHYLAXIS = "HIV_STI_HEPATITIS_PROPHYLAXIS",
    EMERGENCY_CONTRACEPTION_PRESCRIPTION = "EMERGENCY_CONTRACEPTION_PRESCRIPTION",
    LEGAL_INTERRUPTION_PREGNANCY_COUNSELING = "LEGAL_INTERRUPTION_PREGNANCY_COUNSELING",
}

export const enum ESignatureStatus {
    CANNOT_BE_SIGNED = "CANNOT_BE_SIGNED",
    PENDING = "PENDING",
    IN_PROGRESS = "IN_PROGRESS",
    SIGNED = "SIGNED",
}

export const enum ESurfacePositionDto {
    INTERNAL = "INTERNAL",
    EXTERNAL = "EXTERNAL",
    LEFT = "LEFT",
    RIGHT = "RIGHT",
    CENTRAL = "CENTRAL",
}

export const enum ETerminologyKind {
    SYNONYM = "SYNONYM",
    TERMINOLOGY = "TERMINOLOGY",
}

export const enum EVictimKeeperReportPlace {
    POLICE_STATION = "POLICE_STATION",
    POLICE_STATION_WOMEN_OFFICE = "POLICE_STATION_WOMEN_OFFICE",
    PUBLIC_PROSECUTORS_OFFICE = "PUBLIC_PROSECUTORS_OFFICE",
    FAMILY_COURT = "FAMILY_COURT",
    PEACE_COURT = "PEACE_COURT",
}

export const enum EViolenceEvaluationRiskLevel {
    LOW = "LOW",
    MEDIUM = "MEDIUM",
    HIGH = "HIGH",
}

export const enum EViolenceFrequency {
    FIRST_TIME = "FIRST_TIME",
    SOMETIMES = "SOMETIMES",
    FREQUENT = "FREQUENT",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum EViolenceTowardsUnderageType {
    DIRECT_VIOLENCE = "DIRECT_VIOLENCE",
    INDIRECT_VIOLENCE = "INDIRECT_VIOLENCE",
    NO_VIOLENCE = "NO_VIOLENCE",
    NO_INFORMATION = "NO_INFORMATION",
}

export const enum EVirtualConsultationEvent {
    INCOMING_CALL = "INCOMING_CALL",
    CALL_CANCELED = "CALL_CANCELED",
    CALL_REJECTED = "CALL_REJECTED",
    CALL_ACCEPTED = "CALL_ACCEPTED",
}

export const enum EVirtualConsultationPriority {
    LOW = "LOW",
    MEDIUM = "MEDIUM",
    HIGH = "HIGH",
}

export const enum EVirtualConsultationStatus {
    PENDING = "PENDING",
    IN_PROGRESS = "IN_PROGRESS",
    FINISHED = "FINISHED",
    CANCELED = "CANCELED",
}

export const enum ProblemTypeEnum {
    DIAGNOSIS = "DIAGNOSIS",
    PROBLEM = "PROBLEM",
    FAMILY_HISTORY = "FAMILY_HISTORY",
    CHRONIC = "CHRONIC",
    OTHER = "OTHER",
    PERSONAL_HISTORY = "PERSONAL_HISTORY",
    POSTOPERATIVE_DIAGNOSIS = "POSTOPERATIVE_DIAGNOSIS",
    PREOPERATIVE_DIAGNOSIS = "PREOPERATIVE_DIAGNOSIS",
    OTHER_HISTORY = "OTHER_HISTORY",
}

export const enum ProcedureTypeEnum {
    PROCEDURE = "PROCEDURE",
    SURGICAL_PROCEDURE = "SURGICAL_PROCEDURE",
    ANESTHESIA_PROCEDURE = "ANESTHESIA_PROCEDURE",
    CULTURE = "CULTURE",
    FROZEN_SECTION_BIOPSY = "FROZEN_SECTION_BIOPSY",
    DRAINAGE = "DRAINAGE",
}

export const enum SnomedECL {
    ALLERGY = "ALLERGY",
    ANESTHESIA = "ANESTHESIA",
    BLOOD_TYPE = "BLOOD_TYPE",
    CARDIOVASCULAR_DISORDER = "CARDIOVASCULAR_DISORDER",
    CONSULTATION_REASON = "CONSULTATION_REASON",
    DIABETES = "DIABETES",
    DIAGNOSIS = "DIAGNOSIS",
    ELECTROCARDIOGRAPHIC_PROCEDURE = "ELECTROCARDIOGRAPHIC_PROCEDURE",
    EVENT = "EVENT",
    FAMILY_RECORD = "FAMILY_RECORD",
    HOSPITAL_REASON = "HOSPITAL_REASON",
    HYPERTENSION = "HYPERTENSION",
    MEDICINE_WITH_UNIT_OF_PRESENTATION = "MEDICINE_WITH_UNIT_OF_PRESENTATION",
    MEDICINE = "MEDICINE",
    PERSONAL_RECORD = "PERSONAL_RECORD",
    PROCEDURE = "PROCEDURE",
    VACCINE = "VACCINE",
    VIOLENCE_MODALITY = "VIOLENCE_MODALITY",
    VIOLENCE_PROBLEM = "VIOLENCE_PROBLEM",
    VIOLENCE_TYPE = "VIOLENCE_TYPE",
}
