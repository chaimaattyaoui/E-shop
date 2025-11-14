import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewOrderdProductsComponent } from './view-orderd-products.component';

describe('ViewOrderdProductsComponent', () => {
  let component: ViewOrderdProductsComponent;
  let fixture: ComponentFixture<ViewOrderdProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewOrderdProductsComponent]
    });
    fixture = TestBed.createComponent(ViewOrderdProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
