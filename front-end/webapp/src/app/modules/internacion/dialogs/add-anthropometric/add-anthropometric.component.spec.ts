import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAnthropometricComponent } from './add-anthropometric.component';

describe('AddAnthropometricComponent', () => {
  let component: AddAnthropometricComponent;
  let fixture: ComponentFixture<AddAnthropometricComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddAnthropometricComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAnthropometricComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
