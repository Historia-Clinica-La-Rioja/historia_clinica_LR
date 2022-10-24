import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralesComponent } from './generales.component';

describe('GeneralesComponent', () => {
  let component: GeneralesComponent;
  let fixture: ComponentFixture<GeneralesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GeneralesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GeneralesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
