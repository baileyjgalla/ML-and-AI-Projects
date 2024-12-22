import java.util.HashMap;
import java.util.Random;

/**
 * 
 */

/**Player class controlling what room player is currently in. 
 * @author baije
 *
 */
public class Player {
	/** the max weight the player can hold. */
	private static final int MAX_WEIGHT = 30;
	/** The room the player character is currently in. */
	private Room currentRoom;
	/** integer to hold hit probability. **/
	private int hitProbability;
	/** if player is able to fly. **/
	private Boolean canFly;
	/** integer to hold health of a player. **/
	private int playerHealth;
	/** Item being used as weapon. **/
	private Item weapon;
	/** int keeping track of players strength (affects damage done). **/
	private int playerStrength;
	/**
	 * Keeping track of previous location for BACK.
	 */
	private Room previousRoom;
	/** Hashmap of the players current inventory with name and weight of item. */
	private HashMap<String, Item> inventory;
	/** the max weight the player can hold. */
	/** the current weight of items in inventory. */
	private int totalWeight;
	
	/**
	 * Constructor for player class.
	 * @param startingRoom where player begins.
	 */
	public Player(Room startingRoom) {
		currentRoom = startingRoom;
		previousRoom = null;
		inventory = new HashMap<>();
		playerHealth = 100;
		playerStrength = 100;
		canFly = false;
	}
	public void canFly(Boolean flies) {
		canFly = flies;
	}
	/**
	 * Method to return how much damage player inflicted on monster. 
	 * @return damage. Integer value for damage dealt.
	 */
	public int damageDealt() {
		int damage;
		damage = 0;
		if (weapon != null) {
			Random rand = new Random();
			damage = rand.nextInt(100) + 1;
			
		}
		return damage;
	}
	public boolean playerCanFly() {
		return canFly;
	}
	public int getDamageDealt() {
		return damageDealt();
	}
	public int getHealth() {
		return playerHealth;
	}
	public Item getWeapon() {
		return weapon;
	}
	public void setWeapon(Item chosenWeapon) {
		weapon = chosenWeapon;
	}
	public void setHealth(int inHealth) {
		playerHealth = inHealth;
	}
	public void setStrength(int strength) {
		playerStrength = strength;
	}
	public int getStrength() {
		return playerStrength;
	}
	
	
	
	/**
	 * method to add item to inventory as long as it is below max weight threshold.
	 * @param item The item to be added.
	 * @return Whether or not the item was added.
	 */
	public boolean addItem(Item item) {
		boolean success = false; 
		totalWeight = totalWeight + item.getWeight(); 
		if (totalWeight <= MAX_WEIGHT) {
			inventory.put(item.getName().toLowerCase(), item);
			success = true;
		}
		else {
			totalWeight = totalWeight - item.getWeight();	
		}
		return success;
	}
	/**
	 * checks if players inventory is too heavy with addition of new item.
	 * @param item item we are checking the weight of.
	 * @return success if it is not too heavy to be added, false if it is.
	 */
	public boolean checkWeight(Item item) {
		boolean success = false; 
		int weight = 0;
		for (Item theItem : inventory.values()) {
			weight = weight + theItem.getWeight();
		}
		int total = item.getWeight() + weight; 
		if (total < MAX_WEIGHT) {
			success = true;
		}
		return success;
	}
	
	/** retrieves an item from inventory when given name as argument.
	 * @param name user entered item name.
	 * @return an item. */
	public Item getItem(String name) {
		return inventory.get(name);
	}
	public HashMap<String, Item> getInventory(){
		return inventory;
	}
	/** removes item from the inventory.
	 * @param itemName The name of the item to remove.
	 * @return removal. */
	public Item removeItem(String itemName){
		  Item removal = getItem(itemName);
		  if(removal != null) {
			  inventory.remove(itemName);
			  totalWeight = totalWeight - removal.getWeight();  
		  }
		  return removal;
	}
	/**
	 * iterates over player inventory.
	 * @return the items in the inventory.
	 */
	public String inventoryString() {
		String result = "";
		if (inventory.isEmpty()) {
			result = "Your inventory is currently empty.";
		}
		else {
		for (String item: inventory.keySet()) {
			result = result + item + " - ";
		}
		}
		return result;
	}
	/**
	 * sets players current room.
	 * @param room room that is passed in to be changed to current room.
	 */
	public void setRoom(Room room) {
		previousRoom = currentRoom;
		currentRoom = room;	
	}
	/**
	 * getter for players current room.
	 * @return current Room.
	 */
	public Room getRoom() {
		return currentRoom;
	}
	/**
	 * if player is able to go back to the previous room.
	 * @return true if player can go back/ false if they cannot.
	 */
	public boolean goBack() {
		boolean worked = false;
		if (previousRoom != null) {
			setRoom(previousRoom);
			worked = true;
		}
		return worked;
	}
	/**
	 * gets player's hit probability in a battle with monster.
	 * @return hitProbability (0 to 100 the attack will land).
	 */
	public int getHitProbability() {
		Random probability = new Random();
		hitProbability = probability.nextInt(100);
		return hitProbability;
	}
  
}

