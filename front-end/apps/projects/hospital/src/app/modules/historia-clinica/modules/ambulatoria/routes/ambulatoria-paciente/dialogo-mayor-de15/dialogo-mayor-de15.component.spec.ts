import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogoMayorDe15Component } from './dialogo-mayor-de15.component';

describe('DialogoMayorDe15Component', () => {
  let component: DialogoMayorDe15Component;
  let fixture: ComponentFixture<DialogoMayorDe15Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogoMayorDe15Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogoMayorDe15Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
