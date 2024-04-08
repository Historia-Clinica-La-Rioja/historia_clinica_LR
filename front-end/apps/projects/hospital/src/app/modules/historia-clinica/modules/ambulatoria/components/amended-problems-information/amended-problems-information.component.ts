import { Component, Input } from '@angular/core';
import { HCEErrorProblemDto } from '@api-rest/api-model';

@Component({
    selector: 'app-amended-problems-information',
    templateUrl: './amended-problems-information.component.html',
    styleUrls: ['./amended-problems-information.component.scss']
})
export class AmendedProblemsInformationComponent {

    @Input() errorProblem: HCEErrorProblemDto;

    constructor() { }
}
