export enum EstadosEpisodio {
	EN_ATENCION = 1,
	EN_ESPERA = 2,
	CON_ALTA_ADMINISTRATIVA = 3,
	CON_ALTA_MEDICA = 4,
	AUSENTE = 5,
	LLAMADO = 6
}

export enum Triages {
	ROJO_NIVEL_1 = 1,
	NARANJA_NIVEL_2 = 2,
	AMARILLO_NIVEL_3 = 3,
	VERDE_NIVEL_4 = 4,
	AZUL_NIVEL_5 = 5,
	GRIS_SIN_TRIAGE = 6
}

export enum EmergencyCareTypes {
	NOT_DEFINED = -1,
	ADULTO = 1,
	PEDIATRIA = 2,
	GINECOLOGIA = 3
}

export const SECTOR_AMBULATORIO = 1;

export const SECTOR_GUARDIA = 3;

export const TRIAGE_LEVEL_V_ID = 5;

export const INTERNMENT_SECTOR = 2;
