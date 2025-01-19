

export class ChartDataService {
	showGroupSmallData: boolean;

	// attrs only for pie chart
	private originalGroupData: any[] = [];
	private groupChartLabels: string[] = [];

	private groupSmallData: boolean;

	constructor(
		private originalChartLabels: string[],
		private originalData: any[], //
		private updateData: (args: {labels, data: any[]}) => void
	) {
		this.loadPieData();
	}

	private loadPieData() {
		this.groupSmallData = false;

		const pieSum = this.originalData.reduce((partialSum, currentValue) => partialSum + currentValue, 0);
		const percentageData = this.originalData.map((element) => Math.round((element * 100 / pieSum) * 100) / 100);
		const smallDataIndex = percentageData.findIndex(x => x < 1);
		this.showGroupSmallData = smallDataIndex > 1;
		if (this.showGroupSmallData) {
			this.originalGroupData = this.groupedData(this.originalData, smallDataIndex);
			this.groupChartLabels = this.originalChartLabels.slice(0, smallDataIndex);
			this.groupChartLabels.push('Otros');
		}
	}

	private groupedData(data: number[], index: number): number[] {
		const others = data.slice(index);
		let othersSum = others.reduce((partialSum, currentValue) => partialSum + currentValue, 0);
		return [...data.slice(0, index), othersSum];
	}

	toggleGroupSmallData() {
		this.groupSmallData = !this.groupSmallData;
		if (this.groupSmallData) {
			this.updateData({
				labels: this.groupChartLabels,
				data: this.originalGroupData,
			});
		}
		else {
			this.updateData({
				labels: this.originalChartLabels,
				data: this.originalData,
			});
		}
	}

}
