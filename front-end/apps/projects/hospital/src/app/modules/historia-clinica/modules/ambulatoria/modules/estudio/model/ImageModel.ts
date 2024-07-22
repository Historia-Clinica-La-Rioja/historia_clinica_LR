import { DateDto, DiagnosticReportInfoDto, HCEDocumentDataDto, TimeDto } from "@api-rest/api-model";

export const CATEGORY_IMAGE = "Diagnóstico por imágenes"
export const IMAGE_DIAGNOSIS_CATEGORY_ID = '363679005'

export enum E_TYPE_ORDER {
	TRANSCRIPTA = 'transcripta',
	SIN_ORDEN = 'sin orden',
	COMPLETA = 'completa'
}

export interface DiagnosticWithTypeReportInfoDto extends DiagnosticReportInfoDto{
	typeOrder: E_TYPE_ORDER,
	infoOrderInstances: InfoNewTypeOrderDto | InfoNewStudyOrderDto
}


export interface InfoNewTypeOrderDto {
	imageId?: string;
	hceDocumentDataDto?: HCEDocumentDataDto ;
	status?: boolean;
	isAvailableInPACS?: boolean;
	viewReport?: boolean;
	associatedStudies?: string[];
	dateAppoinment: AppointmentDate;
	localViewerUrl?: string;
}


export interface InfoNewStudyOrderDto extends InfoNewTypeOrderDto {
	hasActiveAppointment?: boolean;
}

export interface AppointmentDate {
	appointmentDate: DateDto;
    appointmentHour: TimeDto;
}