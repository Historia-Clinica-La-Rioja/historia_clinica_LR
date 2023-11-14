import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component";
import { DescriptionPriority } from "@presentation/components/priority-select/priority-select.component";
import { APPOINTMENT_STATES_ID } from "@turnos/constants/appointment";

const NO_CLOSURE = -1;
const NO_APPOINTMENT = -1;

export enum REFERENCE_STATES {
    PENDING = 'PENDIENTE',
    ASSIGNED = 'ASIGNADO',
    ABSENT = 'AUSENTE',
    SERVED = 'ATENDIDO',
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
    }
];

export const PENDING: ColoredLabel = {
    description: REFERENCE_STATES.PENDING,
    color: Color.YELLOW
}