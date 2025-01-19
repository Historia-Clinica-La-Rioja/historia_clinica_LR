import { Component, Input, OnInit } from '@angular/core';
import { MeasuringPointData } from '../../services/anesthetic-report-vital-signs.service';

@Component({
    selector: 'app-measuring-point-item',
    templateUrl: './measuring-point-item.component.html',
    styleUrls: ['./measuring-point-item.component.scss']
})
export class MeasuringPointItemComponent implements OnInit {

    @Input() measuringPoint: MeasuringPointData;
    @Input() index: number;

    constructor() { }

    ngOnInit(): void {
    }

}
