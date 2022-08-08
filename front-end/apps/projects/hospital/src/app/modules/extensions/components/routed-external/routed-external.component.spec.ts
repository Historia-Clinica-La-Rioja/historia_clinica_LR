import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoutedExternalComponent } from './routed-external.component';

describe('RoutedExternalComponent', () => {
  let component: RoutedExternalComponent;
  let fixture: ComponentFixture<RoutedExternalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoutedExternalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RoutedExternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
