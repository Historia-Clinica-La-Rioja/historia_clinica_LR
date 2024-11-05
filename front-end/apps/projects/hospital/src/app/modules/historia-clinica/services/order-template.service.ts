import { Injectable } from '@angular/core';

@Injectable()
export class OrderTemplateService {

	studies: RelatedSnomedTemplate[] = [];

	updateStudiesFromTemplate = (sctid: string) => {
		const templateId: number = this.studies.find((study: RelatedSnomedTemplate) => study.sctid === sctid)?.templateId;
		this.studies = this.studies.filter((study: RelatedSnomedTemplate) => study.templateId !== templateId);
	}

	addStudy = (sctid: string, templateId: number) => {
		const study: RelatedSnomedTemplate = {
			sctid,
			templateId
		}
		this.studies.push(study);
	}

	getTemplatesIds = (): number[] | null => {
		return Array.from(new Set(this.studies.map((study: RelatedSnomedTemplate) => study.templateId)));
	}
}

interface RelatedSnomedTemplate {
	sctid: string,
	templateId: number
}
