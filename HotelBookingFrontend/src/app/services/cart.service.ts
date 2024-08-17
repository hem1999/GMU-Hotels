import { Injectable } from '@angular/core';
import { cartBooking } from '../checkout/cartItem.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  /*
    bookings: booking -> roomId, startDate, endDate, pricePerNight []
    //TODO: You should be able to add extra rooms, delete single room or all, 
      // edit start and end dates -> This should again check for availability again.
    total: (startDate)-(endDate) * pricePerNight;

  */
  
  private bookings: cartBooking[] | []

  constructor() {
    this.bookings = [];
   }

  addBooking(newBooking: cartBooking){
    this.bookings = [...this.bookings, newBooking];
  }
  
  getBookings(){
    return this.bookings;
  }


}
