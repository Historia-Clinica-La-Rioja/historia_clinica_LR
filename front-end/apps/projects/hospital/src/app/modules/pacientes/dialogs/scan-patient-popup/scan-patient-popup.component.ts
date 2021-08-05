import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
	selector: 'app-scan-patient-popup',
	templateUrl: './scan-patient-popup.component.html',
	styleUrls: ['./scan-patient-popup.component.scss']
})

export class ScanPatientPopupComponent implements OnInit {
	public formScanPatient: FormGroup;
	
	constructor(private formBuilder: FormBuilder) { }

	ngOnInit(): void {
		this.formScanPatient = this.formBuilder.group({
			informationPatient: []
		})
	}

}
