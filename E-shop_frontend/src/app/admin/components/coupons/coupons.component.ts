import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-coupons',
  templateUrl: './coupons.component.html',
  styleUrls: ['./coupons.component.css']
})
export class CouponsComponent {
  coupons: any[] = [];
  displayedColumns: string[] = ['name', 'discount', 'code', 'expirationDate', 'createdAt', 'actions'];
  conflictMessage: string | null = null; // To store the conflict message

  constructor(
    private adminService: AdminService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.getCoupons();
  }

  getCoupons() {
    this.adminService.getCoupons().subscribe(res => {
      this.coupons = res;
      this.conflictMessage = null; // Clear any previous conflict message
    });
  }

  // Fonction pour vérifier si un coupon est expiré
  isCouponExpired(expirationDate: string): boolean {
    if (!expirationDate) return false;
    const expiry = new Date(expirationDate);
    const today = new Date();
    return expiry < today;
  }

  // Fonction pour supprimer un coupon with enhanced conflict handling
  deleteCoupon(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce code promo ?')) {
      this.adminService.deleteCoupon(id).subscribe({
        next: (res) => {
          this.snackBar.open('✅ Code promo supprimé avec succès', 'Fermer', {
            duration: 5000,
            panelClass: 'success-snackbar'
          });
          this.getCoupons(); // Recharger la liste
        },
        error: (error) => {
          let errorMessage = 'Erreur lors de la suppression';
          if (error.status === 409 && error.error && error.error.message) {
            errorMessage = error.error.message; // e.g., "Cannot delete coupon as it is associated with existing orders"
            this.conflictMessage = errorMessage; // Store the conflict message
            this.snackBar.open(`❌ ${errorMessage}. Veuillez vérifier les commandes associées ou contacter le support.`, 'Fermer', {
              duration: 7000,
              panelClass: 'error-snackbar'
            });
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
            this.snackBar.open(`❌ ${errorMessage}`, 'Fermer', {
              duration: 5000,
              panelClass: 'error-snackbar'
            });
          } else {
            this.snackBar.open(`❌ ${errorMessage}`, 'Fermer', {
              duration: 5000,
              panelClass: 'error-snackbar'
            });
          }
        }
      });
    }
  }

  // Optional: Clear conflict message when user interacts again
  clearConflictMessage() {
    this.conflictMessage = null;
  }
}