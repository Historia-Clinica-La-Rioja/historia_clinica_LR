import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component";
import { DescriptionPriority } from "@presentation/components/priority-select/priority-select.component";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";
import { ReferenceApprovalState, ReferenceState } from "./approval";

const NO_CLOSURE = -1;
const NO_APPOINTMENT = -1;

export const PENDING_ATTENTION_STATE = undefined;

export enum REFERENCE_STATES {
    PENDING = 'PENDIENTE',
    ASSIGNED = 'ASIGNADO',
    ABSENT = 'AUSENTE',
    SERVED = 'ATENDIDO',
}

export enum REFERENCE_STATES_ID {
    PENDING = -1,
    ASSIGNED = 1,
    ABSENT = 3,
    SERVED = 2,
}

export const APPOINTMENT_STATE = [
    {
        id: APPOINTMENT_STATES_ID.ASSIGNED,
        description: REFERENCE_STATES.ASSIGNED
    },
    {
        id: APPOINTMENT_STATES_ID.ABSENT,
        description: REFERENCE_STATES.ABSENT
    },
    {
        id: APPOINTMENT_STATES_ID.SERVED,
        description: REFERENCE_STATES.SERVED
    },
    {
        id: NO_APPOINTMENT,
        description: REFERENCE_STATES.PENDING
    }
];

export const ATTENTION_STATE = [
    {
        id: REFERENCE_STATES_ID.ASSIGNED,
        description: REFERENCE_STATES.ASSIGNED
    },
    {
        id: REFERENCE_STATES_ID.ABSENT,
        description: REFERENCE_STATES.ABSENT
    },
    {
        id: REFERENCE_STATES_ID.SERVED,
        description: REFERENCE_STATES.SERVED
    },
    {
        id: REFERENCE_STATES_ID.PENDING,
        description:  REFERENCE_STATES.PENDING
    }
];

export const PRIORITY_OPTIONS = [
    {
        id: 1,
        description: DescriptionPriority.HIGH
    },
    {
        id: 2,
        description: DescriptionPriority.MEIDUM
    },
    {
        id: 3,
        description: DescriptionPriority.LOW
    }]

export const CLOSURE_OPTIONS = [
    {
        id: NO_CLOSURE,
        description: "Referencia solicitada"
    },
    {
        id: 1,
        description: "Continúa en observación"
    },
    {
        id: 2,
        description: "Inicia tratamiento en centro de referencia"
    },
    {
        id: 3,
        description: "Requiere estudios complementarios"
    },
    {
        id: 4,
        description: "Contrarreferencia"
    },
    {
        id: 5,
        description: "Cierre administrativo de referencia"
    }
];

export const PENDING: ColoredLabel = {
    description: REFERENCE_STATES.PENDING,
    color: Color.YELLOW
}

export const APPROVAL_OPTIONS = [
    {
        id: ReferenceApprovalState.WAITING_APPROVAL,
        description: "Esperando aprobación"
    },
    {
        id: ReferenceApprovalState.APPROVED,
        description: "Solicitud aprobada"
    },
    {
        id: ReferenceApprovalState.REJECTED,
        description: "Solicitud rechazada"
    },
    {
        id: ReferenceApprovalState.SUGGESTED_REVISION,
        description: "Revisión sugerida"
    }
];

export const REFERENCE_STATE_OPTIONS = [
    {
        id: ReferenceState.PENDING_AUDIT,
        description: "Esperando auditoria"
    },
    {
        id: ReferenceState.AUDIT,
        description: "Auditada"
    },
    {
        id: ReferenceState.REJECTED,
        description: "Rechazada"
    },
    {
        id: ReferenceState.SUGGESTED_REVISION,
        description: "Revisión sugerida"
    },
    {
        id: ReferenceState.AUDIT_NOT_REQUIRED,
        description: "No requiere auditoria"
    }
]
