import { Injectable } from '@angular/core';
import { CATEGORY_IMAGE, DiagnosticWithTypeReportInfoDto } from '../model/ImageModel';

@Injectable({
	providedIn: 'root'
})
export class FilterImageOrderStudyService {

	private IMAGE_DIAGNOSIS_ID = '363679005'
	private isImageDiagnosticCategory = (value: string) => value == this.IMAGE_DIAGNOSIS_ID ? CATEGORY_IMAGE : ''

	constructor() {}

	filterCategoriaDImagenes(diagnosticImage: DiagnosticWithTypeReportInfoDto ,categoryId: string): boolean {
		return (categoryId ? diagnosticImage.category === this.isImageDiagnosticCategory(categoryId) : true);
	}

	filterStatus(diagnosticImage: DiagnosticWithTypeReportInfoDto ,statusId: string): boolean {
		return (statusId ? diagnosticImage.statusId === statusId : true);
	}

	filterStudy(diagnosticImage: DiagnosticWithTypeReportInfoDto, study: string): boolean {
		return (study ? this.filterString(diagnosticImage.snomed.pt, study) : true);
	}

	filterString(target: string, filterValue: string): boolean {
		return target?.toLowerCase().includes(filterValue?.toLowerCase());
	}

	applyFilterStudyImageOrders(studySource: DiagnosticWithTypeReportInfoDto, filterValue:StudyOrderFilters): boolean {
		return  this.filterCategoriaDImagenes(studySource, filterValue.categoryId?.toString()) && this.filterStatus(studySource, filterValue.statusId)
		&& this.filterStudy(studySource, filterValue.study)
	}
}


export interface StudyOrderFilters {
	categoryId?: number;
	statusId?: string;
	healthCondition?: number;
	study?: string;
}
