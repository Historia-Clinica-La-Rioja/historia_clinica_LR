import { Component, Input } from '@angular/core';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { NewConsultationPersonalHistoriesService } from '@historia-clinica/modules/ambulatoria/services/new-consultation-personal-histories.service';

@Component({
    selector: 'app-personal-histories-background-list',
    templateUrl: './personal-histories-background-list.component.html',
    styleUrls: ['./personal-histories-background-list.component.scss']
})
export class PersonalHistoriesBackgroundListComponent {
    
    @Input() service: NewConsultationPersonalHistoriesService;
    momentFormat = momentFormat;
    readonly DateFormat = DateFormat;
    
    constructor() { }

}
