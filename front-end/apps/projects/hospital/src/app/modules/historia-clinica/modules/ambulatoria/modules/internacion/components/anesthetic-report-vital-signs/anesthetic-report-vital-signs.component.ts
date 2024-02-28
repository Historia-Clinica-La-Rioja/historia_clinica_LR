import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AnestheticReportVitalSignsService } from '../../services/anesthetic-report-vital-signs.service';
import { TimeDto } from '@api-rest/api-model';

@Component({
    selector: 'app-anesthetic-report-vital-signs',
    templateUrl: './anesthetic-report-vital-signs.component.html',
    styleUrls: ['./anesthetic-report-vital-signs.component.scss']
})
export class AnestheticReportVitalSignsComponent implements OnInit {

    @Input() service: AnestheticReportVitalSignsService;
    form: FormGroup;
    datePickerDate: Date;
    possibleTimesList: TimeDto[];

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();
        this.possibleTimesList = this.service.getPossibleTimesList();
    }

    setSelectedDate(date: Date){
        this.datePickerDate = date;
    }

    registerMeasuringPoint() {
        this.service.handleMeasuringPointRegister();
    }
}
