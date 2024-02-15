import * as moment from "moment";
import { toApiFormat, toFileFormat } from "./date.mapper";
import { DateFormat, momentFormat } from "@core/utils/moment.utils";

const ISODate = `2000-01-01T01:30:00.000-05:00`;
const date = new Date(ISODate);

/*
 Al momento de generar el date no se sabe en que zona horaria se crea (depende de la ejecucion),
 por lo tanto no es posible saber que dia queda como resultado.
 Como consecuencia, al ser metodos de reemplazo, estos tests aseguran que las funciones retornen los mismos
 resultados que las que se buscan reemplazar.
*/

describe('toApiFormat', () => {

	it('should replace momentFormat(date, DateFormat.API_DATE);', () => {
		const apiFormatedDate = toApiFormat(date);
		const momentInstance = moment(date);
		const prevResult = momentFormat(momentInstance, DateFormat.API_DATE);
		expect(apiFormatedDate).toBe(prevResult)
	});

	it('should replace .format(DateFormat.API_DATE) method from moment', () => {
		const apiFormatedDate = toApiFormat(date);
		const momentInstance = moment(date);
		const prevResult = momentInstance.format(DateFormat.API_DATE)
		expect(apiFormatedDate).toBe(prevResult)
	});

})

describe('toFileFormat', () => {

	it('should replace momentFormat(date, DateFormat.FILE_DATE)', () => {
		const fileFormatedDate = toFileFormat(date);
		const momentInstance = moment(date);
		const prevResult = momentFormat(momentInstance, DateFormat.FILE_DATE);
		expect(fileFormatedDate).toBe(prevResult)
	});

})
