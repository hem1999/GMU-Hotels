interface RoomIdsNamesMap {
    [key: string]: string;
  }

export interface Booking{
    bookingId: Number,
    bookingDate: String,
    startDate: String,
    endDate: String,
    roomIdsNamesMap: RoomIdsNamesMap,
    customerId: Number,
    customerName: String

}