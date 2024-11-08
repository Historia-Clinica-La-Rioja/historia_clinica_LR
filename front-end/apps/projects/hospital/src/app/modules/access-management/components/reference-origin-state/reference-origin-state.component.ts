import { getIconState } from '@access-management/constants/approval';
import { Component, OnInit } from '@angular/core';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';

@Component({
    selector: 'app-reference-origin-state',
    templateUrl: './reference-origin-state.component.html',
    styleUrls: ['./reference-origin-state.component.scss']
})
export class ReferenceOriginStateComponent implements OnInit {

    referenceStates = 'PENDING_AUDIT';

    regulationState: ColoredLabel;

    regulation = {
        referenceId: 990,
        state: "SUGGESTED_REVISION",
        professionalName: "Eduardito ALFARO",
        reason: 'eso es un motivo',
        createdOn: {
            date: {
                year: 2024,
                month: 11,
                day: 5
            },
            time: {
                hours: 18,
                minutes: 42,
                seconds: 50
            }
        }
    }

    constructor() { }

    ngOnInit(): void {
        this.regulationState = getIconState[this.regulation.state];
    }

}
