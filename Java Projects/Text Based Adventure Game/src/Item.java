
public class Item {
	/**
	 * String name is the name of the item within the world.
	 */
	private String name;
	/**
	 * The description of the item.
	 */
	private String description;
	/**
	 * The point value associated with the item.
	 */
	private int pointValue;
	/**
	 * The weight associated with the item.
	 */
	private int weight;
	
	// constructor for the Item class 
	public Item(String name, String description, int pointValue, int weight) {
		this.name = name;
		this.description = description;
		this.pointValue = pointValue;
		this.weight = weight;	
	}
	
	public String getName() {
		return name;
	}
	public String getDescription(){
		return description;
	}
	public int getPointValue() {
		return pointValue;
	}
	public int getWeight() {
		return weight;
	}
	public String toString() {
		String result = "";
		result = getName() + "\n" + getDescription() + "\n" + "The item weighs " + getWeight() + " pound(s). \n";
		return result;
	}
	
}
