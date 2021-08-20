import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToothComponent } from './components/tooth/tooth.component';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
import { AppMaterialModule } from '@material/app.material.module';
import { OdontogramComponent } from './components/odontogram/odontogram.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ToothDialogComponent } from './components/tooth-dialog/tooth-dialog.component';
import { ConceptsFacadeService } from './services/concepts-facade.service';



@NgModule({
	declarations: [ToothComponent, OdontogramComponent, ToothDialogComponent],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		AppMaterialModule,
		ReactiveFormsModule
	],
	exports: [
		ToothComponent,
		OdontogramComponent
	],
	providers: [ConceptsFacadeService]
})
export class OdontologiaModule { }
