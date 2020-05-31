package core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/** 
 * Represents an abstract station.
 * @author Mathieu Sibu�
*/
public abstract class Station implements StationObserver {
	
	/*ATTRIBUTES*/
	private static int counterToGenerateIDs = 0;
	private int ID;
	private boolean isOnline;
	private boolean isTerminalOutOfOrder;
	private int nbOfOutOfOrderParkingSlots;
	private double x;
	private double y;
	private HashMap<Integer,ParkingSlot> parkingSlots;
	//registrationFees for user with no registration card
	@SuppressWarnings("serial")
	final static private HashMap<String,Double> feesForUserWithNoCard = new HashMap<String,Double>() {{
		put("mechanical",1.0);
		put("electrical",2.0);
	}};
	//for statistics:
	private int totalNbOfRentOps;
	private int totalNbOfReturnOps;
	
	
	
	/*CONSTRUCTORS*/
	public Station(double x, double y, HashMap<Integer,ParkingSlot> parkingSlots) {
		super();
		this.x = x;
		this.y = y;
		this.parkingSlots = parkingSlots;
		nbOfOutOfOrderParkingSlots = 0;
		for (ParkingSlot ps: this.parkingSlots.values()) {
			ps.registerObserver(this);
			if (ps.isOutOfOrder()) {
				nbOfOutOfOrderParkingSlots += 1;
			}
		}
		isOnline = true;
		isTerminalOutOfOrder = false;
		ID = this.generateID();
		totalNbOfRentOps = 0;
		totalNbOfReturnOps = 0;
	}	
	
	public Station(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		//or just leave it null?
		parkingSlots = new HashMap<Integer,ParkingSlot>();
		nbOfOutOfOrderParkingSlots = 0;
		for (ParkingSlot ps: this.parkingSlots.values()) {
			ps.registerObserver(this);
			if (ps.isOutOfOrder()) {
				nbOfOutOfOrderParkingSlots += 1;
			}
		}
		isOnline = true;
		isTerminalOutOfOrder = false;
		ID = this.generateID();
		totalNbOfRentOps = 0;
		totalNbOfReturnOps = 0;
	}
	
	
	/*METHODS*/
	//getters and setters
	public int getNbOfOutOfOrderParkingSlots() {
		return nbOfOutOfOrderParkingSlots;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isTerminalOutOfOrder() {
		return isTerminalOutOfOrder;
	}

	public void setTerminalOutOfOrder(boolean isTerminalOutOfOrder) {
		this.isTerminalOutOfOrder = isTerminalOutOfOrder;
		isOnline = (nbOfOutOfOrderParkingSlots == parkingSlots.size() && isTerminalOutOfOrder)? false: true;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public HashMap<Integer, ParkingSlot> getParkingSlots() {
		return parkingSlots;
	}

	//when parkingSlots HashMap is updated, we need to go through all the parking slots stored in 
	public void setParkingSlots(HashMap<Integer, ParkingSlot> parkingSlots) {
		if (this.parkingSlots.equals(parkingSlots)) {
			return;
		}
		this.parkingSlots = parkingSlots;
		nbOfOutOfOrderParkingSlots = 0;
		for (ParkingSlot ps: this.parkingSlots.values()) {
			ps.registerObserver(this);
			if (ps.isOutOfOrder()) {
				nbOfOutOfOrderParkingSlots += 1;
			}
		}
		isOnline = (nbOfOutOfOrderParkingSlots == parkingSlots.size() && isTerminalOutOfOrder)? false: true;
	}

	public int getID() {
		return ID;
	}

	public int getTotalNbOfRentOps() {
		return totalNbOfRentOps;
	}

	public void setTotalNbOfRentOps(int totalNbOfRentOps) {
		this.totalNbOfRentOps = totalNbOfRentOps;
	}

	public int getTotalNbOfReturnOps() {
		return totalNbOfReturnOps;
	}
	
	public void setTotalNbOfReturnOps(int totalNbOfReturnOps) {
		this.totalNbOfReturnOps = totalNbOfReturnOps;
	}
	
	public static HashMap<String, Double> getFeesForUserWithNoCard() {
		return feesForUserWithNoCard;
	}

	//equals and hashCode
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + (isOnline ? 1231 : 1237);
		result = prime * result + (isTerminalOutOfOrder ? 1231 : 1237);
		result = prime * result + ((parkingSlots == null) ? 0 : parkingSlots.hashCode());
		result = prime * result + totalNbOfRentOps;
		result = prime * result + totalNbOfReturnOps;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	//no point in having an equals method here, with unique IDs
	
	//toString
	@Override
	public String toString() {
		return "Station " + ID + ":\n"
				+ "- the station is " + (isOnline? "online": "offline") + "\n"
				+ "- its payment terminal is " + (isTerminalOutOfOrder? "out of order": "working fine") + "\n"
				+ "- the station is located at (" + x + ", " + y + ")" + "\n"
				+ "- it has " + parkingSlots.size() + " parking slots, " + nbOfOutOfOrderParkingSlots + " of which are out of order.";
	}
	
	//custom methods
	/**
	 * Increments the static counter to generate a unique ID for each Station.
	 * @return int: the incremented static counter, i.e. a new ID
	 */
	final public int generateID() {
		counterToGenerateIDs += 1;
		return counterToGenerateIDs;
	}

	/**
	 * Identifies a user via the station's terminal. Prints the mean of identification
	 * @param User: the user willing to rent a bike
	 */
	public void identifyUser(User user) {
		if (user.getRegistrationCard() != null) {
			System.out.println("User "+user.getID()+" registrated with "+user.getRegistrationCard().getClass()+" card.");
		} else {
			System.out.println("User "+user.getID()+" registrated with his credit card.");
		}
	}
	
	/**
	 * Charges a user for its bicycle trip depending on the station's fees. Should only be called in 
	 * @param User: the user willing to return its rented bike after using it
	 * @param double: the time (in minutes) spent on the bike
	 */
	/*
	abstract void chargeUser(User user, int duration);
	*/
	public void chargeUser(User user, int duration) throws RuntimeException {
		if (isTerminalOutOfOrder) {
			throw new RuntimeException("Terminal of station "+ID+" not working: go to closest station");
		} else {
			double cost = 0;
			if (user.getRegistrationCard() == null) {
				if (user.getRentedBicycle() instanceof MechanicalBike) {
					cost = (double) duration/60 * feesForUserWithNoCard.get("mechanical");	
				} else if (user.getRentedBicycle() instanceof ElectricalBike) {
					cost = (double) duration/60 * feesForUserWithNoCard.get("electrical");
				}
			} else {
				CardVisitor cardVisitor = new ConcreteCardVisitor();
				Card userCard = user.getRegistrationCard();
				cost = userCard.accept(cardVisitor, duration, user.getRentedBicycle());
			}
			user.setCreditCardBalance(user.getCreditCardBalance() - cost);
			user.setMyVelibTotalCharges(user.getMyVelibTotalCharges() + cost);
		}
	}
	
	//implemented method from interface Observer
	@Override
	public void update(boolean newIsOutOfOrder) {
		if (newIsOutOfOrder) {
			nbOfOutOfOrderParkingSlots += 1;
		} else {
			nbOfOutOfOrderParkingSlots -= 1;
		}
		isOnline = (nbOfOutOfOrderParkingSlots == parkingSlots.size() && isTerminalOutOfOrder)? false: true;
	}
	
	/**
	 * Computes the occupation rate of a given station during a certain time window.
	 * @param Date: the inf date of the time window
	 * @param Date: the sup date of the time window
	 */
	public double getOccupationRate(Date infDate, Date supDate) {
		double res = 0;
		for (ParkingSlot ps: parkingSlots.values()) {
			ArrayList<ActivityLog> selectedActivityLogs = new ArrayList<ActivityLog>();
			for (int i = 0; i < ps.getActivityLogs().size(); i++) {
				ActivityLog currentAl = ps.getActivityLogs().get(i);
				if (ActivityLog.getDateDiff(currentAl.getDate(), supDate)<0) {
					break;
				} else if (ActivityLog.getDateDiff(currentAl.getDate(), infDate)<=0) {
					selectedActivityLogs.add(currentAl);
				} else if (ActivityLog.getDateDiff(currentAl.getDate(), infDate)>0 
						&& i < ps.getActivityLogs().size()-1
						&& ActivityLog.getDateDiff(ps.getActivityLogs().get(i+1).getDate(), infDate)<0
					)
				{
					selectedActivityLogs.add(currentAl);
				}
			}
			
			for (int i = 0; i < selectedActivityLogs.size(); i++) {
				ActivityLog currentAl = selectedActivityLogs.get(i);
				
				//cas i=0
				if (currentAl.isSetToOccupied() 
						&& ActivityLog.getDateDiff(currentAl.getDate(), infDate)>0
						&& i<selectedActivityLogs.size()-1
					) 
				{
					ActivityLog nextAl = selectedActivityLogs.get(i+1);
					res += ActivityLog.getDateDiff(infDate, nextAl.getDate());
					
				} else if (currentAl.isSetToOccupied() && i==selectedActivityLogs.size()-1) {
					res += ActivityLog.getDateDiff(currentAl.getDate(), supDate);
					
				} else if (currentAl.isSetToOccupied() && i<selectedActivityLogs.size()-1) {
					ActivityLog nextAl = selectedActivityLogs.get(i+1);
					res += ActivityLog.getDateDiff(currentAl.getDate(), nextAl.getDate());
				} 
			}
		}
		return res / ActivityLog.getDateDiff(infDate, supDate) * parkingSlots.size();
	}

}
