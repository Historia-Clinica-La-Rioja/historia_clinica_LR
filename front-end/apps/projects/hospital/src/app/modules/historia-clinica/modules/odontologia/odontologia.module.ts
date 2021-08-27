import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToothComponent } from './components/tooth/tooth.component';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
import { AppMaterialModule } from '@material/app.material.module';
import { OdontogramComponent } from './components/odontogram/odontogram.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ToothDialogComponent } from './components/tooth-dialog/tooth-dialog.component';
import { ConceptsFacadeService } from './services/concepts-facade.service';
import { PresentationModule } from '@presentation/presentation.module';
import { HidableScrollableDataComponent } from './components/hidable-scrollable-data/hidable-scrollable-data.component';



@NgModule({
	declarations: [ToothComponent, OdontogramComponent, ToothDialogComponent, HidableScrollableDataComponent],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		AppMaterialModule,
		ReactiveFormsModule,
		PresentationModule
	],
	exports: [
		ToothComponent,
		OdontogramComponent
	],
	providers: [ConceptsFacadeService]
})
export class OdontologiaModule { }
