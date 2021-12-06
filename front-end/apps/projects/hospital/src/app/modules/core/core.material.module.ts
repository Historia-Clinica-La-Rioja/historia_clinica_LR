import { NgModule } from '@angular/core';

import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
	imports: [
		MatDialogModule,
		MatIconModule,
		MatMenuModule,
		MatProgressBarModule,
		MatSnackBarModule,
	],
	exports: [
		MatDialogModule,
		MatMenuModule,
		MatIconModule,
		MatProgressBarModule,
		MatSnackBarModule,
	],
})
export class CoreMaterialModule { }
