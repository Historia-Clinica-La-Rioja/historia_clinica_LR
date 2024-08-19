import { Component, OnInit } from '@angular/core';
import { ProposedSurgery } from '../../services/anesthetic-report-proposed-surgery.service';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-proposed-surgery-background-list',
    templateUrl: './proposed-surgery-background-list.component.html',
    styleUrls: ['./proposed-surgery-background-list.component.scss']
})
export class ProposedSurgeryBackgroundListComponent implements OnInit {

    proposedSurgeries: ProposedSurgery[];
    allCheckedSurgeries = false;

    constructor(
        readonly service: AnestheticReportService
    ) { }

    ngOnInit(): void {
        this.service.anesthesicReportProposedSurgeryService.getProposedSurgeries().subscribe(proposedSurgeries => {
            this.proposedSurgeries = proposedSurgeries;
            this.updateAll();
        })
    }

    updateAll() {
		this.allCheckedSurgeries = this.proposedSurgeries?.every(ps => ps.isAdded);
	}

    setAllSurgeries(completed: boolean) {
		this.allCheckedSurgeries = completed;
		if (this.proposedSurgeries == null) {
			return;
		}
		this.proposedSurgeries.forEach(ps => (ps.isAdded = completed));
	}
}
