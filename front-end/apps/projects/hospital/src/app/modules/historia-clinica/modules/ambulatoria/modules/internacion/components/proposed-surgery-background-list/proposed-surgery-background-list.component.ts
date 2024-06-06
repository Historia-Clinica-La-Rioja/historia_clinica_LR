import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportProposedSurgeryService, ProposedSurgery } from '../../services/anesthetic-report-proposed-surgery.service';

@Component({
    selector: 'app-proposed-surgery-background-list',
    templateUrl: './proposed-surgery-background-list.component.html',
    styleUrls: ['./proposed-surgery-background-list.component.scss']
})
export class ProposedSurgeryBackgroundListComponent implements OnInit {

    @Input() service: AnestheticReportProposedSurgeryService;
    proposedSurgeries: ProposedSurgery[];
    allCheckedSurgeries = false;

    constructor() { }

    ngOnInit(): void {
        this.service.getProposedSurgeries().subscribe(proposedSurgeries => {
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
