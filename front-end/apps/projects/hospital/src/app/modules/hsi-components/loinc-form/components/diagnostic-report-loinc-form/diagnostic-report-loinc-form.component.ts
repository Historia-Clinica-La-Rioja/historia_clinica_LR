import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';
import {
	DiagnosticReportInfoDto,
	ProcedureTemplateFullSummaryDto,
	ProcedureTemplateShortSummaryDto,
} from '@api-rest/api-model';
import { ProcedureTemplatesService } from '@api-rest/services/procedure-templates.service';


@Component({
  selector: 'app-diagnostic-report-loinc-form',
  templateUrl: './diagnostic-report-loinc-form.component.html',
  styleUrls: ['./diagnostic-report-loinc-form.component.scss']
})
export class DiagnosticReportLoincFormComponent {
	@Output() valueChange: EventEmitter<any> = new EventEmitter<any>();
	templates: ProcedureTemplateShortSummaryDto[];
	procedureTemplateFullSummary$: Observable<ProcedureTemplateFullSummaryDto>;

	constructor(
		private procedureTemplatesService: ProcedureTemplatesService,
	) {

	}

	@Input() set diagnosticReportInfo(dri: DiagnosticReportInfoDto) {
		this.procedureTemplatesService.findByDiagnosticReportId(dri.id)
		.subscribe(templates => {
			this.templates = templates;
			if (templates.length > 0) {
				this.loadTemplate(this.templates[0]);
			}
		});
	}

	setTemplate(template: ProcedureTemplateShortSummaryDto) {
		this.loadTemplate(template);
	}

	private loadTemplate(template: ProcedureTemplateShortSummaryDto) {
		this.procedureTemplateFullSummary$ = this.procedureTemplatesService.findById(template.id);
	}

	changeValues($event) {
		this.valueChange.emit($event);
	}
}
