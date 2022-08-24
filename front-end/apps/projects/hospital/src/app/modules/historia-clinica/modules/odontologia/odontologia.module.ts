import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';

// deps
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { PresentationModule } from '@presentation/presentation.module';
// components
import { HidableScrollableDataComponent } from './components/hidable-scrollable-data/hidable-scrollable-data.component';
import { IndicesComponent } from './components/indices/indices.component';
import { OdontogramComponent } from './components/odontogram/odontogram.component';
import { OdontologyConsultationDockPopupComponent } from './components/odontology-consultation-dock-popup/odontology-consultation-dock-popup.component';
import { ToothComponent } from './components/tooth/tooth.component';
import { ToothDialogComponent } from './components/tooth-dialog/tooth-dialog.component';
// dialogs
import { NewConsultationAddDiagnoseFormComponent } from './dialogs/new-consultation-add-diagnose-form/new-consultation-add-diagnose-form.component';
import { NewConsultationPersonalHistoryFormComponent } from './dialogs/new-consultation-personal-history-form/new-consultation-personal-history-form.component';
// services
import { ConceptsFacadeService } from './services/concepts-facade.service';

@NgModule({
	declarations: [
		// components
		HidableScrollableDataComponent,
		IndicesComponent,
		OdontogramComponent,
		OdontologyConsultationDockPopupComponent,
		ToothComponent,
		ToothDialogComponent,
		// dialogs
		NewConsultationAddDiagnoseFormComponent,
		NewConsultationPersonalHistoryFormComponent,
	],
	imports: [
		CommonModule,
		ReactiveFormsModule,
		FlexModule,
		FlexLayoutModule,
		// deps
		HistoriaClinicaModule,
		PresentationModule,
	],
	exports: [
		ToothComponent,
		OdontogramComponent,
	],
	providers: [ConceptsFacadeService]
})
export class OdontologiaModule { }
