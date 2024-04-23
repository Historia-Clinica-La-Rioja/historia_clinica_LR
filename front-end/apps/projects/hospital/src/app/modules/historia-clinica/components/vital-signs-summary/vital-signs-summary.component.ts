import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { VitalSignsData } from '@historia-clinica/services/anesthetic-report-document-summary.service';
import { VitalSignsChartPopupComponent } from '../vital-signs-chart-popup/vital-signs-chart-popup.component';

@Component({
    selector: 'app-vital-signs-summary',
    templateUrl: './vital-signs-summary.component.html',
    styleUrls: ['./vital-signs-summary.component.scss']
})
export class VitalSignsSummaryComponent {

    @Input() vitalSigns: VitalSignsData;

    constructor(
		private readonly dialog: MatDialog,
    ) { }

    showChart() {
        this.dialog.open(VitalSignsChartPopupComponent, {
            width: '50%',
            autoFocus: false,
            data: {
                imgList: this.vitalSigns.chart,
            }
        });
    }
}
