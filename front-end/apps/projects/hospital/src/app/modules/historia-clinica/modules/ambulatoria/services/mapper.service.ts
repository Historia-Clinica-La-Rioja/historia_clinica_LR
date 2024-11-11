import { Injectable } from '@angular/core';
import { SnomedTemplateDto } from '@api-rest/api-model';
import { TemplateOrConceptOption, TemplateOrConceptType } from '@historia-clinica/components/template-concept-typeahead-search/template-concept-typeahead-search.component';

@Injectable({
	providedIn: 'root'
})
export class MapperService {

    toTemplateOrConceptOption: (template: SnomedTemplateDto) => TemplateOrConceptOption = MapperService._toTemplateOrConceptOption;

    private static _toTemplateOrConceptOption(template: SnomedTemplateDto): TemplateOrConceptOption {
		return {
			type: TemplateOrConceptType.TEMPLATE,
			data: template
		};
	}
}
