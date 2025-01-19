import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-version-available',
	templateUrl: './version-available.component.html',
	styleUrls: ['./version-available.component.scss']
})
export class VersionAvailableComponent implements OnInit {

	constructor(
		private readonly dialogRef: MatDialogRef<VersionAvailableComponent>,
	) { }

	ngOnInit(): void {
	}

	reload(){
		this.dialogRef.close(true)
	}

}
