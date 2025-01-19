import {
	DateTimeDto,
	InstitutionReportQueuedDto,
	ReportQueuedDto,
} from '@api-rest/api-model';

import { ReportStatus, initStatus } from './report-queue.utils';

const ANY_DATE_TIME: DateTimeDto = { date: { day: 29, month: 4, year: 1996 }, time: { hours: 10, minutes: 30 } };

const newReportAsPending = (): InstitutionReportQueuedDto => {
	return newInstitutionReportQueuedDto(
		{createdOn: ANY_DATE_TIME} as ReportQueuedDto
	);
}

const newReportAsGenerated = (): InstitutionReportQueuedDto => {
	return newInstitutionReportQueuedDto(
	 	{existsFile: true, generatedOn: ANY_DATE_TIME} as ReportQueuedDto
	);
}

const newReportAsError = (): InstitutionReportQueuedDto => {
	return newInstitutionReportQueuedDto(
		{createdOn: ANY_DATE_TIME, generatedOn: ANY_DATE_TIME, generatedError: 'Error generando 2'} as ReportQueuedDto
	);
}

const newInstitutionReportQueuedDto = (report: ReportQueuedDto) => {
	return {report, id: 3} as InstitutionReportQueuedDto;
}

const validateEmpty = (status: ReportStatus) => {
	expect(status.stage).toBe('EMPTY');
	expect(status.inProcess).toBeUndefined();
	expect(status.toDownload).toBeUndefined();
}

const validateAsk = (status: ReportStatus) => { // descargar o reload
	expect(status.stage).toBe('ASK');
	expect(status.inProcess).toBeUndefined();
	expect(status.toDownload).toBeDefined();
}

const validateReloadPending = (status: ReportStatus) => {
	expect(status.stage).toBe('RELOAD');
	expect(status.inProcess).toBeDefined();
	expect(status.toDownload).toBeUndefined(); // espera sin poder descargar
}

const validateReload = (status: ReportStatus) => {
	expect(status.stage).toBe('RELOAD');
	expect(status.inProcess).toBeDefined();
	expect(status.toDownload).toBeDefined(); // espera pero puede descargar
}

describe('initStatus should be', () => {

	it(`EMPTY when queue is empty`, () => {
		validateEmpty(initStatus([]));
	});

	it(`ASK when queue has only generated reports`, () => {
		validateAsk(initStatus([newReportAsGenerated()]));
		validateAsk(initStatus([newReportAsGenerated(), newReportAsGenerated()]));
	});

	it(`RELOAD when queue has only one requested report`, () => {
		validateReloadPending(initStatus([newReportAsPending()]));
	});

	it(`RELOAD when queue has one generated and one requested`, () => {
		validateReload(initStatus([newReportAsGenerated(), newReportAsPending()]));
		validateReload(initStatus([newReportAsPending(), newReportAsGenerated()]));
	});

	it(`EMPTY when queue has only reports not generated due to some error`, () => {
		validateEmpty(initStatus([newReportAsError()]));
		validateEmpty(initStatus([newReportAsError(), newReportAsError()]));
	});

})
