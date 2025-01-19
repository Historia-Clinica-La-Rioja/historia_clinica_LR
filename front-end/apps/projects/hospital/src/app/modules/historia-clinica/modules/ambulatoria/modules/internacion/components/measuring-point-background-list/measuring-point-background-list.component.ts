import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { AnestheticReportVitalSignsService, MeasuringPointData } from '../../services/anesthetic-report-vital-signs.service';
import { MatDialog } from '@angular/material/dialog';
import { EditMeasuringPointComponent } from '../../dialogs/edit-measuring-point/edit-measuring-point.component';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

const PAGE_SIZE_OPTIONS = [5];
const PAGE_SIZE = 5;

@Component({
    selector: 'app-measuring-point-background-list',
    templateUrl: './measuring-point-background-list.component.html',
    styleUrls: ['./measuring-point-background-list.component.scss']
})
export class MeasuringPointBackgroundListComponent implements OnInit {

    @ViewChild('paginator') paginator: MatPaginator;
    @Input() service: AnestheticReportVitalSignsService;
    measuringPoints: MeasuringPointData[];

    pageSlice: MeasuringPointData[];
    pageSizeOptions = PAGE_SIZE_OPTIONS;
    pageSize = PAGE_SIZE;
    startPage = 0;
    endPage = PAGE_SIZE;
    page = 0;

    constructor(
		public dialog: MatDialog,
    ) { }

    ngOnInit(): void {
        this.service.measuringPoints$.subscribe((measuringPoints) => {
            this.measuringPoints = measuringPoints;
            this.pageSlice = this.measuringPoints.slice(this.startPage, this.endPage);
            this.checkIfPageEmpty();
        })
    }

    private checkIfPageEmpty() {
        if(this.pageSlice.length == 0 && this.startPage != 0){
            this.startPage = this.startPage - PAGE_SIZE;
            this.endPage = this.endPage - PAGE_SIZE;
            this.page = this.page - 1;
            this.pageSlice = this.measuringPoints.slice(this.startPage, this.endPage);
            this.paginator.previousPage();
        }
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

    onPageChange($event: PageEvent) {
        const page = $event;
        this.page = page.pageIndex;
		this.startPage = this.page * page.pageSize;
        this.endPage = $event.pageSize + this.startPage;
		this.pageSlice = this.measuringPoints.slice(this.startPage, this.endPage);
	}
}