import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportVitalSignsService, MeasuringPointData } from '../../services/anesthetic-report-vital-signs.service';

@Component({
    selector: 'app-measuring-point-background-list',
    templateUrl: './measuring-point-background-list.component.html',
    styleUrls: ['./measuring-point-background-list.component.scss']
})
export class MeasuringPointBackgroundListComponent implements OnInit {

    @Input() service: AnestheticReportVitalSignsService;
    measuringPoints: MeasuringPointData[];

    constructor() { }

    ngOnInit(): void {
        this.service.measuringPoints$.subscribe((measuringPoints) => {
            this.measuringPoints = measuringPoints;
        })
    }

}
