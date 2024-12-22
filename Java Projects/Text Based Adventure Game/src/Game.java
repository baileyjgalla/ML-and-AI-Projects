import java.util.HashMap;
import java.util.Random;

/**
 * This class is the main class of the "Campus of Kings" application.
 * "Campus of Kings" is a very simple, text based adventure game. Users can walk
 * around some scenery. That's all. It should really be extended to make it more
 * interesting!
 *
 * This game class creates and initializes all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 *
 * @author Maria Jump
 * @author Bailey Gallagher 
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */

public class Game {
	/** The world where the game takes place. */
	private World world;
	/** The room the player character is currently in. */
	private int turns;
	/** score keeping for entering room. */
 	private int points; 
 	/** the player controlled by the user. */
	private Player player;
	/** Whether player met by monster (in battle is true) and false if monster is dead. */
	private boolean inBattle;
	/** Used to calculate hit probabilities and damage. **/
	private Random hitProbability;
	
	/**
	 * Create the game and initialize its internal map.
	 */
	public Game() {
		world = new World();
		// set the starting room
		turns = 0;
		points = 0;  
		player = new Player(world.getRoom("Section of graves from 1610-1640"));
		hitProbability = new Random();
	
	}
	/**
	 * monster dealing damage to player. Monster starts fight.
	 * @param monster what monster the player is being attacked by
	 * @return playerDied if player is dead (true) if player is alive (false).  
	 */
	private boolean monsterAttack(Monster monster) {
		inBattle = true;
		Boolean playerDied = false;
		int playerHealth = player.getHealth();
		if (inBattle) {
			if (monster.getMonsterName().toLowerCase().equals("skeletal bat") && !player.playerCanFly()) {
				Writer.println("You dont stand a chance against this bat from the ground! Maybe try growing wings next time...");
				Writer.println();
				player.setHealth(0);
				playerDied = true;
				inBattle = false;
				Writer.println("You were killed by the " + monster.getMonsterName() + ". Poor Alminster.");
			}
			else {
				int hit = hitProbability.nextInt(100);
				if (hit <= monster.getHitProbability()) {
					int monsterInflicts = monster.getDamage();
					playerHealth = playerHealth - monsterInflicts;
					player.setHealth(playerHealth);
					if(player.getHealth() > 0) {
					Writer.println("You take " + monsterInflicts + " damage. You have " + playerHealth + " health remaining.");
					}
					if (playerHealth <= 0) {
						playerDied = true;
						inBattle = false;
						Writer.println("You were killed by the " + monster.getMonsterName() + ". Poor Alminster.");
						Writer.println();
					}
				}
				else {
					Writer.println("The monster's attack missed!");
				}
			}
		}	
		return playerDied;	
	}
	/**
	 * Processses command word to put into playerAttack(monster) function.
	 * @param command input by the user.
	 * @return result- if playerAttack on monster is executed.
	 */
	
	private boolean playerAttack(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what monster to attack 
			Writer.println("What monster are you attacking?");
			return result;
		}
		else {
			String monsterName = command.getRestOfLine().toLowerCase();
			Monster target = player.getRoom().getMonster();
			
			if (target != null && target.getMonsterName().toLowerCase().equals(monsterName)) {
					playerAttack(target);
					result = true;
				
			}
			else {
				Writer.println("There is no monster here with that name...");
			}
		
		}
		
		return result;
	}
	/**
	 * player attack on a room's monster.
	 * @param monster the monster in the current room of player.
	 */
	
	private void playerAttack(Monster monster) {
		inBattle = true;
		if (inBattle) {
			if (monster.getHitProbability() <= player.getHitProbability()) {
				int playerDamageDealt = player.damageDealt() + (player.getStrength() / 10);
				if(playerDamageDealt == 0) {
					Writer.print("You attacked the monster without a weapon! Do you need to equip one?");
				}
				
				int monsterHealth = monster.getMonsterHealth() - playerDamageDealt;
				monster.setMonsterHealth(monsterHealth);
				Writer.println(" You inflicted " + playerDamageDealt + " damage on " + monster.getMonsterName() + ". ");
				if (monsterHealth > 0) {
				Writer.println("The " + monster.getMonsterName() + "'s health is now " + monster.getMonsterHealth() + ".");
				}
				if(monsterHealth <= 0) {
					Writer.println("You have defeated the " + monster.getMonsterName() + "!");
					inBattle = false;
					points = points + 20;
					player.getRoom().setMonster(null);
				}

			}
			else {
				Writer.println("Your attack missed!");
			}
		}
	}	
	/**
	 * command look that prints player's location info. 
	 */
	private void look() {
		printLocationInformation();
	}
	/**
	 * Allows player to go back to previous room (unless it is their first turn).
	 */
	private void goBack(){
		boolean worked = player.goBack();
		if (worked) {
			printLocationInformation();
		}
		else { 
			Writer.println("You havent even started, you can't go back!");
		}
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();
		
		// Enter the main game loop. Here we repeatedly read commands and
		// execute them until the game is over.
		boolean wantToQuit = false;
		while (!wantToQuit) {
			Command command = Reader.getCommand();
			wantToQuit = processCommand(command);
		turns++;
		
			
			// other stuff that needs to happen every turn can be added here.
		}
		printGoodbye();
	}

	private void gameStatus() {
		Writer.println("You have earned " + points + " points.");
		Writer.println("You have taken " + turns + " turns.");
		printLocationInformation();
	}
	
	private void printLocationInformation() {
		Writer.println(player.getRoom().toString());
	}
	private void printInventory() {
		Writer.println(player.inventoryString());
	}
	
	private void dropItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("What item?");
		} else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item removedItem = player.removeItem(itemName);
			if(removedItem != null) {
				player.getRoom().addItem(itemName, removedItem);
				Writer.println("Dropped " + itemName + ".");
			}
			else {
				Writer.println("You do not have an item called " + itemName + ".");
			}
		}
	}
	private boolean equip(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Equip what?");
		}
		else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item equipItem = player.getRoom().getItem(itemName); 
			if (equipItem == null) {
				equipItem = player.getItem(itemName);
			}
			
			if(equipItem == null) {
				Writer.println(itemName + " is not in your inventory or the room!");
			}
			else if(equipItem.getName().toLowerCase().equals("dagger")) {
				player.setWeapon(equipItem);
				Writer.println("You have equipped " + itemName + " as your weapon. ");
				result = true;
			}
			else {
				Writer.println("You cant fight with that! Try a different item. ");
			}

		}

		return result;
	}

	private void takeItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Take what?");
		} else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item takenItem = player.getRoom().getItem(itemName);
			if(takenItem != null) {
			
				if (player.addItem(takenItem)) {
					Writer.println("Acquired " + itemName + ".");
					player.getRoom().removeItem(itemName);
				}
				else {
					Writer.println("You arent strong enough to carry this! ");
				}
				
			}
			else {
				Writer.println("There is nothing called " + itemName + " in this room.");
			}
		}
	}
	private void examineItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to examine
			Writer.println("Which item?");
		} else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item item = player.getRoom().getItem(itemName);
			if(item != null) {
				Writer.println(item.toString());

			}
			else {
				item = player.getItem(itemName);
				if (item != null) {

					Writer.println(item.toString());
				}
				else {
					Writer.println("There is no such item.");
				}

			}

		}

	}
	/**
	 * unlocks door in direction NESW.
	 * @param command direction.
	 * @return whether the door is locked (true) or unlocked (false).
	 */
	public boolean unlockDoor(Command command) {
		boolean result = true;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Unlock the door in which direction?");
		} else {
			String doorDirection = command.getRestOfLine();
			Door door = player.getRoom().getExit(doorDirection);
			if(door == null) {
				Writer.println("There is no door in direction: " + doorDirection + ".");
			}
			else if(!door.isLocked()){
				Writer.println("That door is already locked!");
			}
			else {
				Writer.println("Unlock With what?");
				String message = Reader.getResponse().toLowerCase();
				Item playerKey = player.getItem(message);
				if(playerKey == null) {
					Writer.println("You don't have an item called " + message + "." );
				}
				else {
					Item doorKey = door.getkey();
					if(playerKey.getName().toLowerCase().equals(doorKey.getName().toLowerCase())) {
						Writer.println("Unlocked!");
						result = false;
						door.setLocked(false);
					}
					else {
						Writer.println(playerKey.getName() + " is not the key to this door!");
					}
				}
			}	
		}
		
		return result;
	}
	/**
	 * drink potion if it is in inventory and drinkable.
	 * @param command the potion user wants to drink.
	 * @return false if not drank, true if drank successfully.
	 */
	public boolean drinkPotion(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Drink what?");
		}
		else {
			String itemName = command.getRestOfLine().toLowerCase();
			if (player.getItem(itemName) != null) {
				Item itemDrink = player.getItem(itemName);
				if (itemDrink.getName().toLowerCase().equals("health potion")){
					result = true;
					player.removeItem(itemName);
					player.getRoom().removeItem(itemName);
					player.setHealth(player.getHealth() + 50);
					int health = player.getHealth();
					Writer.println("Your health has been increased by 50 points. Your health is now " + health + ". ");
				}
				else if (itemDrink.getName().toLowerCase().equals("strength potion")){
					result = true;
					player.removeItem(itemName);
					player.getRoom().removeItem(itemName);
					player.setStrength(player.getStrength() + 50);
					int strength = player.getStrength();
					Writer.println("Your strength has been increase by 50 points. Your strength is now " + strength + ". ");
				}
				else if (itemDrink.getName().toLowerCase().equals("flying potion")) {
					result = true;
					player.removeItem(itemName);
					player.getRoom().removeItem(itemName);
					player.canFly(true);
					Writer.println("You now are able to fly, but only if you are in the presence of great danger. What constitutes grave danger? I guess you'll find out!");
				}
				else {
					Writer.println("You cant drink that!");
				}
			}
			else {
				Writer.println("You dont have that!");
			}
			
		}
		return result;
	}
	/**
	 * craft potion if player has recipe and all necessary ingredients.
	 * @param command the potionn the player wants to craft.
	 * @return whether potion was crafted or not.
	 */
	public boolean craftPotion(Command command) {
		boolean result = true;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to craft
			Writer.println("Craft what?");
		} 
		else {
			HashMap<String, Item> possessions = player.getInventory();
			HashMap<String, Item> possessionsRoom = player.getRoom().getItems();
			HashMap<String, Item> combinedItems = new HashMap<String, Item>();
			combinedItems.putAll(possessions);
			combinedItems.putAll(possessionsRoom);
			
			String itemName = command.getRestOfLine().toLowerCase();
			BuildableItem craftedItem = world.getBuildableItem(itemName);
			if (craftedItem != null) {
				if(craftedItem.canBuild(combinedItems)) {
					if (player.addItem(craftedItem)){
						Writer.println(craftedItem.getName() + " has been added to your inventory.");
					}
					else if (!player.addItem(craftedItem)) {
						player.getRoom().addItem(itemName, craftedItem);
						Writer.println(craftedItem.getName() + " has been added to the room.");
					}
					else  {
						Writer.println("You need to drop some items, your pouch is too heavy.");
					}
					craftedItem.setCompleted(true);

					// if can build
					HashMap<String, Item> ingredients = craftedItem.getIngredients();
					for (String name : ingredients.keySet()) {
						if (player.getItem(name) != null) {
							player.removeItem(name);
						}
						else {
							player.getRoom().removeItem(name);
						}
					}

				}
				else {
					Writer.println("You dont have the necessary ingredients to make that potion.");
				}

			}
			else {
				Writer.println("That isnt craftable!");
			}
		
		}
		return result;
	}
	/**
	 * locks door in direction NESW.
	 * @param command the direction of the door to be unlocked.
	 * @return false is locked, true if unlocked.
	 */
	public boolean lockDoor(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Lock the door in which direction?");
		} else {
			String doorDirection = command.getRestOfLine();
			Door door = player.getRoom().getExit(doorDirection);
			if(door == null) {
				Writer.println("There is no door in direction: " + doorDirection + ".");
			}
			else if(door.isLocked()){
				Writer.println("That door is already locked!");
			}
			else {
				Writer.println("Lock With what?");
				String message = Reader.getResponse().toLowerCase();
				Item playerKey = player.getItem(message);
				if(playerKey == null) {
					Writer.println("You don't have an item called: " + message + ".");
				}
				else {
					Item doorKey = door.getkey();
					if(playerKey.getName().toLowerCase().equals(doorKey.getName().toLowerCase())) {
						Writer.println("Locked!");
						result = true;
						door.setLocked(true);
					}
					else {
						Writer.println(playerKey.getName() + " is not the key to this door!");
					}
				}
			}	
		}
		
		return result;
	}
	/**Function to allow user to pack item into container item.
	 * @param command entered by user.
	 * @return whether or not item was successfully pack into the container.
	 */
	public boolean pack(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Pack what?");
		}
		else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item item = player.getRoom().getItem(itemName);
			if(item == null) {
				item = player.getItem(itemName);
				if (item == null) {
					Writer.println("You dont have that!");
					return result;
				}
			}

			if (!player.checkWeight(item)) {
				Writer.println("I'm not that buff!");
				return result;
			}
			Writer.println("What container do you want to put it in?");
			String message = Reader.getResponse();
			Item containerItem = player.getRoom().getItem(message);
			if(containerItem == null) {
				containerItem = player.getItem(message);
				if (containerItem == null) {
					Writer.println("You dont have that!");
					return result;
				}


				if(containerItem instanceof Container){
					Container asContainer = (Container) containerItem;

					if(asContainer.getName().equals("Lantern") && item.getName().equals("Fire charm")) {
						// If the container is the lantern && we are putting the fire charm in it
						asContainer.addItem(item); 
						Writer.println("You have packed " + item.getName() + " into the " +  asContainer.getName() + ".");
						Writer.println("Its a little less spooky out here now that you can see a bit!");
						result = true;
					}

					else{
						Writer.println("You can't pack " + item.getName() + " into " + asContainer.getName() + ".");
					}

				}
				else{
					Writer.println(containerItem.getName() + " is not a container!");
				}


			}
		}
		return result;
	}
	/**
	 * Allows user to unpack an item from inside a container.
	 * @param command user chosen item.
	 * @return result (true if successfully unpacked, false if unsuccessful).
	 */
	public boolean unpack(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know what item to drop
			Writer.println("Unpack what?");
		}
		else {
			String itemName = command.getRestOfLine().toLowerCase();
			Item item = player.getRoom().getItem(itemName);
			if(item == null) {
				item = player.getItem(itemName);
				if (item == null) {
					Writer.println("You dont have that!");
					return result;
				}
			}

			Writer.println("What container do you want to unpack from?");
			String message = Reader.getResponse();
			Item containerItem = player.getRoom().getItem(message);
			if(containerItem == null) {
				containerItem = player.getItem(message);
				if (containerItem == null) {
					Writer.println("You dont have that!");
					return result;
				}


				if(containerItem instanceof Container){
					Container asContainer = (Container) containerItem;

					if(asContainer.getName().equals("Lantern") && item.getName().equals("Fire charm")) {
						// If the container is the lantern && we are unpacking the fire charm from it
						asContainer.removeItem(item); 
						Writer.println("You have unpacked " + item.getName() + " from the " +  asContainer.getName() + ".");
						Writer.println("The moon is barely out tonight, I cant see a thing...");
						result = true;
					}

					else{
						Writer.println("You can't unpack " + item.getName() + " from the " + asContainer.getName() + ".");
					}

				}
				else{
					Writer.println(containerItem.getName() + " is not a container!");
				}


			}
		}
		return result;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Helper methods for processing the commands

	/**
	 * Given a command, process (that is: execute) the command.
	 *
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private boolean processCommand(Command command) {
		boolean wantToQuit = false;

		if (command.isUnknown()) {
			Writer.println("I don't know what you mean...");
		} else {

			CommandEnum commandWord = command.getCommandWord();
			boolean commandFinished = false;
			switch(commandWord) {
			case HELP:
				printHelp();
				break;
			case GO:
				if(inBattle == true) {
					Writer.println("You can't do that right now!");
				}
				else {
					wantToQuit = goRoom(command);
					commandFinished = true;
				}
				
				break;
			case QUIT:
				wantToQuit = quit(command);
				break;
			case LOOK:
				look();
				break;
			case STATUS:
				gameStatus();
				break;
			case BACK:
				if(inBattle) {
					Writer.println("You can't do that right now!");
				}
				else {
					goBack();  
					commandFinished = true;
				}   
				break;
			case INVENTORY:
				printInventory();
				break;
			case DROP:
				dropItem(command);
				break;
			case TAKE:
				takeItem(command);
				break;
			case EXAMINE: 
				examineItem(command);
				break;
			case UNLOCK:
				commandFinished = unlockDoor(command); 
				break;
			case LOCK:
				commandFinished = lockDoor(command);
				break;
			case UNPACK:
				commandFinished = unpack(command);
				break;
			case PACK:
				commandFinished = pack(command);
				break;
			case CRAFT:
				commandFinished = craftPotion(command);
				break;
			case EQUIP:
				equip(command);
				break;
			case DRINK:
				drinkPotion(command);
				break;
			case ATTACK:
				commandFinished = playerAttack(command);
				break;
			default:
				Writer.println(commandWord + " is not implemented yet!");
				break;		
			}
			
			if(commandFinished) {
				if(inBattle) {
					Monster roomMonster = player.getRoom().getMonster();
					if(roomMonster != null) {
						if(roomMonster.getMonsterHealth() > 0) {
							monsterAttack(roomMonster);
							if(player.getHealth() <= 0) {
								wantToQuit = true;
							}
						}
						else {
							Writer.println("You defeated the " + roomMonster.getMonsterName() + "!");
						}
					}
					else {
						inBattle = false;
					}
				}
			}
		}
		return wantToQuit;
	}

	///////////////////////////////////////////////////////////////////////////
	// Helper methods for implementing all of the commands.
	// It helps if you organize these in alphabetical order.

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 *
	 * @param command The command to be processed.
	 * @return result (true if they find Alminster).
	 */
	private boolean goRoom(Command command) {
		boolean result = false;
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			Writer.println("Go where?");
		} else {
			String direction = command.getRestOfLine();

			// Try to leave current.
			Door doorway = null;
			if (player.getRoom().getExit(direction) != null) {
				doorway = player.getRoom().getExit(direction);
			}

			if (doorway == null) {
				Writer.println("There is no door!");		
			} 
			else if (doorway != null && player.getRoom().getExit(direction).isLocked()) {
				Writer.println("This door is locked!");
			}
			else {
				Room newRoom = doorway.getDestination();
				player.setRoom(newRoom);
				points += player.getRoom().getPoints();
				printLocationInformation();
				if(newRoom.getName().equals("The end of the catacombs")) {
					Writer.println();
					Writer.println("You have found your bestie Alminster! Toad-ally great job!");
					Writer.println("Now to get Alminster in your bag before he pees on your hand...");
					Writer.println();
					result = true;
				}
				Monster roomMonster = newRoom.getMonster();
				if (roomMonster != null) {
					inBattle = true;
					Writer.println("You are being attacked by the " + roomMonster.getMonsterName() + "!");
				}
			}
		}
		
		return result;
	}

	/**
	 * Print out the closing message for the player.
	 */
	private void printGoodbye() {
		Writer.println("I hope you enjoyed your witchy adventures around the graveyard!");
		Writer.println("You have earned " + points + " points " + " in " + turns + " turns!");
		Writer.println("Thank you for playing.  Good bye.");
	}

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp() {
		Writer.println("You are lost. You are alone. You wander");
		Writer.println("around in the graveyard. ");
		Writer.println();
		Writer.println("Your command words are:");
		Writer.println(" look, status, back, go, quit, help, take, examine, drop, pack, unpack, equip, attack, craft, drink, unlock and lock");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		Writer.println();
		Writer.println("Alminster's Absence");
		Writer.println("You are Martha, a young witch who has lost her friend Alminster the toad, and must venture through the neighborhood graveyard to find him.");
		Writer.println("Explore the area, fight foes and craft potions with whatever you may find on your way.");
		Writer.println("If you need help, consult wicca-pedia! (Or type 'help' if you need help.)");
		Writer.println();
		printLocationInformation();
	}
	

	/**
	 * "Quit" was entered. Check the rest of the command to see whether we
	 * really quit the game.
	 *
	 * @param command
	 *            The command to be processed.
	 * @return true, if this command quits the game, false otherwise.
	 */
	private boolean quit(Command command) {
		boolean wantToQuit = true;
		if (command.hasSecondWord()) {
			Writer.println("Quit what?");
			wantToQuit = false;
		}
		return wantToQuit;
	}
}
