import { Component, Input, OnInit } from '@angular/core';
import { DiagnosisDto, EmergencyCareEvolutionNoteDocumentDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientProcedureDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { HEALTH_VERIFICATIONS } from '../../../ambulatoria/modules/internacion/constants/ids';
@Component({
	selector: 'app-emergency-care-evolution-note',
	templateUrl: './emergency-care-evolution-note.component.html',
	styleUrls: ['./emergency-care-evolution-note.component.scss']
})
export class EmergencyCareEvolutionNoteComponent implements OnInit {


	@Input() content: EmergencyCareEvolutionNoteDocumentDto;
	private criticalityTypes: any[];

	especialidadContent;
	motivosContent;
	diagnosticosContent;
	evolucionContent;
	antropometricosContent;
	medicacionContent;
	procedimientosContent;
	factoresContent;
	alergiasContent;
	antecedentesFamiliaresContent;

	constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService,
	) { }

	ngOnInit(): void {

		this.internacionMasterDataService.getAllergyCriticality().subscribe(
			c => {
				this.criticalityTypes = c;
				this.alergiasContent = this.toAlergias();
			}
		)
		this.especialidadContent = [this.content.clinicalSpecialtyName];
		this.motivosContent = this.content.emergencyCareEvolutionNoteClinicalData.reasons.map(r => r.snomed.pt)
		this.diagnosticosContent = this.toDiagnostico()
		this.evolucionContent = this.content.emergencyCareEvolutionNoteClinicalData.evolutionNote ? [this.content.emergencyCareEvolutionNoteClinicalData.evolutionNote] : null;
		this.antropometricosContent = this.toAntropometricosContent();
		this.medicacionContent = this.toMedications();
		this.procedimientosContent = this.toProcedimientos();
		this.factoresContent = this.toRiskFactors();
		this.antecedentesFamiliaresContent = this.toAntecedentesFamiliares()
	}

	private toAntecedentesFamiliares(): string[] {
		this.content.emergencyCareEvolutionNoteClinicalData.familyHistories

		return this.content.emergencyCareEvolutionNoteClinicalData.familyHistories.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);

		function map(m: OutpatientFamilyHistoryDto): string[] {
			return [m.snomed.pt, `Desde ${m.startDate}`]
		}
	}

	private toAntropometricosContent(): string[] {
		const result = [];
		const bloodValue = this.content.emergencyCareEvolutionNoteClinicalData.anthropometricData?.bloodType?.value;
		if (bloodValue) {
			result.push(`Grupo y factor sanguineo: ${bloodValue}`)
		}
		const height = this.content.emergencyCareEvolutionNoteClinicalData.anthropometricData?.height?.value;
		if (height) {
			result.push(`Altura: ${height}cm`)
		}
		const weight = this.content.emergencyCareEvolutionNoteClinicalData.anthropometricData?.weight?.value;
		if (weight) {
			result.push(`Peso: ${weight}Kg`)
		}
		const headCircumference = this.content.emergencyCareEvolutionNoteClinicalData.anthropometricData?.headCircumference?.value;
		if (headCircumference) {
			result.push(`Perímetro cefálico: ${headCircumference}cm`)
		}
		return result;
	}

	private toMedications(): string[] {
		const medications = this.content.emergencyCareEvolutionNoteClinicalData.medications.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);;
		return medications;

		function map(m: OutpatientMedicationDto): string[] {
			return [m.snomed.pt, m.note]
		}

	}

	private toProcedimientos(): string[] {
		return this.content.emergencyCareEvolutionNoteClinicalData.procedures.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);;

		function map(m: OutpatientProcedureDto): string[] {
			return [m.snomed.pt, m.performedDate]
		}
	}

	private toRiskFactors(): string[] {
		const result = [];
		const heartRate = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.heartRate
		if (heartRate) {
			result.push(`Frecuencia cardíaca : ${heartRate.value}/min`)
		}
		const respiratoryRate = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.respiratoryRate
		if (respiratoryRate) {
			result.push(`Frecuencia respiratoria: ${respiratoryRate.value}/min`)
		}
		const temperature = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.temperature
		if (temperature) {
			result.push(`Temperatura: ${temperature.value}°`)
		}
		const bloodOxygenSaturation = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.bloodOxygenSaturation
		if (bloodOxygenSaturation) {
			result.push(`Saturación de oxigeno: ${bloodOxygenSaturation.value}%`)
		}
		const systolicBloodPressure = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.systolicBloodPressure;
		if (systolicBloodPressure) {
			result.push(`Tension arterial sistólica: ${systolicBloodPressure.value}mm`)
		}
		const diastolicBloodPressure = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.diastolicBloodPressure;
		if (diastolicBloodPressure) {
			result.push(`Tension arterial diastólica: ${diastolicBloodPressure.value}mm`)
		}
		const bloodGlucose = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.bloodGlucose;
		if (bloodGlucose) {
			result.push(`Glucemia: ${bloodGlucose.value}mg/dl`)
		}
		const glycosylatedHemoglobin = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.glycosylatedHemoglobin;
		if (glycosylatedHemoglobin) {
			result.push(`Hemoglobina glicosilada: ${glycosylatedHemoglobin.value}%`)
		}

		const cardiovascularRisk = this.content.emergencyCareEvolutionNoteClinicalData.riskFactors?.cardiovascularRisk;
		if (cardiovascularRisk) {
			result.push(`Riesgo cardivascular: ${cardiovascularRisk.value}%`)
		}
		return result;
	}

	private toAlergias(): string[] {
		return this.content.emergencyCareEvolutionNoteClinicalData.allergies
			.map(a => `${a.snomed.pt} - ${this.criticalityTypes.find(c => c.id === a.criticalityId).display}`);
	}

	private toDiagnostico(): string[] {

		const mainDiagnosis = this.content.emergencyCareEvolutionNoteClinicalData.mainDiagnosis;
		const principal = [`${mainDiagnosis.snomed.pt} (Principal) - ${getVerification(mainDiagnosis.verificationId)}`]
		const others = this.content.emergencyCareEvolutionNoteClinicalData.diagnosis
			.map(r => `${r.snomed.pt} - ${getVerification(r.verificationId)}`);

		return principal.concat(others);

		function getVerification(verificationId: string): string {
			let verification = HEALTH_VERIFICATIONS.DESCARTADO;
			if (verificationId === HEALTH_VERIFICATIONS.CONFIRMADO) {
				verification = 'Confirmado'
			} else if (verificationId === HEALTH_VERIFICATIONS.PRESUNTIVO)
				verification = 'Presuntivo';
			return verification;
		}
	}
}
