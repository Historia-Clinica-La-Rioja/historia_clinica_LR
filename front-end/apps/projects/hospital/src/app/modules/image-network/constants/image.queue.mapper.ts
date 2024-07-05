import { EImageMoveStatus, ImageQueueListDto, ImageQueuePatientDataDto } from "@api-rest/api-model"
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component"
import { ItemImageQueue } from "../components/image-table-technical/image-table-technical.component"
import { Color } from "@presentation/colored-label/colored-label.component"
import { PatientNameService } from "@core/services/patient-name.service"
import { convertDateTimeDtoToDate, dateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper"


export const EIMAGE_QUEUE_ERROR = EImageMoveStatus.ERROR

export const QUEUE_ERROR = 'Error'
export const MOVIENDO = 'Moviendo ...'
const PENDIENTE = 'Pendiente de moverse'


export const translateMappingStatus = new Map<EImageMoveStatus,string>()
translateMappingStatus.set(EImageMoveStatus.ERROR, QUEUE_ERROR)
translateMappingStatus.set(EImageMoveStatus.MOVING, MOVIENDO)
translateMappingStatus.set(EImageMoveStatus.PENDING, PENDIENTE)

export const mapToListItemImageQueue = (items: ImageQueueListDto[], patientNameServiceInstance: PatientNameService): ItemImageQueue[] => {
    return items.map(item => {
        return {
            person: toPatientSummary(item.patient, patientNameServiceInstance),
            studies: item.studies ? item.studies.join(' | ') : null,
            status: {
                description: translateMappingStatus.get(item.imageMoveStatus),
                color: item.imageMoveStatus === EIMAGE_QUEUE_ERROR ? Color.RED : Color.YELLOW,
            },
            date: item.lastTriedOn ? convertDateTimeDtoToDate(item.lastTriedOn) : null,
            appointmentDate: dateTimeDtoToDate(item.appointmentDateTime),
            serviceRequestId: item.serviceRequestId,
            idMove: item.id,
            uid:item.studyImageUID
        }
    })

}

const toPatientSummary = (patient: ImageQueuePatientDataDto, patientNameService: PatientNameService): PatientSummary => {
    return {
        fullName: patientNameService.completeName(patient.firstName, patient.nameSelfDetermination, patient.lastName,patient.middleNames, patient.otherLastNames),
        identification: {
            type: patient.identificationType,
            number: +patient.identificationNumber,
        },
        id: patient.personId,
        gender: patient.gender.description,
        age: patient.personAgeDto.years,
    }
}