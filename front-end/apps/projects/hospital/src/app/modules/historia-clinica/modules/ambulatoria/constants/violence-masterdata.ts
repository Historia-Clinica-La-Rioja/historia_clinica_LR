import { ESecurityForceType, EAggressorRelationship, ELiveTogetherStatus, ERelationshipLength, EViolenceFrequency, ECriminalRecordStatus, EDisabilityCertificateStatus, EKeeperRelationship, EViolenceEvaluationRiskLevel, ESchoolLevel, EViolenceTowardsUnderageType, EHealthInstitutionOrganization, EHealthSystemOrganization, EIntermentIndicationStatus, EMunicipalGovernmentDevice, EProvincialGovernmentDevice, ENationalGovernmentDevice, EInstitutionReportPlace, EInstitutionReportReason, ESexualViolenceAction, EVictimKeeperReportPlace } from "@api-rest/api-model";

enum AreaDescription {
    PROVINCIAL_HOSPITAL = 'Hospital provincial',
    HEALTH_REGION = 'Sede región sanitaria',
    UPA = 'UPA',
    CPA = 'CPA',
    POSTAS = 'Postas',
    SIES = 'SIES',
    VACCINATION = 'Vacunatorio',
    CETEC = 'CETEC',
    MINISTRY_HEADQUARTERS = 'Sede Central del Ministerio',
    CAPS = 'CAPS',
    OTHER = 'Otros'
}

enum EstablishmentsDescription {
    CLINIC = 'Clínica médica',
    PEDIATRIC = 'Pediatría',
    GYNECOLOGIC_OBSTRETRIC = 'Ginecología/Obstetricia',
    SOCIAL_WORK = 'Trabajo social',
    MENTAL_HEALTH = 'Salud mental',
    NURSING = 'Enfermería',
    SAPS = 'SAPS',
    EDA = 'EDA',
    EMERGENCY_CARE = 'Guardia',
    AGAINST_VIOLENCE = 'Comité contras las violencias',
    ADDRESS = 'Dirección',
    OTHER = 'Otros'
}

export enum Devices {
    MUNICIPAL_DEVICES = 'Dispositivos estatales municipales',
    PROVINCIAL_DEVICES = 'Dispositivos estatales provinciales',
    NATIONAL_DEVICES = 'Dispositivos estatales nacionales',
    SOCIAL_ORGANIZATION = 'Organizaciones sociales y de la sociedad civil'
}

enum MunicipalDevicesDescription {
    GENDER_DIVERSITY = 'Área de Género y diversidad',
    AGAINST_VIOLENCE = 'Mesa local contra las Violencias',
    PROTECTION = 'Servicio Local de Promoción y Protección de Derechos de NNyA',
    CHILDHOOD_ADOLESCENCE_AREA = 'Dirección/área de Niñez y Adolescencia',
    SOCIAL_DEVELOPMENT_AREA = 'Área de Desarrollo Social',
    ADDICTION_PREVENTION_AREA = 'Dirección/área de prevención y atención de adicciones',
    EDUCATIONAL_INSTITUTIONS = 'Instituciones educativas'
}

enum ProvincialDevicesDescription {
    WOMAN_MINISTERY_GENDER_POLITICS_SEXUAL_DIVERSITY = 'Ministerio de Mujeres, Políticas de Género y Diversidad Sexual',
    RIGHT_PROTECION = 'Servicio Zonal de Promoción y Protección de Derechos de NNyA',
    COMMUNITY_RIGHTS = 'Ministerio de Desarrollo de la Comunidad',
    EDUCATIONAL_INSTITUTIONS = 'Instituciones educativas',
    SECURITY_FORCES = 'Fuerzas de seguridad/Comisarías',
    JUDICAL_SYSTEM = 'Sistema judicial/juzgados de paz y de familia',
    PATRONAGE = 'Patronato de liberados',
    JUVENILE_PRISON = 'Instituciones del sistema de responsabilidad penal juvenil',
    JUSTICE_RIGHTS = 'Ministerio de Justicia y Derechos Humanos'
}

enum NationalDevicesDescription {
    WOMAN_MINISTERY_GENDER_DIVERSITY = 'Ministerio de Mujeres, Géneros y Diversidad',
    SECRETARY = 'Secretaría Nacional de Niñez, Adolescencia y Familia',
    SOCIAL_DEVELOPMENT = 'Ministerio de Desarrollo Social',
    SEDRONAR = 'SEDRONAR',
    ANSES = 'ANSES',
    PERSON_REGISTRY = 'Registro de las personas',
    EDUCATIONAL_INSTITUTIONS = 'Instituciones educativas',
    SECURITY_FORCES = 'Fuerzas de seguridad',
    JUSTICE_RIGHTS = 'Ministerio de Justicia y Derechos Humanos'
}

enum OrganizationsDescription {
    POLICE_STATION = 'Comisaría',
    WOMEN_OFFICE_POLICE_STATION = 'Comisaría/Oficina de la Mujer',
    PROSECUTOR = 'Fiscalía',
    FAMILY_COURT = 'Juzgado de la Familia',
    PEACE_COURT = 'Juzgado de Paz'
}

enum ComplaintsDescription {
    INJURIES = 'Lesiones graves o gravísimas a personas adultas',
    VIOLENCE = 'Violencias contra niñeces y adolescencias',
    OTHER = 'Otro'
}

enum OrganizationsExtendedDescription {
    COMPLAINT = 'Denuncia medio digital de seguridad pasa a fiscalía',
    OTHER = 'Otro'
}

enum ImplementedActionsDescription {
    ITS = 'Indicación de estudios de laboratorio para determinar ITS',
    VIH_ITS_HEPATITIS = 'Profilaxis para VIH, ITS y Hepatitis virales frente la exposición a fluidos potencialmente infecciosos',
    EMERGENCY = 'Indicación de anticoncepción de emergencia',
    ILE = 'Consejería de acceso a Interrupción Legal de Embarazo (ILE) frente a confirmación de embarazo producto de violación.'
}

enum ViolenceTypeDescription {
    DIRECT = 'Sí, es violencia directa contra NNyA',
    INDIRECT = 'Sí, es una violencia indirecta contra NNyA',
}

enum EscolarizationLevelDescription {
    MATERNAL = 'Maternal',
    INITIAL = 'Inicial',
    PRIMARY = 'Primario',
    SECONDARY = 'Secundario'
}

enum RiskLevelDescription {
    LOW = 'Bajo',
    MEDIUM = 'Medio',
    HIGH = 'Alto'
}

enum RelationOptionDescription {
    MOTHER = 'Madre',
    FATHER = 'Padre',
    GRANDFATHERMOTHER = 'Abuelo/a',
    AUNT = 'Tío/a',
    SIBLING = 'Hermano/a',
    REFERRER = 'Referente',
    OTHER = 'Otro'
}

export interface BasicOption {
    text: string,
    value: boolean
}
export interface ValueOption {
    text: string,
    value: string
}

export enum FormOption {
    YES = 'Sí',
    NO = 'No',
    IN_PROCESS = "En trámite",
    WITHOUT_DATA = 'Sin información',
}
export const BasicTwoOptions: BasicOption[] = [
    {
        text: FormOption.YES,
        value: true
    },
    {
        text: FormOption.NO,
        value: false
    },
];
 
export const ImplementedActions : ValueOption[] = [
    {
        text: ImplementedActionsDescription.ITS,
        value: ESexualViolenceAction.STI_LABORATORY_PRESCRIPTION
    },
    {
        text: ImplementedActionsDescription.VIH_ITS_HEPATITIS,
        value: ESexualViolenceAction.HIV_STI_HEPATITIS_PROPHYLAXIS
    },
    {
        text: ImplementedActionsDescription.EMERGENCY,
        value: ESexualViolenceAction.EMERGENCY_CONTRACEPTION_PRESCRIPTION
    },
    {
        text: ImplementedActionsDescription.ILE,
        value: ESexualViolenceAction.LEGAL_INTERRUPTION_PREGNANCY_COUNSELING
    }
]

export const Complaints: ValueOption[] = [
    {
        text: ComplaintsDescription.INJURIES,
        value: EInstitutionReportReason.SERIOUS_EXTREMELY_INJURIES
    },
    {
        text: ComplaintsDescription.VIOLENCE,
        value: EInstitutionReportReason.AGAINST_CHILDHOOD_ADOLESCENCE
    },
    {
        text: ComplaintsDescription.OTHER,
        value: EInstitutionReportReason.OTHER
    },
]

export const Organizations: ValueOption[] = [
    {
        text: OrganizationsDescription.POLICE_STATION,
        value: EVictimKeeperReportPlace.POLICE_STATION
    },
    {
        text: OrganizationsDescription.WOMEN_OFFICE_POLICE_STATION,
        value: EVictimKeeperReportPlace.POLICE_STATION_WOMEN_OFFICE
    },
    {
        text: OrganizationsDescription.PROSECUTOR,
        value: EVictimKeeperReportPlace.PUBLIC_PROSECUTORS_OFFICE
    },
    {
        text: OrganizationsDescription.FAMILY_COURT,
        value: EVictimKeeperReportPlace.FAMILY_COURT
    },
    {
        text: OrganizationsDescription.PEACE_COURT,
        value: EVictimKeeperReportPlace.PEACE_COURT
    },
]

export const OrganizationsExtended: ValueOption[] = [
    {
        text: OrganizationsDescription.POLICE_STATION,
        value: EInstitutionReportPlace.POLICE_STATION
    },
    {
        text: OrganizationsDescription.WOMEN_OFFICE_POLICE_STATION,
        value: EInstitutionReportPlace.POLICE_STATION_WOMEN_OFFICE
    },
    {
        text: OrganizationsDescription.PROSECUTOR,
        value: EInstitutionReportPlace.PUBLIC_PROSECUTORS_OFFICE
    },
    {
        text: OrganizationsDescription.FAMILY_COURT,
        value: EInstitutionReportPlace.FAMILY_COURT
    },
    {
        text: OrganizationsDescription.PEACE_COURT,
        value: EInstitutionReportPlace.PEACE_COURT
    },
    {
        text: OrganizationsExtendedDescription.COMPLAINT,
        value: EInstitutionReportPlace.DIGITAL_SECURITY_REPORT
    },
    {
        text: OrganizationsExtendedDescription.OTHER,
        value: EInstitutionReportPlace.OTHER
    },
]

export const NationalDevices: ValueOption[] = [
    {
        text: NationalDevicesDescription.WOMAN_MINISTERY_GENDER_DIVERSITY,
        value: ENationalGovernmentDevice.WOMEN_GENDER_DIVERSITY_MINISTRY
    },
    {
        text: NationalDevicesDescription.SECRETARY,
        value: ENationalGovernmentDevice.CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY
    },
    {
        text: NationalDevicesDescription.SOCIAL_DEVELOPMENT,
        value: ENationalGovernmentDevice.SOCIAL_DEVELOPMENT_MINISTRY
    },
    {
        text: NationalDevicesDescription.SEDRONAR,
        value: ENationalGovernmentDevice.SEDRONAR
    },
    {
        text: NationalDevicesDescription.ANSES,
        value: ENationalGovernmentDevice.ANSES
    },
    {
        text: NationalDevicesDescription.PERSON_REGISTRY,
        value: ENationalGovernmentDevice.CIVIL_REGISTRY
    },
    {
        text: NationalDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: ENationalGovernmentDevice.EDUCATIONAL_INSTITUTION
    },
    {
        text: NationalDevicesDescription.SECURITY_FORCES,
        value: ENationalGovernmentDevice.SECURITY_FORCES
    },
    {
        text: NationalDevicesDescription.JUSTICE_RIGHTS,
        value: ENationalGovernmentDevice.JUSTICE_MINISTRY
    },
]

export const ProvincialDevices: ValueOption[] = [
    {
        text: ProvincialDevicesDescription.WOMAN_MINISTERY_GENDER_POLITICS_SEXUAL_DIVERSITY,
        value: EProvincialGovernmentDevice.WOMEN_GENDER_DIVERSITY_MINISTRY
    },
    {
        text: ProvincialDevicesDescription.RIGHT_PROTECION,
        value: EProvincialGovernmentDevice.PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE
    },
    {
        text: ProvincialDevicesDescription.COMMUNITY_RIGHTS,
        value: EProvincialGovernmentDevice.COMMUNITY_DEVELOPMENT_MINISTRY
    },
    {
        text: ProvincialDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: EProvincialGovernmentDevice.EDUCATIONAL_INSTITUTION
    },
    {
        text: ProvincialDevicesDescription.SECURITY_FORCES,
        value: EProvincialGovernmentDevice.SECURITY_FORCES
    },
    {
        text: ProvincialDevicesDescription.JUDICAL_SYSTEM,
        value: EProvincialGovernmentDevice.JUDICIAL_SYSTEM
    },
    {
        text: ProvincialDevicesDescription.PATRONAGE,
        value: EProvincialGovernmentDevice.PAROLE_BOARD
    },
    {
        text: ProvincialDevicesDescription.JUVENILE_PRISON,
        value: EProvincialGovernmentDevice.JUVENILE_JUSTICE_INSTITUTION
    },
    {
        text: ProvincialDevicesDescription.JUSTICE_RIGHTS,
        value: EProvincialGovernmentDevice.JUSTICE_MINISTRY
    },
]

export const MunicipalDevices: ValueOption[] = [
    {
        text: MunicipalDevicesDescription.GENDER_DIVERSITY,
        value: EMunicipalGovernmentDevice.GENDER_DIVERSITY
    },
    {
        text: MunicipalDevicesDescription.AGAINST_VIOLENCE,
        value: EMunicipalGovernmentDevice.LOCAL_COMMITTEE_AGAINST_VIOLENCE
    },
    {
        text: MunicipalDevicesDescription.PROTECTION,
        value: EMunicipalGovernmentDevice.PROTECTION_CHILDREN_TEENS
    },
    {
        text: MunicipalDevicesDescription.CHILDHOOD_ADOLESCENCE_AREA,
        value: EMunicipalGovernmentDevice.DIRECTORATE_CHILDHOOD
    },
    {
        text: MunicipalDevicesDescription.SOCIAL_DEVELOPMENT_AREA,
        value: EMunicipalGovernmentDevice.SOCIAL_DEVELOPMENT_AREA
    },
    {
        text: MunicipalDevicesDescription.ADDICTION_PREVENTION_AREA,
        value: EMunicipalGovernmentDevice.PREVENTION_TREATMENT
    },
    {
        text: MunicipalDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: EMunicipalGovernmentDevice.EDUCATIONAL_INSTITUTION
    },
]

export const InternmentIndication: ValueOption[] = [
    {
        text: FormOption.YES,
        value: EIntermentIndicationStatus.YES
    },
    {
        text: FormOption.NO,
        value: EIntermentIndicationStatus.NO
    },
    {
        text: 'Sí, como medida de resguardo',
        value: EIntermentIndicationStatus.AS_PROTECTIVE_MEASURE
    },
]

export const Establishments: ValueOption[] = [
    {
        text: EstablishmentsDescription.ADDRESS,
        value: EHealthInstitutionOrganization.MANAGEMENT
    },
    {
        text: EstablishmentsDescription.AGAINST_VIOLENCE,
        value: EHealthInstitutionOrganization.COMMITTEE
    },
    {
        text: EstablishmentsDescription.CLINIC,
        value: EHealthInstitutionOrganization.MEDICAL_CLINIC
    },
    {
        text: EstablishmentsDescription.EDA,
        value: EHealthInstitutionOrganization.EDA
    },
    {
        text: EstablishmentsDescription.EMERGENCY_CARE,
        value: EHealthInstitutionOrganization.EMERGENCY_CARE
    },
    {
        text: EstablishmentsDescription.GYNECOLOGIC_OBSTRETRIC,
        value: EHealthInstitutionOrganization.GYNECOLOGY_OBSTETRICS
    },
    {
        text: EstablishmentsDescription.MENTAL_HEALTH,
        value: EHealthInstitutionOrganization.MENTAL_HEALTH
    },
    {
        text: EstablishmentsDescription.NURSING,
        value: EHealthInstitutionOrganization.NURSING
    },
    {
        text: EstablishmentsDescription.OTHER,
        value: EHealthInstitutionOrganization.OTHERS
    },
    {
        text: EstablishmentsDescription.PEDIATRIC,
        value: EHealthInstitutionOrganization.PEDIATRICS
    },
    {
        text: EstablishmentsDescription.SAPS,
        value: EHealthInstitutionOrganization.SAPS
    },
    {
        text: EstablishmentsDescription.SOCIAL_WORK,
        value: EHealthInstitutionOrganization.SOCIAL_WORK
    },
]

export const Areas: ValueOption[] = [
    {
        text: AreaDescription.CAPS,
        value: EHealthSystemOrganization.CAPS
    },
    {
        text: AreaDescription.CETEC,
        value: EHealthSystemOrganization.CETEC
    },
    {
        text: AreaDescription.CPA,
        value: EHealthSystemOrganization.CPA
    },
    {
        text: AreaDescription.HEALTH_REGION,
        value: EHealthSystemOrganization.SANITARY_REGION
    },
    {
        text: AreaDescription.MINISTRY_HEADQUARTERS,
        value: EHealthSystemOrganization.MINISTRY_CENTER
    },
    {
        text: AreaDescription.OTHER,
        value: EHealthSystemOrganization.OTHERS
    },
    {
        text: AreaDescription.POSTAS,
        value: EHealthSystemOrganization.POSTAS
    },
    {
        text: AreaDescription.PROVINCIAL_HOSPITAL,
        value: EHealthSystemOrganization.PROVINCIAL_HOSPITAL
    },
    {
        text: AreaDescription.SIES,
        value: EHealthSystemOrganization.SIES
    },
    {
        text: AreaDescription.UPA,
        value: EHealthSystemOrganization.UPA
    },
    {
        text: AreaDescription.VACCINATION,
        value: EHealthSystemOrganization.VACCINATION_CENTER
    },
]

export const BasicOptions: BasicOption[] = [
    {
        text: FormOption.YES,
        value: true
    },
    {
        text: FormOption.NO,
        value: false
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: null
    }
]

export const ViolenceTypes: ValueOption[] = [
    {
        text: ViolenceTypeDescription.DIRECT,
        value: EViolenceTowardsUnderageType.DIRECT_VIOLENCE
    },
    {
        text: ViolenceTypeDescription.INDIRECT,
        value: EViolenceTowardsUnderageType.INDIRECT_VIOLENCE
    },
    {
        text: FormOption.NO,
        value: EViolenceTowardsUnderageType.NO_VIOLENCE
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: EViolenceTowardsUnderageType.NO_INFORMATION
    },
]

export const EscolarizationLevels: ValueOption[] = [
    {
        text: EscolarizationLevelDescription.INITIAL,
        value: ESchoolLevel.NURSERY_SCHOOL
    },
    {
        text: EscolarizationLevelDescription.MATERNAL,
        value: ESchoolLevel.KINDERGARTEN
    },
    {
        text: EscolarizationLevelDescription.PRIMARY,
        value: ESchoolLevel.ELEMENTARY_SCHOOL
    }
    , {
        text: EscolarizationLevelDescription.SECONDARY,
        value: ESchoolLevel.HIGH_SCHOOL
    },
]

export const RiskLevels: ValueOption[] = [
    {
        text: RiskLevelDescription.HIGH,
        value: EViolenceEvaluationRiskLevel.HIGH
    },
    {
        text: RiskLevelDescription.LOW,
        value: EViolenceEvaluationRiskLevel.LOW
    },
    {
        text: RiskLevelDescription.MEDIUM,
        value: EViolenceEvaluationRiskLevel.MEDIUM
    }
]

export const StateOptions: BasicOption[] = [
    {
        text: 'Activo',
        value: true,
    },
    {
        text: 'Retirado',
        value: false,
    }
]

export const InstitutionOptions: any[] = [
    {
        text: 'Excombatiente',
        value: ESecurityForceType.EX_COMBATANT,
    },
    {
        text: 'Fuerzas armadas',
        value: ESecurityForceType.ARMED_FORCES,
    },
    {
        text: 'Policía Federal',
        value: ESecurityForceType.FEDERAL_POLICE,
    },
    {
        text: 'Policía Provincial',
        value: ESecurityForceType.PROVINCIAL_POLICE,
    },
    {
        text: 'Seguridad Privada',
        value: ESecurityForceType.PRIVATE_SECURITY,
    },
    {
        text: 'Servicio Penitenciario',
        value: ESecurityForceType.PENITENTIARY_SERVICE,
    },
    {
        text: 'Otras',
        value: ESecurityForceType.OTHER,
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: ESecurityForceType.ARMED_FORCES,
    }
]

export const AggressorRelationship: ValueOption[] = [
    {
        text: 'Pareja/novio/a',
        value: EAggressorRelationship.PARTNER,
    },
    {
        text: 'Ex pareja',
        value: EAggressorRelationship.EX_PARTNER,
    },
    {
        text: 'Padre',
        value: EAggressorRelationship.FATHER,
    },
    {
        text: 'Progenitor afín (Padrastro)',
        value: EAggressorRelationship.STEPFATHER,
    },
    {
        text: 'Madre',
        value: EAggressorRelationship.MOTHER,
    },
    {
        text: 'Progenitora afín (Madrastra)',
        value: EAggressorRelationship.STEPMOTHER,
    },
    {
        text: 'Hijo',
        value: EAggressorRelationship.SON,
    },
    {
        text: 'Hija',
        value: EAggressorRelationship.DAUGHTER,
    },
    {
        text: 'Hermano/a',
        value: EAggressorRelationship.SIBLING,
    },
    {
        text: 'Superior jerárquico',
        value: EAggressorRelationship.SUPERIOR,
    },
    {
        text: 'Otra/o conocida/o',
        value: EAggressorRelationship.ACQUAINTANCE,
    },
    {
        text: 'Sin vinculo',
        value: EAggressorRelationship.NO_RELATIONSHIP,
    },
    {
        text: 'Sin información',
        value: EAggressorRelationship.NO_INFORMATION,
    },
    {
        text: 'No contesta',
        value: EAggressorRelationship.DOES_NOT_ANSWER,
    },
]
export const LiveTogetherStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: ELiveTogetherStatus.YES
    },
    {
        text: "Si, otra casa mismo terreno",
        value: ELiveTogetherStatus.SAME_SPACE,
    },
    {
        text: FormOption.NO,
        value: ELiveTogetherStatus.NO
    },
    {
        text: "No convive, pero convivio",
        value: ELiveTogetherStatus.NOT_NOW
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: ELiveTogetherStatus.NO_INFORMATION
    }
]
export const RelationshipLengths: ValueOption[] = [
    {
        text: "Hasta 6 meses",
        value: ERelationshipLength.UP_TO_SIX_MONTHS
    },
    {
        text: "Hasta 1 año",
        value: ERelationshipLength.ONE_YEAR
    },
    {
        text: "Mas de 1 año",
        value: ERelationshipLength.MORE_THAN_ONE_YEAR
    }
]
export const ViolenceFrequencys: ValueOption[] = [
    {
        text: "Primera vez",
        value: EViolenceFrequency.FIRST_TIME,
    },
    {
        text: "Alguna vez anterior",
        value: EViolenceFrequency.SOMETIMES,
    },
    {
        text: "Con frecuencia",
        value: EViolenceFrequency.FREQUENT,
    }, {
        text: "Sin información",
        value: EViolenceFrequency.NO_INFORMATION,
    }
]
export const CriminalRecordStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: ECriminalRecordStatus.YES,
    },
    {
        text: "Si con otras personas",
        value: ECriminalRecordStatus.WITH_OTHER_PEOPLE,
    },
    {
        text: FormOption.NO,
        value: ECriminalRecordStatus.NO,
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: ECriminalRecordStatus.NO_INFORMATION,
    }
]

export const DisabilityCertificateStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: EDisabilityCertificateStatus.HAS_CERTIFICATE,
    },
    {
        text: FormOption.NO,
        value: EDisabilityCertificateStatus.HAS_NOT_CERTIFICATE,
    },
    {
        text: FormOption.IN_PROCESS,
        value: EDisabilityCertificateStatus.PENDING,
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: EDisabilityCertificateStatus.NO_INFORMATION
    }
]

export const Sectors: BasicOption[] = [
    {
        text: "Formal",
        value: true
    },
    {
        text: "Informal",
        value: false,
    }
]

export const RelationOption: ValueOption[] = [
    {
        text: RelationOptionDescription.SIBLING,
        value: EKeeperRelationship.BROTHER_OR_SISTER
    },
    {
        text: RelationOptionDescription.FATHER,
        value: EKeeperRelationship.FATHER
    },
    {
        text: RelationOptionDescription.GRANDFATHERMOTHER,
        value: EKeeperRelationship.GRANDPARENT
    },
    {
        text: RelationOptionDescription.MOTHER,
        value: EKeeperRelationship.MOTHER
    },
    {
        text: RelationOptionDescription.OTHER,
        value: EKeeperRelationship.OTHER
    },
    {
        text: RelationOptionDescription.REFERRER,
        value: EKeeperRelationship.RELATED
    },
    {
        text: RelationOptionDescription.AUNT,
        value: EKeeperRelationship.UNCLE_OR_AUNT
    }
]
