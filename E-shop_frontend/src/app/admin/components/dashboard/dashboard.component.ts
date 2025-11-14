import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  products:any[] = [];

  searchProductForm!: FormGroup;

  constructor(private adminService: AdminService,
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
  this.adminService.getAllProductsWithRatings().subscribe(res => {
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
  this.adminService.getAllProductsByName(title).subscribe(res => {
    res.forEach((element: any) => {
      element.processedImg = element.imageUrl;  
      this.products.push(element);
    });
    console.log(this.products);
  });
}
getAllProductsWithRatings() {
    this.products = [];
    this.adminService.getAllProductsWithRatings().subscribe(res => {
      res.forEach((element: any) => {
        element.processedImg = element.imageUrl;
        this.products.push(element);
      });
      console.log(this.products);
    });
  }


deleteProduct(productId: any) {
  const confirmed = confirm('Êtes-vous sûr de vouloir supprimer ce produit ?');
  if (!confirmed) return;

  this.adminService.deleteProduct(productId).subscribe({
    next: () => {
      this.snackBar.open('Produit supprimé avec succès !', 'Fermer', {
        duration: 5000
      });
      this.getAllProducts(); // 🔄 reload la liste
    },
    error: (err) => {
      this.snackBar.open('Erreur lors de la suppression !', 'Fermer', {
        duration: 5000,
        panelClass: 'error-snackbar'
      });
      console.error(err);
    }
  });
}
}

