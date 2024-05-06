import { Component, Input } from '@angular/core';
import { EmergencyCareEvolutionNoteDocumentDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientProcedureDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { HEALTH_VERIFICATIONS } from '../../../ambulatoria/modules/internacion/constants/ids';
@Component({
	selector: 'app-emergency-care-evolution-note',
	templateUrl: './emergency-care-evolution-note.component.html',
	styleUrls: ['./emergency-care-evolution-note.component.scss']
})
export class EmergencyCareEvolutionNoteComponent {


	@Input() set content(newContent: EmergencyCareEvolutionNoteDocumentDto) {
		console.log(newContent);
		this.internacionMasterDataService.getAllergyCriticality().subscribe(
			c => {
				this.criticalityTypes = c;
				this.alergiasContent = this.toAlergias(newContent.emergencyCareEvolutionNoteClinicalData.allergies);
				if (newContent.emergencyCareEvolutionNoteClinicalData.allergies.isReferred === false)
					this.alergiasContent = ['No refiere alergias'];
			}
		)
		this.especialidadContent = [newContent.clinicalSpecialtyName];
		this.motivosContent = newContent.emergencyCareEvolutionNoteClinicalData.reasons.map(r => r.snomed.pt)
		this.diagnosticosContent = this.toDiagnostico(newContent.emergencyCareEvolutionNoteClinicalData.mainDiagnosis, newContent.emergencyCareEvolutionNoteClinicalData.diagnosis)
		this.evolucionContent = newContent.emergencyCareEvolutionNoteClinicalData.evolutionNote ? [newContent.emergencyCareEvolutionNoteClinicalData.evolutionNote] : null;
		this.antropometricosContent = this.toAntropometricosContent(newContent.emergencyCareEvolutionNoteClinicalData.anthropometricData);
		this.medicacionContent = this.toMedications(newContent.emergencyCareEvolutionNoteClinicalData.medications);
		this.procedimientosContent = this.toProcedimientos(newContent.emergencyCareEvolutionNoteClinicalData.procedures);
		this.factoresContent = this.toRiskFactors(newContent.emergencyCareEvolutionNoteClinicalData.riskFactors);
		this.antecedentesFamiliaresContent = this.toAntecedentesFamiliares(newContent.emergencyCareEvolutionNoteClinicalData.familyHistories)
	}

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

	private toAntecedentesFamiliares(familyHistories): string[] {
		if (familyHistories.isReferred === false)
			this.antecedentesFamiliaresContent = ['No refiere antecedentes familiares'];
		return familyHistories.referredContent.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);

		function map(m: OutpatientFamilyHistoryDto): string[] {
			return [m.snomed.pt, m.startDate ? `Desde ${m.startDate}`: null]
		}
	}

	private toAntropometricosContent(anthropometricData): string[] {
		const result = [];
		const bloodValue = anthropometricData?.bloodType?.value;
		if (bloodValue) {
			result.push(`Grupo y factor sanguineo: ${bloodValue}`)
		}
		const height = anthropometricData?.height?.value;
		if (height) {
			result.push(`Altura: ${height}cm`)
		}
		const weight = anthropometricData?.weight?.value;
		if (weight) {
			result.push(`Peso: ${weight}Kg`)
		}
		const headCircumference = anthropometricData?.headCircumference?.value;
		if (headCircumference) {
			result.push(`Perímetro cefálico: ${headCircumference}cm`)
		}
		return result;
	}

	private toMedications(medications): string[] {
		return medications.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);;

		function map(m: OutpatientMedicationDto): string[] {
			return [m.snomed.pt, m.note]
		}

	}

	private toProcedimientos(procedures): string[] {
		return procedures.map(map).reduce((acumulado, actual) => acumulado.concat(actual), []);;

		function map(m: OutpatientProcedureDto): string[] {
			return [m.snomed.pt, m.performedDate]
		}
	}

	private toRiskFactors(riskFactors): string[] {
		const result = [];
		const heartRate = riskFactors?.heartRate
		if (heartRate) {
			result.push(`Frecuencia cardíaca : ${heartRate.value}/min`)
		}
		const respiratoryRate = riskFactors?.respiratoryRate
		if (respiratoryRate) {
			result.push(`Frecuencia respiratoria: ${respiratoryRate.value}/min`)
		}
		const temperature = riskFactors?.temperature
		if (temperature) {
			result.push(`Temperatura: ${temperature.value}°`)
		}
		const bloodOxygenSaturation = riskFactors?.bloodOxygenSaturation
		if (bloodOxygenSaturation) {
			result.push(`Saturación de oxigeno: ${bloodOxygenSaturation.value}%`)
		}
		const systolicBloodPressure = riskFactors?.systolicBloodPressure;
		if (systolicBloodPressure) {
			result.push(`Tension arterial sistólica: ${systolicBloodPressure.value}mm`)
		}
		const diastolicBloodPressure = riskFactors?.diastolicBloodPressure;
		if (diastolicBloodPressure) {
			result.push(`Tension arterial diastólica: ${diastolicBloodPressure.value}mm`)
		}
		const bloodGlucose = riskFactors?.bloodGlucose;
		if (bloodGlucose) {
			result.push(`Glucemia: ${bloodGlucose.value}mg/dl`)
		}
		const glycosylatedHemoglobin = riskFactors?.glycosylatedHemoglobin;
		if (glycosylatedHemoglobin) {
			result.push(`Hemoglobina glicosilada: ${glycosylatedHemoglobin.value}%`)
		}

		const cardiovascularRisk = riskFactors?.cardiovascularRisk;
		if (cardiovascularRisk) {
			result.push(`Riesgo cardivascular: ${cardiovascularRisk.value}%`)
		}
		return result;
	}

	private toAlergias(allergies): string[] {
		return allergies.content.map(a => `${a.snomed.pt} - ${this.criticalityTypes.find(c => c.id === a.criticalityId).display}`);
	}

	private toDiagnostico(mainDiagnosis, otherDiagnosis): string[] {

		const principal = [`${mainDiagnosis.snomed.pt} (Principal) - ${getVerification(mainDiagnosis.verificationId)}`]
		const others = otherDiagnosis.map(r => `${r.snomed.pt} - ${getVerification(r.verificationId)}`);

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
