import { MasterDataDto } from "@api-rest/api-model";

export enum EAppointmentExpiredReasons {
    OTHER = 1
}

const expiredReasons: MasterDataDto[] = [{
    id: EAppointmentExpiredReasons.OTHER,
    description: "Otro"
}]

export const getExpiredReasons = (): MasterDataDto[] => expiredReasons;

