import { ESecurityForceType, EAggressorRelationship, ELiveTogetherStatus, ERelationshipLength, EViolenceFrequency, ECriminalRecordStatus } from "@api-rest/api-model";
import { BasicOption, FormOption, ValueOption } from "../components/violence-situation-person-information/violence-situation-person-information.component";

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
