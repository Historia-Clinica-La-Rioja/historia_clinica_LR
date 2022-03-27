import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { HCEAllergyDto } from "@api-rest/api-model";

@Component({
	selector: 'app-show-allergies',
	templateUrl: './show-allergies.component.html',
	styleUrls: ['./show-allergies.component.scss']
})
export class ShowAllergiesComponent implements OnInit {

	constructor(@Inject(MAT_DIALOG_DATA) public data: { allergies: HCEAllergyDto[] }
	) {
	}

	ngOnInit(): void {
	}

}
