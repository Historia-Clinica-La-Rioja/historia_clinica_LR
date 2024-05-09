import { EImageMoveStatus, ImageQueueListDto, ImageQueuePatientDataDto } from "@api-rest/api-model"
import { PatientSummary } from "../../hsi-components/patient-summary/patient-summary.component"
import { ItemImageQueue } from "../components/image-table-technical/image-table-technical.component"
import { Color } from "@presentation/colored-label/colored-label.component"
import { PatientNameService } from "@core/services/patient-name.service"


const EIMAGE_QUEUE_ERROR = EImageMoveStatus.ERROR

export const QUEUE_ERROR = 'Error'
const MOVIENDO = 'Moviendo ...'
const PENDIENTE = 'Pendiente de moverse'


const translateMappingStatus = new Map<EImageMoveStatus,string>()
translateMappingStatus.set(EImageMoveStatus.ERROR, QUEUE_ERROR)
translateMappingStatus.set(EImageMoveStatus.MOVING, MOVIENDO)
translateMappingStatus.set(EImageMoveStatus.PENDING, PENDIENTE)

export const mapToItemImageQueue = (items: ImageQueueListDto[], patientNameServiceInstance: PatientNameService): ItemImageQueue[] => {
    return items.map(item => {
        return {
            person: toPatientSummary(item.patient, patientNameServiceInstance),
            studies: item.studies ? item.studies.join(' | ') : null,
            status: {
                description: translateMappingStatus.get(item.imageMoveStatus),
                color: item.imageMoveStatus === EIMAGE_QUEUE_ERROR ? Color.RED : Color.YELLOW,
            },
            date: new Date(item.updatedOn),
            serviceRequestId: item.serviceRequestId,
            idMove: item.id
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