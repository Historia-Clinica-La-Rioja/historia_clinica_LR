import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DateOnlyIsoFormInputComponent } from './date-only-iso-form-input.component';

describe('DateOnlyIsoFormInputComponent', () => {
  let component: DateOnlyIsoFormInputComponent;
  let fixture: ComponentFixture<DateOnlyIsoFormInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DateOnlyIsoFormInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DateOnlyIsoFormInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
