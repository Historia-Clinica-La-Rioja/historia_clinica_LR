import { Injectable } from '@angular/core';
import { SnomedTemplateDto } from '@api-rest/api-model';
import { Study } from './order-studies.service';
import { pushIfNotExists } from '@core/utils/array.utils';

@Injectable()
export class OrderTemplateService {

	private studies: RelatedSnomedTemplate[] = [];

	private allTemplates: SnomedTemplateDto[] = [];

	updateStudiesFromTemplate = (sctid: string) => {
		const templateId: number = this.studies.find((study: RelatedSnomedTemplate) => study.sctid === sctid)?.templateId;
		this.studies = this.studies.filter((study: RelatedSnomedTemplate) => study.templateId !== templateId);
	}

	addStudy = (sctid: string, templateId: number) => {
		const study: RelatedSnomedTemplate = {
			sctid,
			templateId
		}
		this.studies = pushIfNotExists<RelatedSnomedTemplate>(this.studies, study, this.compare);
	}

	getTemplatesIds = (): number[] => {
		return Array.from(new Set(this.studies.map((study: RelatedSnomedTemplate) => study.templateId)));
	}

	setAllTemplates = (templates: SnomedTemplateDto[]) => {
		this.allTemplates = templates;
	}

	addTemplate = (studies: Study[]) => {
		const templates = this.getTemplatesWithStudies(studies);
		templates.forEach(template => {
			template.concepts.forEach(concept => this.addStudy(concept.conceptId, template.id));
		});
	}

	private compare = (r: RelatedSnomedTemplate, rst: RelatedSnomedTemplate): boolean => {
		return r.sctid === rst.sctid;
	}

	private getTemplatesWithStudies = (studies: Study[]): SnomedTemplateDto[] => {
		const matchingTemplates = this.allTemplates.filter(template => {
			const allConceptsMatch = template.concepts.every(concept =>
				studies.some(study => concept.conceptId === study.snomed.sctid)
			);
			return allConceptsMatch;
		});
		return matchingTemplates;
	}
}

interface RelatedSnomedTemplate {
	sctid: string,
	templateId: number
}
