import { DiagnosticReportInfoDto, HCEDocumentDataDto } from "@api-rest/api-model";

export const CATEGORY_IMAGE = "Diagnóstico por imágenes"

export const enum E_TYPE_ORDER {
	TRANSCRIPTA = 'transcripta',
	SIN_ORDEN = 'sin orden',
	COMPLETA = 'completa'
}

export interface DiagnosticWithTypeReportInfoDto extends DiagnosticReportInfoDto{
	typeOrder: E_TYPE_ORDER,
	infoOrderInstances: InfoNewTypeOrderDto
}


export interface InfoNewTypeOrderDto {
	imageId?: string;
	hceDocumentDataDto?: HCEDocumentDataDto ;
	status?: boolean;
	seeStudy?: boolean;
	viewReport?: boolean;
}


