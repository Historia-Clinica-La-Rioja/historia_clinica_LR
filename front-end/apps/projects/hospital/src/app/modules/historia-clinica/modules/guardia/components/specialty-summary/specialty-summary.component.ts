import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-specialty-summary',
    templateUrl: './specialty-summary.component.html',
    styleUrls: ['./specialty-summary.component.scss']
})
export class SpecialtySummaryComponent {
    
    @Input() specialty: string;
    constructor() { }
}
