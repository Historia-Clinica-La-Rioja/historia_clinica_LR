//imports
import { NursingRecordFacadeService } from './services/nursing-record-facade.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms";
import { PresentationModule } from "@presentation/presentation.module";
//standalone componentes
import { PatientSummaryComponent } from 'projects/hospital/src/app/modules/hsi-components/patient-summary/patient-summary.component';
//components
import { CardMedicacionesComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/card-medicaciones/card-medicaciones.component";
import { CardPharmacoComponent } from './components/card-pharmaco/card-pharmaco.component';
import { DayDisplayComponent } from './components/day-display/day-display.component';
import { NursingCareComponent } from './components/nursing-care/nursing-care.component';
import { NursingRecordComponent } from './components/nursing-record/nursing-record.component';
import { InternmentIndicationsCardComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/internment-indications-card/internment-indications-card.component";
import { ItemPrescripcionesComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/components/item-prescripciones/item-prescripciones.component";
import { InternmentDietCardComponent } from './components/internment-diet-card/internment-diet-card.component';
import { InternmentPharmacoCardComponent } from './components/internment-pharmaco-card/internment-pharmaco-card.component';
import { InternmentOtherIndicationCardComponent } from './components/internment-other-indication-card/internment-other-indication-card.component';
import { InternmentParenteralPlanCardComponent } from './components/internment-parenteral-plan-card/internment-parenteral-plan-card.component';
import { NuevaPrescripcionComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/nueva-prescripcion/nueva-prescripcion.component";
import { MostFrequentComponent } from './dialogs/most-frequent/most-frequent.component';
import { SuspenderMedicacionComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/suspender-medicacion/suspender-medicacion.component";
import { DietComponent } from "@historia-clinica/modules/ambulatoria/modules/indicacion/dialogs/diet/diet.component";
import { OtherIndicationComponent } from './dialogs/other-indication/other-indication.component';
import { ParenteralPlanComponent } from './dialogs/parenteral-plan/parenteral-plan.component';
import { HistoriaClinicaModule } from '@historia-clinica/historia-clinica.module';
import { PharmacoComponent } from './dialogs/pharmaco/pharmaco.component';
import { GeneralNursingRecordComponent } from './components/general-nursing-record/general-nursing-record.component';
import { SpecificNursingRecordComponent } from './components/specific-nursing-record/specific-nursing-record.component';
import { RegisterNursingRecordComponent } from './dialogs/register-nursing-record/register-nursing-record.component';
import { RelativeDatePipe } from './pipes/relative-date.pipe';
import { InternmentIndicationDetailComponent } from './dialogs/internment-indication-detail/internment-indication-detail.component';
import { PrescripcionValidatorPopupComponent } from './dialogs/prescripcion-validator-popup/prescripcion-validator-popup.component';
import { WarningMessageComponent } from './components/warning-message/warning-message.component';
import { ShowViaPipe } from './pipes/show-via.pipe';
import { EmergencyCareIndicationsCardComponent } from './components/emergency-care-indications-card/emergency-care-indications-card.component';
import { PharmacosFrequentComponent } from './dialogs/pharmacos-frequent/pharmacos-frequent.component';
import { PatientInformationComponent } from './components/patient-information/patient-information.component';
import { PrescriptionInformationComponent } from './components/prescription-information/prescription-information.component';
import { MedicationInformationComponent } from './components/medication-information/medication-information.component';
import { CommercialPharmacoTypeaheadComponent } from './components/commercial-pharmaco-typeahead/commercial-pharmaco-typeahead.component';

@NgModule({
	declarations: [
		CardMedicacionesComponent,
		CardPharmacoComponent,
		DayDisplayComponent,
		GeneralNursingRecordComponent,
		ItemPrescripcionesComponent,
		InternmentIndicationsCardComponent,
		InternmentDietCardComponent,
		InternmentPharmacoCardComponent,
		InternmentOtherIndicationCardComponent,
		InternmentParenteralPlanCardComponent,
		MostFrequentComponent,
		NuevaPrescripcionComponent,
		NursingCareComponent,
		NursingRecordComponent,
		SpecificNursingRecordComponent,
		SuspenderMedicacionComponent,
		ShowViaPipe,
		DietComponent,
		OtherIndicationComponent,
		ParenteralPlanComponent,
		PharmacosFrequentComponent,
		PharmacoComponent,
		RegisterNursingRecordComponent,
		RelativeDatePipe,
		InternmentIndicationDetailComponent,
		PrescripcionValidatorPopupComponent,
		WarningMessageComponent,
		EmergencyCareIndicationsCardComponent,
		PatientInformationComponent,
		PrescriptionInformationComponent,
		MedicationInformationComponent,
		CommercialPharmacoTypeaheadComponent,
	],
	exports: [
		CardMedicacionesComponent,
		ItemPrescripcionesComponent,
		InternmentIndicationsCardComponent,
		EmergencyCareIndicationsCardComponent,
		NursingCareComponent,
		CommercialPharmacoTypeaheadComponent
	],
	imports: [
		CommonModule,
		FormsModule,
		PresentationModule,
		HistoriaClinicaModule,
		//Standalone Component
		PatientSummaryComponent
	],
	providers: [
		NursingRecordFacadeService
	]
})
export class IndicacionModule { }
