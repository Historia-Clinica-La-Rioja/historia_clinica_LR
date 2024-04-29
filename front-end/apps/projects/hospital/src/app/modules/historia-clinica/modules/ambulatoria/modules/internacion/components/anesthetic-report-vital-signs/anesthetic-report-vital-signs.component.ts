import { Component } from '@angular/core';
import { VitalSignsAttribute, VitalSignsData } from '../../services/anesthetic-report-vital-signs.service';
import { TimeDto } from '@api-rest/api-model';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { FormGroup } from '@angular/forms';
import { ToFormGroup } from '@core/utils/form.utils';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-anesthetic-report-vital-signs',
    templateUrl: './anesthetic-report-vital-signs.component.html',
    styleUrls: ['./anesthetic-report-vital-signs.component.scss']
})
export class AnestheticReportVitalSignsComponent {

    vitalSignsForm: FormGroup<ToFormGroup<VitalSignsData>>;
    timePickerData: TimePickerData = {
        hideLabel: true,
    }

    constructor(
        readonly service: AnestheticReportService,
    ) { }

    ngOnInit(): void {
        this.vitalSignsForm = this.service.anestheticReportVitalSignsService.getVitalSignsForm();
    }

    setDateAttribute(date: Date, attribute: VitalSignsAttribute) {
        this.service.anestheticReportVitalSignsService.setFormDateAttributeValue(attribute, date)
    }

    setTimeAttribute(time: TimeDto, attribute: VitalSignsAttribute) {
        this.service.anestheticReportVitalSignsService.setFormTimeAttributeValue(attribute, time)
    }
}
