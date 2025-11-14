import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

    products:any[] = [];
    searchProductForm!: FormGroup;
  
    constructor(private customerService: CustomerService,
      private fb: FormBuilder,
      private snackBar: MatSnackBar){}
  
    ngOnInit(){
      this.getAllProducts();
      this.searchProductForm = this.fb.group({
        title: [null, [Validators.required]]
      })
    }
  
getAllProducts() {
  this.products = [];
  this.customerService.getAllProductsWithRatings().subscribe(res => {
    res.forEach((element: any) => {
      // Cloudinary already gives you a valid URL
      element.processedImg = element.imageUrl;  
      this.products.push(element);
    });
    console.log(this.products);
  });
}

submitForm() {
  this.products = [];
  const title = this.searchProductForm.get('title')!.value;
  this.customerService.getAllProductsByName(title).subscribe(res => {
    res.forEach((element: any) => {
      element.processedImg = element.imageUrl;  
      this.products.push(element);
    });
    console.log(this.products);
  });
}

addToCart(id: any) {
  this.customerService.addToCart(id).subscribe({
    next: () => {
      this.snackBar.open("Product added to cart successfully", "close", { duration: 5000 });
    },
    error: err => {
      if (err.status === 409) {
        this.snackBar.open("Product already in cart!", "close", { duration: 5000 });
      } else {
        this.snackBar.open("Error adding product to cart", "close", { duration: 5000 });
      }
    }
  });
}

}

