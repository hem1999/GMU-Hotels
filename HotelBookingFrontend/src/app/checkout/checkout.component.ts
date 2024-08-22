import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CartService } from '../services/cart.service';
import { cartBooking } from './cartItem.model';
import { Observable, Subscription, } from 'rxjs';
import { KeyValuePipe } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { HotelApiService } from '../services/hotel-api.service';
import { addBookingType } from '../profile/booking.model';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [KeyValuePipe],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit, OnDestroy{
  // authService = inject(AuthService);
  // router = inject(Router);
  // cartService = inject(CartService);

  constructor(private authService:AuthService, private router:Router,
     private cartService:CartService, private hoteApiService: HotelApiService){

  }
  private cartSubscription: Subscription | undefined;
  cartItems: cartBooking[] = [];
  invoice:{[roomName:string]: number} = {

  }
  totalCartValue:number = 0;
  ngOnInit(){
    if(this.authService.getUserId() == null){
      this.router.navigateByUrl("login");
    }
    this.cartSubscription = this.cartService.getBookings().subscribe(
      res => this.cartItems = res
    );

    this.updateInvoice();

  }

  ngOnDestroy(): void {
      if(this.cartSubscription){
        this.cartSubscription.unsubscribe();
      }
  }

  updateInvoice(){
    this.invoice = this.cartService.getInvoice();
    this.totalCartValue = 0;
    Object.keys(this.invoice).forEach(
      name => {
        this.totalCartValue+= this.invoice[name];
      }
    )
  }

  makeBookingRequestFromCartItems():addBookingType[]{
    let request: addBookingType[] = [];
    this.cartItems.forEach(
      c => {
        let r = {
          "roomId" : c.roomId.toString(),
          "startDate": c.startDate.toString(),
          "endDate": c.endDate.toString(),
          "userId": this.authService.getUserId(),
          "bookingDate": new Date().toISOString()
        } 
        request.push(r);
      }
    );

    return request;
  }

  onCheckOut(){
    let request = this.makeBookingRequestFromCartItems();
    console.log(request);
    this.hoteApiService.addBookings(request).subscribe({
      next: res => {
        this.router.navigateByUrl("/thankyou");
         }
    });
    
  }

  removeThisBooking(booking: cartBooking){
    this.cartService.removeOneBooking(booking.roomId, booking.startDate, booking.endDate);
    this.updateInvoice();
  }


  
}
