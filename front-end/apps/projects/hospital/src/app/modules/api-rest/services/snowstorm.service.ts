import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { map } from 'rxjs/operators';
import { SnomedDto, SnomedResponseDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';

export const SNOMED_RESULTS_LIMIT = '30';

const REPORTABLE_SNOMED_CONCEPTS = [
	{
		parentFsn: "",
		parentId: "",
		pt: "dengue",
		sctid: "38362002",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre hemorrágica dengue",
		sctid: "20927009",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "sospecha de dengue",
		sctid: "418429061000132101",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "dengue sin signos de alarma",
		sctid: "722862003",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "dengue con signos de alarma",
		sctid: "722863008",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "síndrome de shock por Dengue",
		sctid: "409671005",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "mielitis causada por virus Dengue",
		sctid: "866057003",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "anticuerpo anti-dengue, IgM, negativo",
		sctid: "409692005",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "anticuerpo anti-dengue, inmunoglobulina M, positivo",
		sctid: "409693000",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "anticuerpo anti-dengue, inmunoglobulina G, negativo",
		sctid: "409694006",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "anticuerpo anti-dengue, inmunoglobulina G, positivo",
		sctid: "409695007",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre hemorrágica por Dengue, grado I",
		sctid: "409676000",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre hemorrágica por Dengue, grado II",
		sctid: "409677009",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre hemorrágica por Dengue, grado IV",
		sctid: "409679007",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre hemorrágica por Dengue, grado III",
		sctid: "409678004",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "coronavirus 2 del síndrome respiratorio agudo severo no detectado",
		sctid: "1240591000000102",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "exposición a COVID-19",
		sctid: "840546002",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "COVID-19 post-agudo",
		sctid: "1119303003",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "coronavirus 2 del síndrome respiratorio agudo severo detectado",
		sctid: "1240581000000104",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "enfermedad aguda causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "1119302008",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "coronavirus 2 del síndrome respiratorio agudo severo detectado",
		sctid: "1240581000000104",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "COVID-19 post-agudo",
		sctid: "1119303003",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "exposición a COVID-19",
		sctid: "840546002",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "coronavirus 2 del síndrome respiratorio agudo severo no detectado",
		sctid: "1240591000000102",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "antecedente de enfermedad causada por SARS-CoV-2",
		sctid: "292508471000119105",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "disnea causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "119981000146107",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "fiebre causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "119751000146104",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "neumonía causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "882784691000119100",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "síndrome crónico post-COVID-19",
		sctid: "1119304009",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "enfermedad causada por COVID-19",
		sctid: "840539006"	,
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "conjuntivitis debida a enfermedad causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "119741000146102",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacuna contra SARS-CoV-2 rechazada",
		sctid: "1145031003",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacuna contra SARS-CoV-2 rechazada",
		sctid: "1145033000",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "linfocitopenia debida a coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "866151004",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "exposición ocupacional al SARS-CoV-2",
		sctid: "897036007",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "trombocitopenia debida a coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "866152006",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "infección asintomática por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "189486241000119100",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "lesión renal aguda debida a enfermedad causada por SARS-CoV-2",
		sctid: "870589006",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacuna contra SARS-CoV-2 contraindicada",
		sctid: "1145026000",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacuna contra SARS-CoV-2 contraindicada",
		sctid: "1145023008",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "bronquitis aguda causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "138389411000119105",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacunación contra SARS-CoV-2 no indicada",
		sctid: "1145028004",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "con mayor riesgo de exposición a SARS-CoV-2",
		sctid: "870577009",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "reacción adversa a vacuna contra SARS-CoV-2",
		sctid: "1142180003",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "sospecha de enfermedad causada por COVID-19",
		sctid: "840544004",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "reacción adversa a vacuna ARNm contra SARS-CoV-2",
		sctid: "1142181004",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "hipersensibilidad a vacuna ARNm contra SARS-CoV-2",
		sctid: "1145003007",
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "sepsis debida a enfermedad causada por coronavirus 2 del síndrome respiratorio agudo severo",
		sctid: "870588003",	
	},
	{
		parentFsn: "",
		parentId: "",
		pt: "vacunación con vacuna contra SARS-CoV-2 no indicada",
		sctid: "1145030002"	,
	},
];
@Injectable({
	providedIn: 'root'
})
export class SnowstormService {

	constructor(
		private readonly http: HttpClient
	) { }

	getSNOMEDConcepts(params): Observable<SnomedResponseDto> {
		const url = `${environment.apiBase}/snowstorm/concepts`;
		return this.http.get<any>(url, { params }).pipe(map(results => {
			if (results) {
				const newItems = results.items.map((i: any): SnomedDto => {
					return {
						sctid: i.conceptId,
						pt: i.pt.term,
						// TODO no llegan las siguientes propiedades desde este endpoint de snowstorm
						parentFsn: '',
						parentId: ''
					};
				});
				results.items = newItems;
				return results;
			}
			else {
				throw Error('SNOMED concepts could not be obtained.');
			}
		}));
	}

	getIsReportable(params): Observable<boolean> {
		//TODO const url = `${environment.apiBase}/snowstorm/is-reportable`;
		const result = REPORTABLE_SNOMED_CONCEPTS.find(snomedDto => snomedDto.sctid === params.sctid && snomedDto.pt === params.pt);
		return result ? of(true) : of(false);
	}

}

export interface ConceptRequestParams {
	term: string;
	ecl?: string;
}
