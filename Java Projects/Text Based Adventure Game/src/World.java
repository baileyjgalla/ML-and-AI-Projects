
import java.util.HashMap;

/**
 * This class represents the entire world that makes up the "Campus of Kings"
 * application. "Campus of Kings" is a very simple, text based adventure game.
 * Users can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 *
 * This world class creates the world where the game takes place.
 *
 * @author Maria Jump
 * @version 2015.02.01
 *
 * Used with permission from Dr. Maria Jump at Northeastern University
 */
public class World {
	/** The rooms in the world. */
	private HashMap<String, Room> rooms;
	/** The items needed to craft potions. **/
	private HashMap<String, BuildableItem> potionBuilding;
	
	/**
	 * Constructor for the world.
	 */
	public World() {
		potionBuilding = new HashMap<String, BuildableItem>();				
		rooms = new HashMap<String, Room>();
		createRooms();
		createItems();
	}
	/**
	 * gets the buildable item from the hashMap.
	 * @param name the name of the buildable item.
	 * @return the buildable item.
	 */
	public BuildableItem getBuildableItem(String name) {
		return potionBuilding.get(name);
	}	
	
	public void createItems() {
		Item ashes = createItem("Barren patch of dirt", "Ashes", "There is no way to tell what these are made of. ", 2, 1);
		Item ivy = createItem("A section of graves from 1800-1820", "Ivy", "I hope this isnt poison ivy, Id hate to get a rash. ", 4, 1);
		Item daisy = createItem("A section of well maintained plots", "Daisy petals", "I bet I could use these for something...", 1, 1);
		createItem("An overgrown pond", "Koi fish scale", "We could weigh this scale on a scale! ", 1, 1);
		createItem("Section of graves from 1700-1730", "Sprouting lily", "How pretty. ", 1, 1);
		createItem("Section of graves from 1700-1730", "Half of a potion recipe", "It looks like it was torn, maybe I can find the other piece. ", 1, 1);
		createItem("Section of graves from 1730-1740", "Copper coin", "This is very old, I dont recognize the markings. ", 3, 2);
		createItem("Section of graves that all bear your last name.", "Health Potion Recipe", "It is ingredients for a health potion. It reads: Craft a shiny stone, red cells that don't belong to you, and a needle's other half.", 4, 1);
		createItem("Section of graves that all bear your last name.", "Fire charm", "This is an enchantment that lets you hold fire in your hand. Sick. ", 3, 1);
		createItem("A moonlit path.", "Dagger", "A small dagger; it looks to be very old but it is still sharp ", 2, 4);
		Item rose = createItem("A moonlit path.", "Rose petals", " I could use these when I'm a flower girl. Or for potions, whichever. ", 2, 1);
		Item thread = createItem("A section of well maintained plots", "Spool of thread", "Its just ordinary cotton thread. I dont have a needle though...", 1, 3);
		Item onyx = createItem("A section of Nun's burial sites", "Onyx", "A chunk of onyx. It looks like it may have been a jewel at one time.", 3, 2);
		Item salt = createItem("A salt circle", "Salt", "I wonder what breaking this circle would do. Eh, why not? ", 1, 1);
		createItem("The groundskeeper's shed", "Watering can", "An old watering can. It's heavy even though its empty. ", 2, 12);
		Item map = createItem("The groundskeeper's shed", "Scrap of map", "A piece of a map written on parchment. I think its written in blood. How charming. ", 3, 1);
		createItem("The groundskeeper's shed", "Bag of crackers", "These are pretty stale. ", 1, 1);
		createItem("The skeleton's mausoleum", "Key ring", "A bunch of keys on a key chain. Whoever owns these <3's Las Vegas. ", 2, 2);
		createItem("An angel statue", "Strength Potion Recipe", "It is ingredients for a health potion. It reads: Craft a poisonous plant, a pretty-looking weed and some soot.", 2, 1);
		Item plasma = createItem("The first section of the catacombs", "Blue plasma", "Its slimey. ", 3, 2);
		createItem("A section of graves from 1900-1930", "Flying Potion recipe", "This seems like it would allow you to fly for a short time. It reads: Craft petals of a thorned flower, the opposite of pepper and a touch of colored goo.", 3, 1);
		
		Item skeletonKey = createItem("Barren patch of dirt", "Skeleton key", "I hope this isnt just someone's house key. ", 3, 1);
		Door lockedDoor = rooms.get("A large pile of leaves.".toLowerCase()).getExit("north");
		lockedDoor.setLocked(true);
		lockedDoor.setKey(skeletonKey);
		
		/** Creating buildable items. **/
		BuildableItem healthPotion = new BuildableItem("Health Potion", "Increases your health by 50%.", 10, 2);
		BuildableItem flyingPotion = new BuildableItem("Flying Potion", "Allows you to fly for a short time, but only when their is grave danger.", 10, 2);
		BuildableItem strengthPotion = new BuildableItem("Strength Potion", "Increases your strength by 50%.", 10, 2);
		Container lantern = new Container("Lantern", "An old iron lantern. It doesnt have a flame though, and the knob doesnt seem to work. ", 2, 10);
		Room containerRoom = rooms.get("Upstairs in the Mausoleum".toLowerCase());	
	
		if (containerRoom != null) {
			
			containerRoom.addItem(lantern.getName(),  lantern);
		}
		
		/** Adding ingredients to ingredient hashMap**/
		healthPotion.addItem(onyx);
		healthPotion.addItem(map);
		healthPotion.addItem(thread);
		flyingPotion.addItem(plasma); 
		flyingPotion.addItem(rose); 
		flyingPotion.addItem(salt); 
		strengthPotion.addItem(ashes);
		strengthPotion.addItem(daisy);
		strengthPotion.addItem(ivy);
		 
		
		/** Adding buildable items to my potion building hashMap. **/
		potionBuilding.put(flyingPotion.getName().toLowerCase(), flyingPotion);
		potionBuilding.put(healthPotion.getName().toLowerCase(), healthPotion);
		potionBuilding.put(strengthPotion.getName().toLowerCase(), strengthPotion);
	}
	
	private Item createItem(String roomName, String itemName, String description, int points, int weight) {
		Room room = rooms.get(roomName.toLowerCase());	
		Item addedItem = null;
		if (room != null) {
			addedItem = new Item(itemName, description, points, weight);
			room.addItem(itemName,  addedItem);
		}
		
		return addedItem;
	}
	

	/**
	 * This method takes care of creating all of the aspects of the world for
	 * the "Campus of Kings" application.
	 *
	 * @param name
	 *            The provided name of the room.
	 * @return The room associated with the provided name
	 */
	public Room getRoom(String name) {
		return rooms.get(name.toLowerCase());
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// Start of private helper methods

	/**
	 * Helper method for recreating a Room. Ensure that the room is created and
	 * installed in to the collection of Rooms
	 *
	 * @param theRoom
	 *            The room to add to the world.
	 */
	private void addRoom(Room theRoom) {
		rooms.put(theRoom.getName().toLowerCase(), theRoom);
	}

	/**
	 * Helper method for creating doors between rooms.
	 * @param from The room where the door originates.
	 * @param direction The direction of the door in the room from.
	 * @param to The room where the door goes.
	 */
	private void createDoor(Room from, String direction, Room to) {
		Door door = new Door(to);
		from.setExit(direction, door);	
	}
	/**
	 * This method creates all of the individual places in this world and all
	 * the doors connecting them.
	 */
	private void createRooms() {
		
		// creating monsters for rooms
		Monster skeletalRat = new Monster("Skeletal rat", 100, 25);
		Monster skeletalBat = new Monster("Skeletal bat", 100, 40);
		
		// Creating all the rooms.
		Room graves1610 = new Room("Section of graves from 1610-1640", "An ordinary spooky section of graves. Its the darkest time of night but luckily the moon throws some light.", null);
		Room graves1640 = new Room("Section of graves from 1640-1660", "An ordinary and slightly fresher section of graves. A cat scurries away when you approach. ", null);
		Room graves1660 = new Room("Section of graves from 1660-1680", "Who would have guessed? More graves. Slightly newer. You pray for a change of scenery.", null);
		Room graves1680 = new Room("Section of graves from 1680-1700", "You guessed it. These appear to be dating up to the eighteenth century.", null);
		Room reverendsMausoleum = new Room("Reverends Mausoleum", "You encounter the ghost of the Reverend. He does not speak, but floats around his tomb. There is a large cross on the wall in the tomb and the bottom of a narrow stairwell visible.", null);
		Room upstairsMausoleum = new Room("Upstairs in the Mausoleum", "The ghost stays downstairs. You missed having company.", null);
		Room overgrownPond = new Room("An overgrown pond", "The pond is teeming with creatures, and you can see koi fish through the water. You hear a few frog's ribbits, but theyre not Alminster's. (He's not a social butter-frog)", null);
		Room graves1700 = new Room("Section of graves from 1700-1730", "This site has large towering graves compared to the ones you have seen from earlier time periods", null);
		Room patchOfDirt = new Room("Barren patch of dirt", "Mysteriously, the entire area is bare earth with the exception of a grayish mass you see a few yards away.", null);
		Room graves1730 = new Room("Section of graves from 1730-1740", "You see small flat graves with the exception of one large statue.", null);
		Room graves1740 = new Room("Section of graves from 1740-1750", "The graves here look similar to those of the decade behind.", null);
		Room marthasRelatives = new Room("Section of graves that all bear your last name.", "The graves all appear to be those related to you in some way. Probably insignificant!", null);
		Room moonlitPath = new Room("A moonlit path." , " This seems to be a normal path walked by visitors. Bushes cover one side.", null);
		Room priestsCrypt = new Room("A priest's crypt", "You see a large and daunting crypt.", null );
		Room wellTendedPlots = new Room("A section of well maintained plots", "Here the plots are well manicured and all have flowers or wreaths bestowed on them. You see daisy petals scattered.", null);
		Room nunsPlots = new Room("A section of Nun's burial sites", "You hear rustling. Ominous.", null);
		Room graves1800 = new Room("A section of graves from 1800-1820", "A normal section of graves with ivy grown over the faces.", null);
		Room saltCircle = new Room("A salt circle", "As you approach you see figures running away. You call out to them but get no response. They looked like they were wearing cloaks like yours.", null);
		Room groundskeepersShed = new Room("The groundskeeper's shed", "A small shed where tools are kept. Inside, it looks like a worker ran out in the middle of lunch.", null);
		Room petSemetary = new Room("A pet semetary", "You wonder if Stephen King is strict about his intellectual property. You see spirits of passed animals. You call out but realize they dont answer, since they are animals and also dead.", null);
		Room graves1820 = new Room("A section of graves spanning from 1820-1840", "An ordinary section of graves.", null);
		Room graves1840 = new Room("A section of graves spanning from 1840-1860", "An ordinary section of graves.", null);
		Room graves1880 = new Room("A section of graves spanning from 1880-1900", "You wonder why the graves seem to have skipped over 1860-1880.", null);
		Room woodenTrapdoor = new Room("A large pile of leaves.", "Definitely shouldnt investigate that. Actually, it looks like theres something underneath the pile. It looks like a trap door to the north, but I cant open it.", null);
		Room skeletonMausoleum = new Room("The skeleton's mausoleum", "You approach a large tomb that looks like it has been converted into a clubhouse. There are DO NOT ENTER signs painted with ash and the door is locked.", null);
		Room angelStatue = new Room("An angel statue", "You see a large statue about six feet high and made of stone. Atop the statue is a crown.", null);
		Room graves1900 = new Room("A section of graves from 1900-1930", "An ordinary section of graves.", null);
		Room catacombsA = new Room("The first section of the catacombs", "There is blood on the walls and a message that says to be prepared to face foes or death. Maybe I should drink my potions now...", null);
		Room catacombsB = new Room("Another subsection of the catacombs", "You come across an infinity pool. You see a siren and she bids you to come closer. You think you should probablyyy not.", null);
		Room catacombsC = new Room("Further into the catacombs", "You hear ribbits from far away. Looking around, skeletal rats scurrying around the room. They bite.", skeletalRat);
		Room catacombsD = new Room("Deeper into the catacombs", "You hear flapping, and a large skeletal bat is flying towards you. He looks mad.", skeletalBat);
		Room catacombsE = new Room("The end of the catacombs", "There is a mysterious pool of water here, there seems to be no movement of the liquid. You hear muffled ribbits.", null);
		Room graves2000 = new Room("A section of graves from 2000", "A more modern section of graves.", null);
		Room graves1980 = new Room("A section of graves spanning from 1980 to 2000", "An ordinary section of graves.", null);
		Room graves1950 = new Room("A section of graves from 1950-1980", "Some names you see resemble those of people you have known. One grave seems to have been dug up recently.", null);
		Room patchOfFlowers = new Room("A pretty patch of flowers", "There are roses, lillies and daises around you.", null);
		Room freshGraves = new Room("A section of very fresh graves", "There is exposed dirt on top the graves, it looks like clay.", null);
		Room pileOfMarkers = new Room("A pile of grave markers", "You see a tossed away flower arrangements and old wreaths on the top of the pile. There is a rustling from the pile.", null);
		Room sparseForest = new Room("Sparse forest", "You see a skeleton hanging from a noose from a tree. Below is a warning that all who pass that point will reach the same fate.", null);
		Room graves1750 = new Room("A section of graves from 1750-1770", "An ordinary section of graves.", null);
		
		
		// Adding all the rooms to the world.
		this.addRoom(graves1610);
		this.addRoom(graves1640);
		this.addRoom(graves1660);
		this.addRoom(graves1680);
		this.addRoom(reverendsMausoleum);
		this.addRoom(upstairsMausoleum);
		this.addRoom(overgrownPond);
		this.addRoom(graves1700);
		this.addRoom(patchOfDirt);
		this.addRoom(graves1730);
		this.addRoom(marthasRelatives);
		this.addRoom(moonlitPath);
		this.addRoom(priestsCrypt);
		this.addRoom(wellTendedPlots);
		this.addRoom(nunsPlots);
		this.addRoom(graves1800);
		this.addRoom(saltCircle);
		this.addRoom(groundskeepersShed);
		this.addRoom(petSemetary);
		this.addRoom(graves1820);
		this.addRoom(graves1840);
		this.addRoom(graves1880);
		this.addRoom(woodenTrapdoor);
		this.addRoom(skeletonMausoleum);
		this.addRoom(angelStatue);
		this.addRoom(graves1900);
		this.addRoom(catacombsA);
		this.addRoom(catacombsB);
		this.addRoom(catacombsC);
		this.addRoom(catacombsD);
		this.addRoom(catacombsE);
		this.addRoom(graves2000);
		this.addRoom(graves1980);
		this.addRoom(graves1950);
		this.addRoom(patchOfFlowers);
		this.addRoom(freshGraves);
		this.addRoom(pileOfMarkers);
		this.addRoom(sparseForest);
		this.addRoom(graves1750);
		this.addRoom(graves1740);
		
		
		// Creating all the doors between the rooms.
			this.createDoor(graves1610,"north", reverendsMausoleum);
			this.createDoor(graves1610, "east", graves1640);
			this.createDoor(graves1640, "west", graves1610);
			this.createDoor(graves1660, "west", graves1640);
			this.createDoor(graves1680, "west", graves1660);
			this.createDoor(graves1640,"east", graves1660);
			this.createDoor(graves1660, "east", graves1680);
			this.createDoor(graves1640, "north",overgrownPond);
			this.createDoor(graves1660, "north", graves1700);
			this.createDoor(graves1680, "north", graves1730);
			this.createDoor(reverendsMausoleum, "south", graves1610);
			this.createDoor(overgrownPond, "south", graves1640);
			this.createDoor(graves1700, "south", graves1660);
			this.createDoor(graves1730, "south", graves1680);
			this.createDoor(overgrownPond, "west", reverendsMausoleum);
			this.createDoor(reverendsMausoleum, "east", overgrownPond);
			this.createDoor(reverendsMausoleum, "north", upstairsMausoleum);
			this.createDoor(upstairsMausoleum, "south", reverendsMausoleum);
			this.createDoor(upstairsMausoleum, "east", graves1750);
			this.createDoor(graves1750, "west", upstairsMausoleum);
			this.createDoor(overgrownPond, "north", graves1750);
			this.createDoor(graves1750, "south", overgrownPond);
			this.createDoor(overgrownPond, "east", graves1700);
			this.createDoor(graves1700, "west", overgrownPond);
			this.createDoor(graves1700, "east", graves1730);
			this.createDoor(graves1730, "west", graves1700);
			this.createDoor(graves1700, "north", patchOfDirt);
			this.createDoor(patchOfDirt, "south", graves1700);
			this.createDoor(graves1730, "north", graves1740);
			this.createDoor(graves1740, "south", graves1730);
			this.createDoor(graves1740, "west", patchOfDirt);
			this.createDoor(patchOfDirt, "east", graves1740);
			this.createDoor(patchOfDirt, "west", graves1750);
			this.createDoor(graves1750, "east", patchOfDirt);
			this.createDoor(graves1750, "north", moonlitPath);
			this.createDoor(moonlitPath, "south", graves1750);
			this.createDoor(patchOfDirt, "north", priestsCrypt);
			this.createDoor(priestsCrypt, "south", patchOfDirt);
			this.createDoor(graves1740, "north", wellTendedPlots);
			this.createDoor(wellTendedPlots, "south", graves1740);
			this.createDoor(wellTendedPlots, "west",  priestsCrypt);
			this.createDoor(priestsCrypt, "east",  wellTendedPlots);
			this.createDoor(priestsCrypt, "west", moonlitPath);
			this.createDoor(moonlitPath, "east", priestsCrypt);
			this.createDoor(moonlitPath, "west", marthasRelatives);
			this.createDoor(marthasRelatives, "east",  moonlitPath);
			this.createDoor(wellTendedPlots, "north", groundskeepersShed);
			this.createDoor(groundskeepersShed, "south", wellTendedPlots);
			this.createDoor(priestsCrypt, "north", saltCircle);
			this.createDoor(nunsPlots, "north", petSemetary);
			this.createDoor(petSemetary, "south", nunsPlots);
			this.createDoor(petSemetary, "north", woodenTrapdoor);
			this.createDoor(woodenTrapdoor, "south", petSemetary);
			this.createDoor(saltCircle, "south", priestsCrypt);
			this.createDoor(moonlitPath, "north", graves1800);
			this.createDoor(graves1800, "south", moonlitPath);
			this.createDoor(groundskeepersShed, "west", saltCircle);
			this.createDoor(saltCircle, "east", groundskeepersShed);
			this.createDoor(saltCircle, "west", graves1800);
			this.createDoor(graves1800, "east", saltCircle);
			this.createDoor(graves1800, "west", nunsPlots);
			this.createDoor(nunsPlots, "east", graves1800);
			this.createDoor(marthasRelatives, "north", nunsPlots);
			this.createDoor(nunsPlots, "south", marthasRelatives);
			this.createDoor(groundskeepersShed, "north", graves1880);
			this.createDoor(graves1880, "south", groundskeepersShed);
			this.createDoor(saltCircle, "north", graves1840);
			this.createDoor(graves1840, "south", saltCircle);
			this.createDoor(graves1800, "north", graves1820);
			this.createDoor(graves1820, "south", graves1800);
			this.createDoor(graves1880, "west", graves1840);
			this.createDoor(graves1840, "east", graves1880);
			this.createDoor(graves1840, "west", graves1820);
			this.createDoor(graves1820, "east", graves1840);
			this.createDoor(graves1820, "west", petSemetary);
			this.createDoor(petSemetary, "east", graves1820);
			this.createDoor(graves1880, "north", graves1900);
			this.createDoor(graves1900, "south", graves1880);
			this.createDoor(graves1840, "north", angelStatue);
			this.createDoor(angelStatue, "south", graves1840);
			this.createDoor(graves1820, "north", skeletonMausoleum);
			this.createDoor(skeletonMausoleum, "south", graves1820);
			this.createDoor(graves1900, "west", angelStatue);
			this.createDoor(angelStatue, "east", graves1900);
			this.createDoor(angelStatue, "west", skeletonMausoleum);
			this.createDoor(skeletonMausoleum, "east", angelStatue);
			this.createDoor(skeletonMausoleum, "west", woodenTrapdoor);
			this.createDoor(woodenTrapdoor, "east", skeletonMausoleum);
			this.createDoor(woodenTrapdoor, "north", catacombsA);
			this.createDoor(catacombsA, "south", woodenTrapdoor);
			this.createDoor(skeletonMausoleum, "north", graves2000);
			this.createDoor(graves2000, "south", skeletonMausoleum);
			this.createDoor(angelStatue, "north", graves1980);
			this.createDoor(graves1980, "south", angelStatue);
			this.createDoor(graves1900, "north", graves1950);
			this.createDoor(graves1950, "south", graves1900);
			this.createDoor(graves1950, "west", graves1980);
			this.createDoor(graves1980, "east", graves1950);
			this.createDoor(graves1980, "west", graves2000);
			this.createDoor(graves2000, "east", graves1980);
			this.createDoor(graves1950, "north", patchOfFlowers);
			this.createDoor(patchOfFlowers, "south", graves1950);
			this.createDoor(graves1980, "north", freshGraves);
			this.createDoor(freshGraves, "south", graves1980);
			this.createDoor(graves2000, "north", pileOfMarkers);
			this.createDoor(pileOfMarkers, "south", graves2000);
			this.createDoor(patchOfFlowers, "west", freshGraves);
			this.createDoor(freshGraves, "east", patchOfFlowers);
			this.createDoor(freshGraves, "west", pileOfMarkers);
			this.createDoor(pileOfMarkers, "east", freshGraves);
			this.createDoor(pileOfMarkers, "west", sparseForest);
			this.createDoor(sparseForest, "east", pileOfMarkers);
			this.createDoor(catacombsA, "east", catacombsB);
			this.createDoor(catacombsB, "west", catacombsA);
			this.createDoor(catacombsA, "west", catacombsD);
			this.createDoor(catacombsD, "east", catacombsA);
			this.createDoor(catacombsA, "north", catacombsC);
			this.createDoor(catacombsC, "south", catacombsA);
			this.createDoor(catacombsD, "north", catacombsE);
			this.createDoor(catacombsE, "south", catacombsD);
		
		
		//Adding point values to special rooms
		
		graves1680.setPoints(5);
		graves1640.setPoints(5);
		overgrownPond.setPoints(5);
		reverendsMausoleum.setPoints(5);
		patchOfDirt.setPoints(5);
		upstairsMausoleum.setPoints(5);
		priestsCrypt.setPoints(5);
		groundskeepersShed.setPoints(5);
		nunsPlots.setPoints(5);
		saltCircle.setPoints(5);
		petSemetary.setPoints(5);
		skeletonMausoleum.setPoints(5);
		woodenTrapdoor.setPoints(5);
		catacombsA.setPoints(15);
		catacombsB.setPoints(15);
		catacombsC.setPoints(15);
		catacombsD.setPoints(15);
		catacombsE.setPoints(50);
		freshGraves.setPoints(5);
		pileOfMarkers.setPoints(5);
			
		
		
	}
}
