import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportVitalSignsService, MeasuringPointData } from '../../services/anesthetic-report-vital-signs.service';
import { MatDialog } from '@angular/material/dialog';
import { EditMeasuringPointComponent } from '../../dialogs/edit-measuring-point/edit-measuring-point.component';

@Component({
    selector: 'app-measuring-point-background-list',
    templateUrl: './measuring-point-background-list.component.html',
    styleUrls: ['./measuring-point-background-list.component.scss']
})
export class MeasuringPointBackgroundListComponent implements OnInit {

    @Input() service: AnestheticReportVitalSignsService;
    measuringPoints: MeasuringPointData[];

    constructor(
		public dialog: MatDialog,
    ) { }

    ngOnInit(): void {
        this.service.measuringPoints$.subscribe((measuringPoints) => {
            this.measuringPoints = measuringPoints;
        })
    }

    openEditDialog(index: number) {
        const dialogRef = this.dialog.open(EditMeasuringPointComponent, {
			width: '35%',
			data: {
				service: this.service,
                measuringPoint: this.measuringPoints.at(index)
			}
		});
		dialogRef.afterClosed().subscribe((newMeasuringPoint) => {
            if(newMeasuringPoint){
                this.service.editMeasuringPoint(newMeasuringPoint, index);
            }
		});
    }
}
