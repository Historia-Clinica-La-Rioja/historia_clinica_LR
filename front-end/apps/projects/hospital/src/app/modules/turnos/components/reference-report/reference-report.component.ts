import { Component } from '@angular/core';
import { ReferenceReportFacadeService } from '@turnos/services/reference-report-facade.service';

@Component({
	selector: 'app-reference-report',
	templateUrl: './reference-report.component.html',
	styleUrls: ['./reference-report.component.scss']
})
export class ReferenceReportComponent {

	constructor(
		readonly referenceReportFacade: ReferenceReportFacadeService,
	) { }

}



