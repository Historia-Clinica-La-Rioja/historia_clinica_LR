import { Component } from '@angular/core';
import { VitalSignsAttribute, VitalSignsData } from '../../services/anesthetic-report-vital-signs.service';
import { TimeDto } from '@api-rest/api-model';
import { FormGroup } from '@angular/forms';
import { ToFormGroup } from '@core/utils/form.utils';
import { AnestheticReportService } from '../../services/anesthetic-report.service';
import { Observable } from 'rxjs';


@Component({
	selector: 'app-anesthetic-report-vital-signs',
	templateUrl: './anesthetic-report-vital-signs.component.html',
	styleUrls: ['./anesthetic-report-vital-signs.component.scss']
})
export class AnestheticReportVitalSignsComponent {

	vitalSignsForm: FormGroup<ToFormGroup<VitalSignsData>>;
	selectedData$: Observable<VitalSignsData>;

	constructor(
		readonly service: AnestheticReportService,
	) { }

	ngOnInit(): void {
		this.vitalSignsForm = this.service.anestheticReportVitalSignsService.getVitalSignsForm();
		this.selectedData$ = this.service.anestheticReportVitalSignsService.vitalSigns$;
		this.service.anestheticReportVitalSignsService.validateDates(this.vitalSignsForm)
	}

	setDateAttribute(date: Date, attribute: VitalSignsAttribute) {
		this.service.anestheticReportVitalSignsService.setFormDateAttributeValue(attribute, date);
		this.service.anestheticReportVitalSignsService.validateDates(this.vitalSignsForm)
	}

	setTimeAttribute(time: TimeDto, attribute: VitalSignsAttribute) {
		this.service.anestheticReportVitalSignsService.setFormTimeAttributeValue(attribute, time);
		this.service.anestheticReportVitalSignsService.validateDates(this.vitalSignsForm)
	}
}
