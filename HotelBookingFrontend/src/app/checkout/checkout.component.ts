import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CartService } from '../services/cart.service';
import { cartBooking } from './cartItem.model';
import { Observable, Subscription, } from 'rxjs';
import { KeyValuePipe } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { HotelApiService } from '../services/hotel-api.service';
import { addBookingType } from '../profile/booking.model';
import { FormControl,Validators,ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [KeyValuePipe, ReactiveFormsModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css'
})
export class CheckoutComponent implements OnInit, OnDestroy{
  // authService = inject(AuthService);
  // router = inject(Router);
  // cartService = inject(CartService);

  //For edit button
  currentEditItem = -1;
  editButtonText = "Edit";
  newStartDate =new  FormControl('',Validators.required);
  newEndDate = new FormControl('',Validators.required);


  constructor(private authService:AuthService, private router:Router,
     private cartService:CartService, private hotelApiService: HotelApiService){

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
    this.hotelApiService.addBookings(request).subscribe({
      next: res => {
        this.router.navigateByUrl("/thankyou");
         }
    });
    
  }

  onEditCartItem(booking:cartBooking,itemIndex:number){
    this.newStartDate.patchValue(booking.startDate.toString());
    this.newEndDate.patchValue(booking.endDate.toString());
    this.currentEditItem = itemIndex;
  }

  onEditCartSubmit(booking:cartBooking){
    this.currentEditItem = -1;
    //TODO: GetIsRoomAvailable when I tried for 5th sept to 7th sept on the single room page showing not available
    // But here no error and also the response says true!
    this.hotelApiService.getIsRoomAvailable(booking.roomId.toString(),this.newStartDate.value??'',this.newEndDate.value??'').subscribe(
      {
        next: res => {
          console.log(res);
          if(res){
            this.cartService.removeOneBooking(booking.roomId.toString(),booking.startDate,booking.endDate);
            let newBooking = booking;
            newBooking.startDate = this.newStartDate.value??'';
            newBooking.endDate = this.newEndDate.value??'';
            this.cartService.addBooking(newBooking);
            this.updateInvoice();
          }else{
            alert("Not available for updated booking dates!");
          }
          

        },
        error: err => {
          alert("Not available for updated booking dates!");
        }
      }
    )
  }

  removeThisBooking(booking: cartBooking){
    this.cartService.removeOneBooking(booking.roomId, booking.startDate, booking.endDate);
    this.updateInvoice();
  }


  
}
