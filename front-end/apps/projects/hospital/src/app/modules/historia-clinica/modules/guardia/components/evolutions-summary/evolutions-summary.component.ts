import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-evolutions-summary',
    templateUrl: './evolutions-summary.component.html',
    styleUrls: ['./evolutions-summary.component.scss']
})
export class EvolutionsSummaryComponent {

    @Input() evolution: string;
    constructor() { }
}
