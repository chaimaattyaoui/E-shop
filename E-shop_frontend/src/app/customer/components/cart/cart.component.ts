import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { PlaceOrderComponent } from '../place-order/place-order.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {

  cartItems: any[] = [];
  order: any;
  couponForm!: FormGroup;
  availableCoupons: any[] = []; // Pour stocker les coupons disponibles
  showCouponSection: boolean = false; // Contrôle l'affichage

  constructor(private customerService: CustomerService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ){}
    
    ngOnInit(): void {
      this.couponForm = this.fb.group({
        code: [null,[Validators.required]]
      })
      this.getCart();
      this.loadAvailableCoupons(); // Charger les coupons au démarrage
    }


    loadAvailableCoupons() {
    this.customerService.getAvailableCoupons().subscribe(
      (coupons: any[]) => {
        this.availableCoupons = coupons;
        this.showCouponSection = this.availableCoupons.length > 0 && !this.order?.couponName;
      },
      error => {
        console.error('Erreur lors du chargement des coupons', error);
        this.showCouponSection = false;
      }
    );
  }

    applyCoupon(){
      this.customerService.applyCoupon(this.couponForm.get(['code'])!.value).subscribe(res=>{
      this.snackBar.open('Super ! Votre réduction est activée 🎉','close',{
      duration: 5000
      });
      this.getCart()

      }, error =>{
          this.snackBar.open(error.error, 'Close', {
          duration: 5000 });
      }
      )
    }

getCart() {
  this.cartItems = [];
  this.customerService.getCartByUserId().subscribe(res => {
    this.order = res;

    if (res.cartItems && Array.isArray(res.cartItems)) {
      res.cartItems.forEach((element: any) => {
        // ⚠️ You’re now using Cloudinary, so the backend returns imageUrl (string),
        // not returnedImg (base64). Adapt accordingly:
        element.processedImg = element.returnImg || element.imageUrl || '';
        this.cartItems.push(element);
      });
    }
  });
}

increaseQuantity(productId: any){
  this.customerService.increaseProductQuantity(productId).subscribe(res=>{
    this.snackBar.open('Product quantity increased','close',{duration: 5000});
    this.getCart();


})
}


decreaseQuantity(productId: any){
  this.customerService.decreaseProductQuantity(productId).subscribe(res=>{
    this.snackBar.open('Product quantity decreased','close',{duration: 5000});
    this.getCart();
    

})
}

placeOrder(){
  this.dialog.open(PlaceOrderComponent);
}

removeProduct(productId: any) {
  this.customerService.removeProductFromCart(productId).subscribe(res => {
    this.snackBar.open('Produit supprimé du panier 🗑️', 'Fermer', { duration: 4000 });
    this.getCart();
  }, error => {
    this.snackBar.open('Erreur lors de la suppression', 'Fermer', { duration: 4000 });
  });
}



}

