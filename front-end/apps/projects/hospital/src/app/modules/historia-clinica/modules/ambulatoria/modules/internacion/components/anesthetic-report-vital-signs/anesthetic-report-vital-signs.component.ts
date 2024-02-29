import { Component, Input } from '@angular/core';
import { AnestheticReportVitalSignsService } from '../../services/anesthetic-report-vital-signs.service';

@Component({
    selector: 'app-anesthetic-report-vital-signs',
    templateUrl: './anesthetic-report-vital-signs.component.html',
    styleUrls: ['./anesthetic-report-vital-signs.component.scss']
})
export class AnestheticReportVitalSignsComponent {

    @Input() service: AnestheticReportVitalSignsService;

    constructor() { }
}
