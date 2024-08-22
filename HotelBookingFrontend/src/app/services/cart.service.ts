import { Injectable } from '@angular/core';
import { cartBooking } from '../checkout/cartItem.model';
import { BehaviorSubject, Observable } from 'rxjs';

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
  
  private bookings: BehaviorSubject<cartBooking[]> = new BehaviorSubject<cartBooking[]>([]);

  constructor() {
    this.loadCart();
   }

  addBooking(newBooking: cartBooking):void{
    let conflicts:cartBooking[]|[] = this.isBookingConflict(newBooking);
    if(conflicts.length == 0){
      this.bookings.next([...this.bookings.value, newBooking]);
      this.saveCart();
    }else{
      //TODO: Do better things with the conflicts for the user to understand!.
      alert("Current Booking conflicts with other bookings, please check!");
    }
  }

  
  getBookings():Observable<cartBooking[]>{
    return this.bookings;
  }

  removeAllBookings():void{
    this.bookings.next([]);
    this.saveCart();
  }

  removeOneBooking(removeRoomId: String, roomStartDate: String, roomEndDate:String):void{
    const updatedBookings = [...this.bookings.value.filter( 
      room=> {
        if(room.roomId != removeRoomId){
          return true;
        }
        if(room.startDate != roomStartDate || room.endDate != roomEndDate){
          return true;
        }
        return false;
      }
      // !(room.roomId==removeRoomId && room.startDate == roomStartDate && room.endDate == roomEndDate)
    )];

    this.bookings.next(updatedBookings);
    this.saveCart();
  }

  isBookingConflict(booking:cartBooking):cartBooking[]|[]{
    let bookingStartDate = new Date(booking.startDate.toString());
    let bookingEndDate = new Date(booking.endDate.toString());

    return this.bookings.value.filter(r => {
      //fetch only conflicting ones!
      let currentRoomStartDate = new Date(r.startDate.toString());
      let currentRoomEndDate = new Date(r.endDate.toString());

      if(r.roomId == booking.roomId){
        if(bookingStartDate <= currentRoomEndDate && bookingEndDate >= currentRoomStartDate){
          return true;
        }
      }
      return false;

    }
    )
  }

  private saveCart(){
    localStorage.setItem('cart',JSON.stringify(this.bookings.value));
  }

  private loadCart(){
    const savedCart = localStorage.getItem('cart');
    if(savedCart){
      this.bookings.next(JSON.parse(savedCart));
    }
  }

  public getInvoice(){
    // Go through each this.bookings, find the difference between startDate-endDate,
    // then return an key,value map that has roomName: totalPrice for that room
    let roomTotalPriceMap:{[roomName:string]: number} = {

    }

    this.bookings.value.forEach(
      b => {
        let bookingEndDate:Date = new Date(b.endDate.toString());
        let bookingStartDate:Date = new Date(b.startDate.toString());
        let differenceInDays:number = 1;
        if(bookingEndDate != bookingStartDate){
          differenceInDays = (bookingEndDate.getTime() - bookingStartDate.getTime());
          differenceInDays = differenceInDays/((1000*60*60*24))
        }

        let price: number = differenceInDays*b.pricePerNight.valueOf();
        if(roomTotalPriceMap[b.roomName.toString()]){
          roomTotalPriceMap[b.roomName.toString()] += price;
        }else{
          roomTotalPriceMap[b.roomName.toString()] = price;
        }
      }
    );
    return roomTotalPriceMap;
  }

}
