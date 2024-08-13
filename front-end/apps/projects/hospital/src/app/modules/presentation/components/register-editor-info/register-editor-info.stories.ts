import { Meta, StoryObj, moduleMetadata } from "@storybook/angular";
import { REGISTER_EDITOR_CASES, RegisterEditorInfoComponent } from "./register-editor-info.component";
import { ReactiveFormsModule } from "@angular/forms";

const meta: Meta<RegisterEditorInfoComponent> = {
	title: 'Presentation/RegisterEditorInfoComponent',
	component: RegisterEditorInfoComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [ReactiveFormsModule],
		}),
	],
};

export default meta;


type Story = StoryObj<RegisterEditorInfoComponent>;

export const Name: Story = {
	args: {
		registerEditor: {
			createdBy: 'Jose Perez',
		}
	}
}

export const NameDate: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE,
		registerEditor: {
			createdBy: 'Jose Perez',
			date: new Date("2024-03-25")
		}
	}
}

export const NameDateHours: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE_HOUR,
		registerEditor: {
			createdBy: 'Jose Perez',
			date: new Date("October 13, 2024 11:13:00")

		}
	}
}

export const NameInstitutionDate: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE,
		registerEditor: {
			createdBy: 'Jose Perez',
			institution: 'Hospital Ramon Santamarina',
			date: new Date("2024-03-25")
		}
	}
}

export const NameInstitutionDateHours: Story = {
	args: {
		registerEditorCase: REGISTER_EDITOR_CASES.DATE_HOUR,
		registerEditor: {
			createdBy: 'Jose Perez',
			institution: 'Hospital Ramon Santamarina',
			date: new Date("October 13, 2024 11:13:00")
		}
	}
}


