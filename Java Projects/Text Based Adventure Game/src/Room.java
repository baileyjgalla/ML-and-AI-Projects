/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Campus of Kings" application. "Campus of Kings" is a
 * very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game. It is connected
 * to other rooms via doors. The doors are labeled north, east, south, west.
 * For each direction, the room stores a reference to an instance of door.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
import java.util.HashMap;

public class Room {
	/** Counter for the total number of rooms created in the world. */
	private static int counter;
	/** HashMap to tie directions (NSEW etc) to their doors. */
	private HashMap<String, Door> directions;
	/** The name of this room.  Room names should be unique. */
	private String name;
	/** The description of this room. */
	private String description;
	/** The monster assigned to the room. **/
	private Monster monster;
	/** The points earned per room. */
	private int points;
	/** HashMap to tie names with items. */
	private HashMap<String, Item> items;

	/**
	 * Static initializer.
	 */
	static {
		counter = 0;
	}
	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 *@param monster the monster associated with the room.
	 * @param name  The room's name.
	 * @param description
	 *            The room's description.
	 */
	public Room(String name, String description, Monster monster) {
		this.name = name;
		this.description = description;
		counter++;
		directions = new HashMap<>();
		items = new HashMap<>();
		this.monster = monster;
		
	}
	/** 
	 * Method to get Monster. 
	 * @return monster type Monster
	 */
	public Monster getMonster() {
		return monster;
	}
	
	public void setMonster(Monster inMonster) {
		monster = inMonster;
	}
	
	public void addItem(String itemName, Item item) {
		items.put(itemName.toLowerCase(), item);
	}
	public Item getItem(String itemName) {
		Item item = items.get(itemName.toLowerCase());
		return item;
	}
	public Item removeItem(String itemName) {
		  Item removal = getItem(itemName);
		  items.remove(itemName);
		  return removal;
	}
	public HashMap<String, Item> getItems(){
		return items;
	}
	/**
	 * * Defines an exit from this room. 
	 * @param direction The direction of the exit. 
	 * @param neighbor The door in the given direction.
	 * 
	 */
	public void setExit(String direction, Door neighbor) {
		directions.put(direction, neighbor);
	}
	/**
	 * Gets a door in a specified direction if it exists.
	 * @param direction
	 * @return The door in the specified location or null if it does not exist 
	 */
	public Door getExit(String direction) {
		return directions.get(direction);
	}

	/**
	 * Returns the name of this room.
	 *
	 * @return The name of this room.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the description of this room.
	 *
	 * @return The description of this room.
	 */
	public String getDescription() {
		return description;
	}
	public String toString() {
		String result = "";
		result = getName() + ": \n" + getDescription() + "\n" + "Exits: ";
		for (String direction : directions.keySet()) {
			if (getExit(direction) != null) {
				result = result + direction + " - ";
			}
		}
		if (!items.isEmpty()) {
			result = result + "\n Items: ";
			for (String itemName : items.keySet()) {
				result = result  + " " + itemName + " - ";
			}
		}
		return result;
	}

	/**
	 * Returns the number of rooms that have been created in the world.
	 * @return The number of rooms that have been created in the world.
	 */
	public static int getCounter() {
		return counter;
	}
	public int getPoints() {
		int newPoints = points;
		points = 0;
		return newPoints;	
	}
	public void setPoints(int points) {
		this.points = points;
	}
}
