import { Component, Input } from '@angular/core';
import { ObstetricEventInfo } from '@historia-clinica/utils/document-summary.model';

@Component({
    selector: 'app-obstetric-event-summary',
    templateUrl: './obstetric-event-summary.component.html',
    styleUrls: ['./obstetric-event-summary.component.scss']
})
export class ObstetricEventSummaryComponent  {
    @Input() eventData: ObstetricEventInfo;
    constructor() { }
}
