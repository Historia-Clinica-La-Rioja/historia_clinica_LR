import { Component, Input } from '@angular/core';
import { AnestheticReportVitalSignsService, VitalSignsAttribute, VitalSignsData } from '../../services/anesthetic-report-vital-signs.service';
import { TimeDto } from '@api-rest/api-model';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { FormGroup } from '@angular/forms';
import { ToFormGroup } from '@core/utils/form.utils';

@Component({
    selector: 'app-anesthetic-report-vital-signs',
    templateUrl: './anesthetic-report-vital-signs.component.html',
    styleUrls: ['./anesthetic-report-vital-signs.component.scss']
})
export class AnestheticReportVitalSignsComponent {

    @Input() service: AnestheticReportVitalSignsService;
    vitalSignsForm: FormGroup<ToFormGroup<VitalSignsData>>;
    timePickerData: TimePickerData = {
        hideLabel: true,
    }

    constructor() { }

    ngOnInit(): void {
        this.vitalSignsForm = this.service.getVitalSignsForm();
    }

    setDateAttribute(date: Date, attribute: VitalSignsAttribute) {
        this.service.setFormDateAttributeValue(attribute, date)
    }

    setTimeAttribute(time: TimeDto, attribute: VitalSignsAttribute) {
        this.service.setFormTimeAttributeValue(attribute, time)
    }
}
