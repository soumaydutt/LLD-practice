import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;


//BLOCK 1: defined enums

//types of vehicles
enum VehicleType{
    MOTORCYCLE,
    CAR,
    TRUCK
}
//size of parking lot
enum SpotType{
    COMPACT,
    MEDIUM,
    LARGE
}
//status of parking ticket
enum TicketStatus{
    ACTIVE,
    PAID
}


// BLOCK 2: vehicle hierarchy
abstract class Vehicle{
    private final String licensePlate;
    private final VehicleType type;
    public Vehicle (String licensePlate , VehicleType type){
        this.licensePlate=licensePlate;
        this.type=type;
    }
    public String getLicensePlate(){
        return licensePlate;
    }
    public VehicleType getType(){
        return type;
    }
}
class Car extends Vehicle{
    public Car(String licensePlate){
        super(licensePlate, VehicleType.CAR);
    }
}
class MotorCycle extends Vehicle{
    public MotorCycle(String licensePlate){
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}
class Truck extends Vehicle{
    public Truck(String licensePlate){
        super(licensePlate,VehicleType.TRUCK);
    }
}

//Block 3:Parking Lot
class ParkingSpot{
    private final String spotId;
    private final SpotType spotType;
    private boolean isOccupied;
    private Vehicle currentVehicle;

    public ParkingSpot (String spotId, SpotType SpotType){
        this.spotId=spotId;
        this.spotType=SpotType;
        this.isOccupied=false;
        this.currentVehicle=null;

    }
    //Business Logic:compatibility between carType and spotSize
    public boolean canFitVehicle(Vehicle vehicle){
        if(this.isOccupied){
            return false;
        }
        VehicleType vType=vehicle.getType();
        if (vType==VehicleType.MOTORCYCLE){
            return true;
        }
        else if(vType==VehicleType.CAR){
            return this.spotType==spotType.MEDIUM|| this.spotType==spotType.LARGE;
        }
        else if(vType==VehicleType.TRUCK){
            return this.spotType==spotType.LARGE;
        }
        return false;
    }
    //thread safety:for parking
    public synchronized boolean park(Vehicle vehicle){
        if(!canFitVehicle(vehicle)){
            
            return false;
        }
        this.currentVehicle=vehicle;
        this.isOccupied=true;
        return true;
    }
    //Thread safe unpark
    public synchronized void unpark(){
        this.currentVehicle=null;
        this.isOccupied=false;

    }
    public String getSpotId(){
        return spotId;
    }
    public SpotType getSpotType(){
        return spotType;
    }
    public boolean isOccupied(){
        return isOccupied;
    }
    public Vehicle getVehicle(){
        return currentVehicle;
    }
}

//Block 4
class ParkingTicket{
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    private TicketStatus status;

    public ParkingTicket(Vehicle vehicle,ParkingSpot spot){
        this.ticketId=UUID.randomUUID().toString().substring(0,8);
        this.vehicle=vehicle;
        this.spot=spot;
        this.entryTime=LocalDateTime.now();
        this.exitTime=null;
        this.fee=0.0;
        this.status=TicketStatus.ACTIVE;


    }
public void markAsPaid(double fee){
    this.exitTime=LocalDateTime.now();
    this.fee=fee;
    this.status=TicketStatus.PAID;

}

public String getTicketId(){
    return ticketId;
}
public Vehicle getVehicle(){
    return vehicle;
}
public ParkingSpot getSpot(){
    return spot;
}
public LocalDateTime getEntryTime(){
    return entryTime;
}
public LocalDateTime  getExitTime(){
    return exitTime;
}
public double getFee(){
    return fee;
}
public TicketStatus getStatus(){
    return status;
}
}

//Block 5
interface FeeCalculationStrategy{
    double calculateFee(ParkingTicket ticket);
}
class StandardFeeStrategy implements FeeCalculationStrategy{
    @Override
    public double calculateFee(ParkingTicket ticket){
        LocalDateTime entry=ticket.getEntryTime();
        LocalDateTime exit=ticket.getExitTime()!=null?ticket.getExitTime():LocalDateTime.now();
        long hours=Duration.between(entry,exit).toHours();
        if (hours<1){hours=1;}
        double hourlyRate=0.0;
        VehicleType type=ticket.getVehicle().getType();
        switch (type){
            case MOTORCYCLE:
                hourlyRate=10.0;
                break;
                case CAR:
                    hourlyRate=20.0;
                    break;
                    case TRUCK:
                        hourlyRate=40.0;
                        break;
        }
        return hours*hourlyRate;
    }
}

public class ParkingLot{
    public static void main(String[] args){
        ParkingSpot spot= new ParkingSpot("S-301",SpotType.LARGE);
        Vehicle car= new Car("CG-10-1010");
        boolean parked=spot.park(car);
        System.out.println("Car Parked Successfully"+parked);
        ParkingTicket ticket=new ParkingTicket(car,spot);
        System.out.println("Ticket Issued: ID=" + ticket.getTicketId() 
                + " | Vehicle Type=" + ticket.getVehicle().getType());

        // Apply Strategy Pattern for fee calculation
        FeeCalculationStrategy feeStrategy = new StandardFeeStrategy();
        
        // Simulate vehicle exiting and calculating fee via strategy
        ticket.markAsPaid(feeStrategy.calculateFee(ticket));
        spot.unpark();

        System.out.println("Ticket Paid: Status=" + ticket.getStatus() 
                + " | Fee Charged: $" + ticket.getFee() 
                + " | Spot Free Now=" + (!spot.isOccupied()));

        System.out.println("Block 5 complete: Fee Calculation Strategy verified!");
        
    }
        
} 
        
        
        
        /*BLOCK 3 testing
       //create spots
       ParkingSpot compactSpot=new ParkingSpot("S-101",SpotType.COMPACT);
       ParkingSpot largeSpot=new ParkingSpot("S102",SpotType.LARGE);
       

       //create vehicles
       Vehicle car=new Car("CG-10-1100");
       Vehicle truck= new Truck("GG-10-11");

       // Test 1: Car tries to park in compact spot (Expected: false)
        boolean carInCompact = compactSpot.park(car);
        System.out.println("Car parked in Compact spot: " + carInCompact);

        // Test 2: Car tries to park in large spot (Expected: true)
        boolean carInLarge = largeSpot.park(car);
        System.out.println("Car parked in Large spot: " + carInLarge);

        // Test 3: Truck tries to park in occupied large spot (Expected: false)
        boolean truckInOccupiedLarge = largeSpot.park(truck);
        System.out.println("Truck parked in already occupied Large spot: " + truckInOccupiedLarge);

        // Test 4: Unpark car and park truck (Expected: true)
        largeSpot.unpark();
        boolean truckAfterUnpark = largeSpot.park(truck);
        System.out.println("Truck parked in Large spot after car left: " + truckAfterUnpark);

        System.out.println("Block 3 complete: ParkingSpot logic verified!");

    }
}
*/
/*
test
//setup spot and vehicle
        ParkingSpot spot= new ParkingSpot("S-201",SpotType.MEDIUM);
        Vehicle car=new Car("CG-10-1010");

        //park the car
        boolean parked=spot.park(car);
        System.out.println("Car parked successfully"+parked);
        // 3. Issue Ticket
        ParkingTicket ticket = new ParkingTicket(car, spot);
        System.out.println("Ticket Issued: ID=" + ticket.getTicketId() 
                + " | Vehicle=" + ticket.getVehicle().getLicensePlate() 
                + " | Spot=" + ticket.getSpot().getSpotId() 
                + " | Status=" + ticket.getStatus());

        // 4. Vehicle exits and pays fee
        ticket.markAsPaid(50.0);
        spot.unpark();

        System.out.println("Ticket Paid: Status=" + ticket.getStatus() 
                + " | Fee charged=" + ticket.getFee() 
                + " | Spot free now=" + (!spot.isOccupied()));

        System.out.println("Block 4 complete: ParkingTicket lifecycle verified!");
    }
    */