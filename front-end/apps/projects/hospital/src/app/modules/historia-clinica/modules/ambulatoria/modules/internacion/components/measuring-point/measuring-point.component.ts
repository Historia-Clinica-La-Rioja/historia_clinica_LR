import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';
import { AnestheticReportVitalSignsService } from '../../services/anesthetic-report-vital-signs.service';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';

@Component({
    selector: 'app-measuring-point',
    templateUrl: './measuring-point.component.html',
    styleUrls: ['./measuring-point.component.scss']
})
export class MeasuringPointComponent implements OnInit {

    @Input() service: AnestheticReportVitalSignsService;
    form: FormGroup;
    timePickerData: TimePickerData;

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();
        this.timePickerData = this.service.getTimePickerData();
    }

    setSelectedDate(date: Date){
        this.service.setStartingDate(date)
    }

    registerMeasuringPoint() {
        this.service.handleMeasuringPointRegister();
    }

    setMeasuringPointStartTime(time: TimeDto) {
        this.form.controls.measuringPointStartTime.setValue(time);
    }
}
