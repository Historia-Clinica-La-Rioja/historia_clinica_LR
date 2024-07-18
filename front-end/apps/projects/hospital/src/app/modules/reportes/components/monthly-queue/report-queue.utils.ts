import { InstitutionReportQueuedDto } from '@api-rest/api-model';

type PageStage = 'RELOAD' | 'ASK' | 'EMPTY' | 'DOWNLOAD';

export interface ReportStatus {
	stage: PageStage;
	toDownload?: InstitutionReportQueuedDto;
	inProcess?: InstitutionReportQueuedDto;
}

export const initStatus = (list: InstitutionReportQueuedDto[]): ReportStatus => {
	const toDownload = list.find(
		rq => rq.report.existsFile
	);
	const inProcess = list.find(
		rq => !rq.report.generatedOn
	);
	if (inProcess) {
		return {stage: 'RELOAD', inProcess, toDownload};
	}
	if (toDownload) {
		return {stage: 'ASK', toDownload};
	}
	return {stage: 'EMPTY'};
};

export const refreshStatus = (list: InstitutionReportQueuedDto[]): ReportStatus => {
	const toDownload = list.find(
		rq => rq.report.existsFile
	);
	const inProcess = list.find(
		rq => !rq.report.generatedOn
	);
	if (inProcess) {
		return {stage: 'RELOAD', inProcess, toDownload};
	}
	if (toDownload) {
		return {stage: 'DOWNLOAD', toDownload};
	}
	return {stage: 'EMPTY'};
};
