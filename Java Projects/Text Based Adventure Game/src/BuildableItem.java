import java.util.HashMap;

public class BuildableItem extends Item {
	/** HashMap used to hold items needed to make a potion.**/
	private HashMap<String, Item> ingredients;
	
	/** Whether or not the potion has been crafted.**/
	private boolean complete;
	public BuildableItem(String name, String description, int pointValue, int weight) {
		super(name, description, pointValue, weight);
		complete = false;
		ingredients = new HashMap<>();
	}
	/** getter for whether potion is crafted or not.
	 * @return complete.**/
	public boolean getCompleted() {
		return complete;
	}
	/** setter for whether potion is crafted or not.
	 * @param status whether or not the potion has been crafted.**/
	public void setCompleted(boolean status) {
		complete = status;
	}
	public HashMap<String, Item> getIngredients(){
		return ingredients;
	}
	public Item getItem(String name) {
		return ingredients.get(name);
	}
	public void addItem(Item item) {
		ingredients.put(item.getName(), item);	
	}
	
	public boolean canBuild(HashMap<String, Item> inventory) {	
		boolean result = false;
		for (Item item : ingredients.values()) {
			if (!inventory.containsValue(item)) {
				break;
			}
			else {
				result = true;			
			}
		}
		return result;
	}
}
