import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';
import { AnestheticReportVitalSignsService } from '../../services/anesthetic-report-vital-signs.service';

@Component({
    selector: 'app-measuring-point',
    templateUrl: './measuring-point.component.html',
    styleUrls: ['./measuring-point.component.scss']
})
export class MeasuringPointComponent implements OnInit {

    @Input() service: AnestheticReportVitalSignsService;
    form: FormGroup;
    possibleTimesList: TimeDto[];

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();
        this.possibleTimesList = this.service.getPossibleTimesList();
    }

    setSelectedDate(date: Date){
        this.service.setStartingDate(date)
    }

    registerMeasuringPoint() {
        this.service.handleMeasuringPointRegister();
    }
}
