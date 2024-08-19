import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { VitalSignsChartPopupComponent } from '../vital-signs-chart-popup/vital-signs-chart-popup.component';
import { VitalSignsData } from '@historia-clinica/utils/document-summary.model';

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
                imgList: this.vitalSigns.vitalSignsChart,
            }
        });
    }
}
