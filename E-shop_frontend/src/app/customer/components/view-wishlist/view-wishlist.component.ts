import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-view-wishlist',
  templateUrl: './view-wishlist.component.html',
  styleUrls: ['./view-wishlist.component.css']
})
export class ViewWishlistComponent {

  products: any[] = [];

  constructor(private customerService: CustomerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(){
    this.getWishlistByUseId();
  }

  getWishlistByUseId(){
    this.customerService.getWishlistByUseId().subscribe(res=>{
      this.products = res.map((element: any) => {
  element.processedImg = element.returnedImage;
  return element;
});

    })
  }

  addToCart(id: any) {
  this.customerService.addToCart(id).subscribe({
    next: () => {
      this.snackBar.open("Produit ajouté au panier avec succès", "close", { duration: 5000 });
    },
    error: err => {
      if (err.status === 409) {
        this.snackBar.open("Produit déjà dans le panier !", "close", { duration: 5000 });
      } else {
        this.snackBar.open("Erreur lors de l'ajout du produit au panier", "close", { duration: 5000 });
      }
    }
  });
}




}
