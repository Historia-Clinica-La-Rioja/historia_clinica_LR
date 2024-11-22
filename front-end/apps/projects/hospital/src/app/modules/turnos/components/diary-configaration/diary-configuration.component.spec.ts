import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiaryConfigurationComponent } from './diary-configuration.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { By } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

describe('DiaryConfigarationComponent', () => {
	let component: DiaryConfigurationComponent;
	let fixture: ComponentFixture<DiaryConfigurationComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [DiaryConfigurationComponent],
			imports: [
				MatSelectModule,
				BrowserAnimationsModule,
				ReactiveFormsModule,
				MatFormFieldModule,
				MatInputModule,
				MatSelectModule,
				BrowserAnimationsModule
			]
		})
			.compileComponents();

		fixture = TestBed.createComponent(DiaryConfigurationComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should show input daysRange', async () => {

		const matSelect = fixture.debugElement.query(By.css('mat-select')).nativeElement
		matSelect.click();
		fixture.detectChanges();

		await fixture.whenStable();
		fixture.detectChanges();

		const options = document.querySelectorAll('mat-option');
		const selectedOption = Array.from(options)
		.find(option => option.textContent?.trim() === 'HABILITACION POR RANGO DE DIAS');
		if (selectedOption) {
			(selectedOption as HTMLElement).click()
			fixture.detectChanges();

			const inputElement = fixture.debugElement.query(By.css('input'))
			expect(inputElement).toBeTruthy();
		}
	});

	it('should display a component', () => {
        expect(component).toBeTruthy();
    });
});
