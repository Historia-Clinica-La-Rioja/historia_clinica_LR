import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';

import { CoreModule } from '@core/core.module';
import { PresentationModule } from '@presentation/presentation.module';
import { AppMaterialModule } from '@material/app.material.module';
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';

import { ToothComponent } from './components/tooth/tooth.component';
import { OdontogramComponent } from './components/odontogram/odontogram.component';
import { ToothDialogComponent } from './components/tooth-dialog/tooth-dialog.component';
import { ConceptsFacadeService } from './services/concepts-facade.service';
import { HidableScrollableDataComponent } from './components/hidable-scrollable-data/hidable-scrollable-data.component';
import { OdontologyConsultationDockPopupComponent } from './components/odontology-consultation-dock-popup/odontology-consultation-dock-popup.component';
import { IndicesComponent } from './components/indices/indices.component';

@NgModule({
	declarations: [
		ToothComponent,
		OdontogramComponent,
		ToothDialogComponent,
		HidableScrollableDataComponent,
		OdontologyConsultationDockPopupComponent,
		IndicesComponent,
	],
	imports: [
		CommonModule,
		ReactiveFormsModule,
		FlexModule,
		FlexLayoutModule,
		CoreModule,
		PresentationModule,
		AppMaterialModule,
		HistoriaClinicaModule,
	],
	exports: [
		ToothComponent,
		OdontogramComponent,
	],
	providers: [ConceptsFacadeService]
})
export class OdontologiaModule { }
