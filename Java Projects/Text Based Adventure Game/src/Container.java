import java.util.HashMap;

public class Container extends Item{
	/**
	 * HashMap containing all items of a container player utilizes.
	 */
	private HashMap<String, Item> container;

	public Container(String name, String description, int pointValue, int weight) {
		super(name, description, pointValue, weight);
		container = new HashMap<>();
		
	}
	public void addItem(Item item) {
		container.put(item.getName(), item);
	}
	
	public Item removeItem(String name) {
		
		Item removal = container.get(name);
		if (removal != null) {
			container.remove(name);
		}
		return removal;
	}
	
	public Item removeItem(Item item) {
		
		Item removal = container.get(item.getName());
		if (removal != null) {
			container.remove(item.getName());
		}
		return removal;
	}
	

	public String toString() {
		String result = "";
		for (String item: container.keySet()) {
			result = result + container.get(item);
		}
		result = result + super.toString();
		return result;
	}
	
	@Override
	public int getWeight() {
		int sum = 0;
		for (Item item : container.values()) {
			sum = sum + item.getWeight();
		}
		sum = sum + super.getWeight();
		
		return sum;
	}

	
}
