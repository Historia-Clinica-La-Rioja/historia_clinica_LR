import { NgModule } from '@angular/core';

import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTooltipModule } from '@angular/material/tooltip';

@NgModule({
	imports: [
		MatDialogModule,
		MatIconModule,
		MatMenuModule,
		MatProgressBarModule,
		MatSelectModule,
		MatSnackBarModule,
		MatTooltipModule,
	],
	exports: [
		MatDialogModule,
		MatMenuModule,
		MatIconModule,
		MatProgressBarModule,
		MatSelectModule,
		MatSnackBarModule,
		MatTooltipModule,
	],
})
export class CoreMaterialModule { }
