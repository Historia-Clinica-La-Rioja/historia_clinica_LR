import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { FactorDeRiesgoCurrentPreviousComponent } from './factor-de-riesgo-current-previous.component';

import { FactorDeRiesgoComponent } from '../factor-de-riesgo-current/factor-de-riesgo.component';

const meta: Meta<FactorDeRiesgoCurrentPreviousComponent> = {
	title: 'Example/FactorDeRiesgoCurrentPreviousComponent',
	component: FactorDeRiesgoCurrentPreviousComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			declarations: [FactorDeRiesgoComponent, ]
		}),
	],
};

export default meta;
type Story = StoryObj<FactorDeRiesgoCurrentPreviousComponent>;

export const Custom: Story = {
	args: {
		riskFactor: {
			description: 'Titulo',
			currentValue: {
				value: 2,
				effectiveTime: new Date()
			},
			previousValue: {
				value: 1,
				effectiveTime: new Date()
			}
		},
	},
}
