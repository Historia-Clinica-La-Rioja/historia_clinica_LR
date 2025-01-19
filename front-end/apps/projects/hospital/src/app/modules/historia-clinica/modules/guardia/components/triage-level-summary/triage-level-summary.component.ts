import { Component, Input } from '@angular/core';
import { TriageCategory } from '../triage-chip/triage-chip.component';

@Component({
    selector: 'app-triage-level-summary',
    templateUrl: './triage-level-summary.component.html',
    styleUrls: ['./triage-level-summary.component.scss']
})
export class TriageLevelSummaryComponent {

    @Input() triageLevel: TriageCategory;
    
    constructor() { }
}
