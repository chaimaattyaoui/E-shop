import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  // Table columns to display
  displayedColumns: string[] = [
    'trackingId',
    'userName',
    'amount',
    'description',
    'address',
    'date',
    'status',
    'action'
  ];

  // Table data source for pagination
  dataSource = new MatTableDataSource<any>();
  orders: any[] = [];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private snackBar: MatSnackBar,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.getPlacedOrders();
  }

  // Fetch placed orders from backend
  getPlacedOrders(): void {
    this.adminService.getPlacedOrders().subscribe({
      next: (res) => {
        this.orders = res;
        this.dataSource.data = res;
        this.dataSource.paginator = this.paginator;
      },
      error: (err) => {
        console.error('Error fetching orders:', err);
        this.snackBar.open('Erreur lors du chargement des commandes', 'Fermer', { duration: 4000 });
      }
    });
  }

  // Change order status (Shipped, Delivered, etc.)
  changeOrderStatus(orderId: number, status: string): void {
    this.adminService.changeOrderStatus(orderId, status).subscribe({
      next: (res) => {
        if (res && res.id != null) {
          this.snackBar.open('Statut de la commande mis à jour avec succès', 'Fermer', { duration: 4000 });
          this.getPlacedOrders();
        } else {
          this.snackBar.open('Une erreur est survenue', 'Fermer', { duration: 4000 });
        }
      },
      error: () => {
        this.snackBar.open('Erreur de mise à jour du statut', 'Fermer', { duration: 4000 });
      }
    });
  }

  // View order details (for example, in a modal or console)
  viewOrderDetails(order: any): void {
    console.log('Order Details:', order);
    this.snackBar.open(`Commande ${order.trackingId} - ${order.orderStatus}`, 'Fermer', { duration: 4000 });
  }
}
