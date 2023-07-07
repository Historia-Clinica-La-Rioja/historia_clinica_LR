import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnfermeriaComponent } from './enfermeria.component';

describe('EnfermeriaComponent', () => {
  let component: EnfermeriaComponent;
  let fixture: ComponentFixture<EnfermeriaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnfermeriaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnfermeriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
