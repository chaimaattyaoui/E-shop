import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-view-orderd-products',
  templateUrl: './view-orderd-products.component.html',
  styleUrls: ['./view-orderd-products.component.css']
})
export class ViewOrderdProductsComponent {

  orderId: any = this.activatedroute.snapshot.params['orderId'];
  orderedProductDetailsList = [];
  totalAmount:any;

    constructor(private customerService: CustomerService,
      private activatedroute: ActivatedRoute,
    ){}

    ngOnInit(){
      this.getOrderedProductsDetailsByOrderedId();
    }

    getOrderedProductsDetailsByOrderedId(){
      this.customerService.getOrderedProducts(this.orderId).subscribe(res=>{
        res.productDtoList.forEach(element =>{
          element.processedImg = element.imageUrl;


          this.orderedProductDetailsList.push(element);
        });
        this.totalAmount = res.orderAmount;
      })
    }


}

