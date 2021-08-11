import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './routes/home/home.component';
import { AmbulatoriaRoutingModule } from './ambulatoria-routing.module';
import { FormsModule } from '@angular/forms';
import { CoreModule } from '@core/core.module';
import { PacientesModule } from '../../../pacientes/pacientes.module';
import { PresentationModule } from '@presentation/presentation.module';
import { PatientProfileComponent } from './routes/patient-profile/patient-profile.component';
import { AmbulatoriaPacienteComponent } from './routes/ambulatoria-paciente/ambulatoria-paciente.component';
import { ResumenComponent } from './components/resumen/resumen.component';
import { ProblemasComponent } from './components/problemas/problemas.component';
import { VacunasComponent } from './components/vacunas/vacunas.component';
import { HistoriaClinicaModule } from '../../historia-clinica.module';
import { AplicarVacunaComponent } from './dialogs/aplicar-vacuna/aplicar-vacuna.component';
import { AppMaterialModule } from '../../../material/app.material.module';
import { SolveProblemComponent } from '../../dialogs/solve-problem/solve-problem.component';
import { HistoricalProblemsFiltersComponent } from './components/historical-problems-filters/historical-problems-filters.component';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
import { NuevaConsultaDockPopupComponent } from './dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { OrdenesComponent } from './components/ordenes/ordenes.component';
import { NuevaPrescripcionComponent } from './dialogs/ordenes-prescripciones/nueva-prescripcion/nueva-prescripcion.component';
import { ConfirmarPrescripcionComponent } from './dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { AgregarPrescripcionItemComponent } from './dialogs/ordenes-prescripciones/agregar-prescripcion-item/agregar-prescripcion-item.component';
import { SuspenderMedicacionComponent } from './dialogs/ordenes-prescripciones/suspender-medicacion/suspender-medicacion.component';
import { CardMedicacionesComponent } from './components/ordenes/card-medicaciones/card-medicaciones.component';
import { CardEstudiosComponent } from './components/ordenes/card-estudios/card-estudios.component';
import { CardIndicacionesComponent } from './components/ordenes/card-indicaciones/card-indicaciones.component';
import { ItemPrescripcionesComponent } from './components/ordenes/item-prescripciones/item-prescripciones.component';
import { CompletarEstudioComponent } from './dialogs/ordenes-prescripciones/completar-estudio/completar-estudio.component';
import { VerResultadosEstudioComponent } from './dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';
import { ExternalSummaryCardComponent } from '@presentation/components/external-summary-card/external-summary-card.component';
import { ConfirmarNuevaConsultaPopupComponent } from './dialogs/confirmar-nueva-consulta-popup/confirmar-nueva-consulta-popup.component';
import { OdontologiaModule } from '../odontologia/odontologia.module';
import { AgregarVacunaComponent } from './dialogs/agregar-vacuna/agregar-vacuna.component';
import { AgregarVacunasComponent } from './dialogs/agregar-vacunas/agregar-vacunas.component';
import { SuccesMessageDialogComponent } from './dialogs/succes-message-dialog/succes-message-dialog.component';
import { DetalleVacunaComponent } from './dialogs/detalle-vacuna/detalle-vacuna.component';


@NgModule({
	declarations: [
		HomeComponent,
		PatientProfileComponent,
		AmbulatoriaPacienteComponent,
		ResumenComponent,
		ProblemasComponent,
		VacunasComponent,
		AplicarVacunaComponent,
		SolveProblemComponent,
		HistoricalProblemsFiltersComponent,
		NuevaConsultaDockPopupComponent,
		OrdenesComponent,
		NuevaPrescripcionComponent,
		ConfirmarPrescripcionComponent,
		AgregarPrescripcionItemComponent,
		SuspenderMedicacionComponent,
		CardMedicacionesComponent,
		CardEstudiosComponent,
		CardIndicacionesComponent,
		ItemPrescripcionesComponent,
		CompletarEstudioComponent,
		VerResultadosEstudioComponent,
		ExternalSummaryCardComponent,
		ConfirmarNuevaConsultaPopupComponent,
		AgregarVacunasComponent,
		AgregarVacunaComponent,
		SuccesMessageDialogComponent,
		DetalleVacunaComponent
	],
	imports: [
		AmbulatoriaRoutingModule,
		AppMaterialModule,
		CommonModule,
		CoreModule,
		FormsModule,
		HistoriaClinicaModule,
		PacientesModule,
		PresentationModule,
		OverlayModule,
		PortalModule,
		OdontologiaModule
	]
})
export class AmbulatoriaModule {
}
