import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-appointment-historic',
    templateUrl: './appointment-historic.component.html',
    styleUrls: ['./appointment-historic.component.scss']
})
export class AppointmentHistoricComponent implements OnInit {
    constructor(
        @Inject(MAT_DIALOG_DATA) public data,
    ) { }

    ngOnInit(): void {
       
    }

}
