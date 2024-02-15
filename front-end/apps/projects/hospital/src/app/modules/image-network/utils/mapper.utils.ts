import { StudyAppointmentDto } from "@api-rest/api-model";
import { getImageSizeInMB, mapToState } from "./study.utils";
import { StudyAppointment } from "../models/models";

export const toStudyAppointment = (s: StudyAppointmentDto): StudyAppointment => {
    return {
        info: s,
        state: mapToState(s.statusId),
        imageSize: getImageSizeInMB(s.sizeImage)
    }
}
