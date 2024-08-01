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

export enum RelationOptionDescription {
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
    value: string,
    checked?: boolean
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
        value: ESexualViolenceAction.STI_LABORATORY_PRESCRIPTION,
        checked: false
    },
    {
        text: ImplementedActionsDescription.VIH_ITS_HEPATITIS,
        value: ESexualViolenceAction.HIV_STI_HEPATITIS_PROPHYLAXIS,
        checked: false
    },
    {
        text: ImplementedActionsDescription.EMERGENCY,
        value: ESexualViolenceAction.EMERGENCY_CONTRACEPTION_PRESCRIPTION,
        checked: false
    },
    {
        text: ImplementedActionsDescription.ILE,
        value: ESexualViolenceAction.LEGAL_INTERRUPTION_PREGNANCY_COUNSELING,
        checked: false
    }
]

export const Complaints: ValueOption[] = [
    {
        text: ComplaintsDescription.INJURIES,
        value: EInstitutionReportReason.SERIOUS_EXTREMELY_INJURIES,
        checked: false
    },
    {
        text: ComplaintsDescription.VIOLENCE,
        value: EInstitutionReportReason.AGAINST_CHILDHOOD_ADOLESCENCE,
        checked: false
    },
    {
        text: ComplaintsDescription.OTHER,
        value: EInstitutionReportReason.OTHER,
        checked: false
    },
]

export const Organizations: ValueOption[] = [
    {
        text: OrganizationsDescription.POLICE_STATION,
        value: EVictimKeeperReportPlace.POLICE_STATION,
        checked: false
    },
    {
        text: OrganizationsDescription.WOMEN_OFFICE_POLICE_STATION,
        value: EVictimKeeperReportPlace.POLICE_STATION_WOMEN_OFFICE,
        checked: false
    },
    {
        text: OrganizationsDescription.PROSECUTOR,
        value: EVictimKeeperReportPlace.PUBLIC_PROSECUTORS_OFFICE,
        checked: false
    },
    {
        text: OrganizationsDescription.FAMILY_COURT,
        value: EVictimKeeperReportPlace.FAMILY_COURT,
        checked: false
    },
    {
        text: OrganizationsDescription.PEACE_COURT,
        value: EVictimKeeperReportPlace.PEACE_COURT,
        checked: false
    },
]

export const OrganizationsExtended: ValueOption[] = [
    {
        text: OrganizationsDescription.POLICE_STATION,
        value: EInstitutionReportPlace.POLICE_STATION,
        checked: false
    },
    {
        text: OrganizationsDescription.WOMEN_OFFICE_POLICE_STATION,
        value: EInstitutionReportPlace.POLICE_STATION_WOMEN_OFFICE,
        checked: false
    },
    {
        text: OrganizationsDescription.PROSECUTOR,
        value: EInstitutionReportPlace.PUBLIC_PROSECUTORS_OFFICE,
        checked: false
    },
    {
        text: OrganizationsDescription.FAMILY_COURT,
        value: EInstitutionReportPlace.FAMILY_COURT,
        checked: false
    },
    {
        text: OrganizationsDescription.PEACE_COURT,
        value: EInstitutionReportPlace.PEACE_COURT,
        checked: false
    },
    {
        text: OrganizationsExtendedDescription.COMPLAINT,
        value: EInstitutionReportPlace.DIGITAL_SECURITY_REPORT,
        checked: false
    },
    {
        text: OrganizationsExtendedDescription.OTHER,
        value: EInstitutionReportPlace.OTHER,
        checked: false
    },
]

export const NationalDevices: ValueOption[] = [
    {
        text: NationalDevicesDescription.WOMAN_MINISTERY_GENDER_DIVERSITY,
        value: ENationalGovernmentDevice.WOMEN_GENDER_DIVERSITY_MINISTRY,
        checked: false
    },
    {
        text: NationalDevicesDescription.SECRETARY,
        value: ENationalGovernmentDevice.CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY,
        checked: false
    },
    {
        text: NationalDevicesDescription.SOCIAL_DEVELOPMENT,
        value: ENationalGovernmentDevice.SOCIAL_DEVELOPMENT_MINISTRY,
        checked: false
    },
    {
        text: NationalDevicesDescription.SEDRONAR,
        value: ENationalGovernmentDevice.SEDRONAR,
        checked: false
    },
    {
        text: NationalDevicesDescription.ANSES,
        value: ENationalGovernmentDevice.ANSES,
        checked: false
    },
    {
        text: NationalDevicesDescription.PERSON_REGISTRY,
        value: ENationalGovernmentDevice.CIVIL_REGISTRY,
        checked: false
    },
    {
        text: NationalDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: ENationalGovernmentDevice.EDUCATIONAL_INSTITUTION,
        checked: false
    },
    {
        text: NationalDevicesDescription.SECURITY_FORCES,
        value: ENationalGovernmentDevice.SECURITY_FORCES,
        checked: false
    },
    {
        text: NationalDevicesDescription.JUSTICE_RIGHTS,
        value: ENationalGovernmentDevice.JUSTICE_MINISTRY,
        checked: false
    },
]

export const ProvincialDevices: ValueOption[] = [
    {
        text: ProvincialDevicesDescription.WOMAN_MINISTERY_GENDER_POLITICS_SEXUAL_DIVERSITY,
        value: EProvincialGovernmentDevice.WOMEN_GENDER_DIVERSITY_MINISTRY,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.RIGHT_PROTECION,
        value: EProvincialGovernmentDevice.PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.COMMUNITY_RIGHTS,
        value: EProvincialGovernmentDevice.COMMUNITY_DEVELOPMENT_MINISTRY,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: EProvincialGovernmentDevice.EDUCATIONAL_INSTITUTION,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.SECURITY_FORCES,
        value: EProvincialGovernmentDevice.SECURITY_FORCES,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.JUDICAL_SYSTEM,
        value: EProvincialGovernmentDevice.JUDICIAL_SYSTEM,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.PATRONAGE,
        value: EProvincialGovernmentDevice.PAROLE_BOARD,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.JUVENILE_PRISON,
        value: EProvincialGovernmentDevice.JUVENILE_JUSTICE_INSTITUTION,
        checked: false
    },
    {
        text: ProvincialDevicesDescription.JUSTICE_RIGHTS,
        value: EProvincialGovernmentDevice.JUSTICE_MINISTRY,
        checked: false
    },
]

export const MunicipalDevices: ValueOption[] = [
    {
        text: MunicipalDevicesDescription.GENDER_DIVERSITY,
        value: EMunicipalGovernmentDevice.GENDER_DIVERSITY,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.AGAINST_VIOLENCE,
        value: EMunicipalGovernmentDevice.LOCAL_COMMITTEE_AGAINST_VIOLENCE,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.PROTECTION,
        value: EMunicipalGovernmentDevice.PROTECTION_CHILDREN_TEENS,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.CHILDHOOD_ADOLESCENCE_AREA,
        value: EMunicipalGovernmentDevice.DIRECTORATE_CHILDHOOD,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.SOCIAL_DEVELOPMENT_AREA,
        value: EMunicipalGovernmentDevice.SOCIAL_DEVELOPMENT_AREA,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.ADDICTION_PREVENTION_AREA,
        value: EMunicipalGovernmentDevice.PREVENTION_TREATMENT,
        checked: false
    },
    {
        text: MunicipalDevicesDescription.EDUCATIONAL_INSTITUTIONS,
        value: EMunicipalGovernmentDevice.EDUCATIONAL_INSTITUTION,
        checked: false
    },
]

export const InternmentIndication: ValueOption[] = [
    {
        text: FormOption.YES,
        value: EIntermentIndicationStatus.YES,
        checked: false
    },
    {
        text: FormOption.NO,
        value: EIntermentIndicationStatus.NO,
        checked: false
    },
    {
        text: 'Sí, como medida de resguardo',
        value: EIntermentIndicationStatus.AS_PROTECTIVE_MEASURE,
        checked: false
    },
]

export const Establishments: ValueOption[] = [
    {
        text: EstablishmentsDescription.ADDRESS,
        value: EHealthInstitutionOrganization.MANAGEMENT,
        checked: false
    },
    {
        text: EstablishmentsDescription.AGAINST_VIOLENCE,
        value: EHealthInstitutionOrganization.COMMITTEE,
        checked: false
    },
    {
        text: EstablishmentsDescription.CLINIC,
        value: EHealthInstitutionOrganization.MEDICAL_CLINIC,
        checked: false
    },
    {
        text: EstablishmentsDescription.EDA,
        value: EHealthInstitutionOrganization.EDA,
        checked: false
    },
    {
        text: EstablishmentsDescription.EMERGENCY_CARE,
        value: EHealthInstitutionOrganization.EMERGENCY_CARE,
        checked: false
    },
    {
        text: EstablishmentsDescription.GYNECOLOGIC_OBSTRETRIC,
        value: EHealthInstitutionOrganization.GYNECOLOGY_OBSTETRICS,
        checked: false
    },
    {
        text: EstablishmentsDescription.MENTAL_HEALTH,
        value: EHealthInstitutionOrganization.MENTAL_HEALTH,
        checked: false
    },
    {
        text: EstablishmentsDescription.NURSING,
        value: EHealthInstitutionOrganization.NURSING,
        checked: false
    },
    {
        text: EstablishmentsDescription.OTHER,
        value: EHealthInstitutionOrganization.OTHERS,
        checked: false
    },
    {
        text: EstablishmentsDescription.PEDIATRIC,
        value: EHealthInstitutionOrganization.PEDIATRICS,
        checked: false
    },
    {
        text: EstablishmentsDescription.SAPS,
        value: EHealthInstitutionOrganization.SAPS,
        checked: false
    },
    {
        text: EstablishmentsDescription.SOCIAL_WORK,
        value: EHealthInstitutionOrganization.SOCIAL_WORK,
        checked: false
    },
]

export const Areas: ValueOption[] = [
    {
        text: AreaDescription.CAPS,
        value: EHealthSystemOrganization.CAPS,
        checked: false
    },
    {
        text: AreaDescription.CETEC,
        value: EHealthSystemOrganization.CETEC,
        checked: false
    },
    {
        text: AreaDescription.CPA,
        value: EHealthSystemOrganization.CPA,
        checked: false
    },
    {
        text: AreaDescription.HEALTH_REGION,
        value: EHealthSystemOrganization.SANITARY_REGION,
        checked: false
    },
    {
        text: AreaDescription.MINISTRY_HEADQUARTERS,
        value: EHealthSystemOrganization.MINISTRY_CENTER,
        checked: false
    },
    {
        text: AreaDescription.OTHER,
        value: EHealthSystemOrganization.OTHERS,
        checked: false
    },
    {
        text: AreaDescription.POSTAS,
        value: EHealthSystemOrganization.POSTAS,
        checked: false
    },
    {
        text: AreaDescription.PROVINCIAL_HOSPITAL,
        value: EHealthSystemOrganization.PROVINCIAL_HOSPITAL,
        checked: false
    },
    {
        text: AreaDescription.SIES,
        value: EHealthSystemOrganization.SIES,
        checked: false
    },
    {
        text: AreaDescription.UPA,
        value: EHealthSystemOrganization.UPA,
        checked: false
    },
    {
        text: AreaDescription.VACCINATION,
        value: EHealthSystemOrganization.VACCINATION_CENTER,
        checked: false
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
        value: null || undefined
    }
]

export const ViolenceTypes: ValueOption[] = [
    {
        text: ViolenceTypeDescription.DIRECT,
        value: EViolenceTowardsUnderageType.DIRECT_VIOLENCE,
        checked: false
    },
    {
        text: ViolenceTypeDescription.INDIRECT,
        value: EViolenceTowardsUnderageType.INDIRECT_VIOLENCE,
        checked: false
    },
    {
        text: FormOption.NO,
        value: EViolenceTowardsUnderageType.NO_VIOLENCE,
        checked: false
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: EViolenceTowardsUnderageType.NO_INFORMATION,
        checked: false
    },
]

export const EscolarizationLevels: ValueOption[] = [
    {
        text: EscolarizationLevelDescription.INITIAL,
        value: ESchoolLevel.NURSERY_SCHOOL,
        checked: false
    },
    {
        text: EscolarizationLevelDescription.MATERNAL,
        value: ESchoolLevel.KINDERGARTEN,
        checked: false
    },
    {
        text: EscolarizationLevelDescription.PRIMARY,
        value: ESchoolLevel.ELEMENTARY_SCHOOL,
        checked: false
    }
    , {
        text: EscolarizationLevelDescription.SECONDARY,
        value: ESchoolLevel.HIGH_SCHOOL,
        checked: false
    },
]

export const RiskLevels: ValueOption[] = [
    {
        text: RiskLevelDescription.HIGH,
        value: EViolenceEvaluationRiskLevel.HIGH,
        checked: false
    },
    {
        text: RiskLevelDescription.LOW,
        value: EViolenceEvaluationRiskLevel.LOW,
        checked: false
    },
    {
        text: RiskLevelDescription.MEDIUM,
        value: EViolenceEvaluationRiskLevel.MEDIUM,
        checked: false
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

export const InstitutionOptions: ValueOption[] = [
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
        checked: false
    },
    {
        text: 'Ex pareja',
        value: EAggressorRelationship.EX_PARTNER,
        checked: false
    },
    {
        text: 'Padre',
        value: EAggressorRelationship.FATHER,
        checked: false
    },
    {
        text: 'Progenitor afín (Padrastro)',
        value: EAggressorRelationship.STEPFATHER,
        checked: false
    },
    {
        text: 'Madre',
        value: EAggressorRelationship.MOTHER,
        checked: false
    },
    {
        text: 'Progenitora afín (Madrastra)',
        value: EAggressorRelationship.STEPMOTHER,
        checked: false
    },
    {
        text: 'Hijo',
        value: EAggressorRelationship.SON,
        checked: false
    },
    {
        text: 'Hija',
        value: EAggressorRelationship.DAUGHTER,
        checked: false
    },
    {
        text: 'Hermano/a',
        value: EAggressorRelationship.SIBLING,
        checked: false
    },
    {
        text: 'Superior jerárquico',
        value: EAggressorRelationship.SUPERIOR,
        checked: false
    },
    {
        text: 'Otra/o conocida/o',
        value: EAggressorRelationship.ACQUAINTANCE,
        checked: false
    },
    {
        text: 'Sin vinculo',
        value: EAggressorRelationship.NO_RELATIONSHIP,
        checked: false
    },
    {
        text: 'Sin información',
        value: EAggressorRelationship.NO_INFORMATION,
        checked: false
    },
    {
        text: 'No contesta',
        value: EAggressorRelationship.DOES_NOT_ANSWER,
        checked: false
    },
]
export const LiveTogetherStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: ELiveTogetherStatus.YES,
        checked: false
    },
    {
        text: "Sí, otra casa en mismo terreno o en cercanía",
        value: ELiveTogetherStatus.SAME_SPACE,
        checked: false
    },
    {
        text: FormOption.NO,
        value: ELiveTogetherStatus.NO,
        checked: false
    },
    {
        text: "No convive, pero convivio",
        value: ELiveTogetherStatus.NOT_NOW,
        checked: false
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: ELiveTogetherStatus.NO_INFORMATION,
        checked: false
    }
]
export const RelationshipLengths: ValueOption[] = [
    {
        text: "Hasta 6 meses",
        value: ERelationshipLength.UP_TO_SIX_MONTHS,
        checked: false
    },
    {
        text: "Hasta 1 año",
        value: ERelationshipLength.ONE_YEAR,
        checked: false
    },
    {
        text: "Mas de 1 año",
        value: ERelationshipLength.MORE_THAN_ONE_YEAR,
        checked: false
    }
]
export const ViolenceFrequencys: ValueOption[] = [
    {
        text: "Primera vez",
        value: EViolenceFrequency.FIRST_TIME,
        checked: false
    },
    {
        text: "Alguna vez anterior",
        value: EViolenceFrequency.SOMETIMES,
        checked: false
    },
    {
        text: "Con frecuencia",
        value: EViolenceFrequency.FREQUENT,
        checked: false
    }, 
    {
        text: "Sin información",
        value: EViolenceFrequency.NO_INFORMATION,
        checked: false
    }
]
export const CriminalRecordStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: ECriminalRecordStatus.YES,
        checked: false
    },
    {
        text: "Sí, con otras personas",
        value: ECriminalRecordStatus.WITH_OTHER_PEOPLE,
        checked: false
    },
    {
        text: FormOption.NO,
        value: ECriminalRecordStatus.NO,
        checked: false
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: ECriminalRecordStatus.NO_INFORMATION,
        checked: false
    }
]

export const DisabilityCertificateStatus: ValueOption[] = [
    {
        text: FormOption.YES,
        value: EDisabilityCertificateStatus.HAS_CERTIFICATE,
        checked: false
    },
    {
        text: FormOption.NO,
        value: EDisabilityCertificateStatus.HAS_NOT_CERTIFICATE,
        checked: false
    },
    {
        text: FormOption.IN_PROCESS,
        value: EDisabilityCertificateStatus.PENDING,
        checked: false
    },
    {
        text: FormOption.WITHOUT_DATA,
        value: EDisabilityCertificateStatus.NO_INFORMATION,
        checked: false
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
        value: EKeeperRelationship.BROTHER_OR_SISTER,
        checked: false
    },
    {
        text: RelationOptionDescription.FATHER,
        value: EKeeperRelationship.FATHER,
        checked: false
    },
    {
        text: RelationOptionDescription.GRANDFATHERMOTHER,
        value: EKeeperRelationship.GRANDPARENT,
        checked: false
    },
    {
        text: RelationOptionDescription.MOTHER,
        value: EKeeperRelationship.MOTHER,
        checked: false
    },
    {
        text: RelationOptionDescription.OTHER,
        value: EKeeperRelationship.OTHER,
        checked: false
    },
    {
        text: RelationOptionDescription.REFERRER,
        value: EKeeperRelationship.RELATED,
        checked: false
    },
    {
        text: RelationOptionDescription.AUNT,
        value: EKeeperRelationship.UNCLE_OR_AUNT,
        checked: false
    }
]
