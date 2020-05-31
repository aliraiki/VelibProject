package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import core.*;

/**
 * JUnit test to make sure the MyVelibNetwork methods work properly (adding users, stations and bicycles). 
 * 
 * @author Ali Ra�ki
 *
 */

public class MyVelibNetworkTest {

	/**
	 * Set up a network of side 10.
	 * 
	 * Test if the right number of stations and parking slots is added, and correctly initialized.
	 *  
	 */
	@Test
	public void testAddStations() {
		MyVelibNetwork network = new MyVelibNetwork(10);
		network.addStations(6, 11); //Adding 6 stations, with 11 parking slots in each one.
		
		assertEquals(network.getStations().size(), 6);
		
		HashMap.Entry<Integer,Station> stationEntry = network.getStations().entrySet().iterator().next();
		int nbOfParkingSlots = stationEntry.getValue().getParkingSlots().size();
		int totalParkingSlots = nbOfParkingSlots*network.getStations().size();
		assertEquals(totalParkingSlots, 66);
		
		for(Station station : network.getStations().values()) {
			assertTrue(station.isOnline());
			assertNotNull(station.getX());
			assertNotNull(station.getY());
		}
		
	}

	/**
	 * Set up a network of side 10.
	 * 
	 * Test if the right number of users is added and that all the required fields are correctly filled.
	 *  
	 */
	@Test
	public void testAddUsers() {
		MyVelibNetwork network = new MyVelibNetwork(10);
		network.addUsers(10); //Adding 10 users.
		
		assertEquals(network.getUsers().size(), 10);
		
		for(User user : network.getUsers().values()) {
			assertNotNull(user.getName());
			assertNotNull(user.getCreditCardBalance());
			assertNotNull(user.getX());
			assertNotNull(user.getY());
		}
		
	}

	/**
	 * Set up a network of side 10.
	 * 
	 * Test adding a single user.
	 *  
	 */
	@Test
	public void testAddUser() {
		MyVelibNetwork network = new MyVelibNetwork(10);
		network.addUser("Georges", "vmax"); //Add a user Georges with a Vmax Card;
		network.addUser("Suzie", "vlibre"); //Add a user Suzie with a Vlibre Card;
		network.addUser("John", "none"); //Add a user John with no registration Card;
		
		System.out.println(network.getUsers().size());
		assertEquals(network.getUsers().size(), 3);		
	}
}