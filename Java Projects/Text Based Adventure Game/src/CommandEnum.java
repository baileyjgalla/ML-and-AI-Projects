
public enum CommandEnum {
	/**
	 * Go.
	 */
	GO("go"),
	/**
	 * Quit.
	 */
	QUIT("quit"),
	/**
	 * Help.
	 */
	HELP("help"),
	/**
	 * Look.
	 */
	LOOK("look"),
	/**
	 * Status.
	 */
	STATUS("status"),
	/**
	 * Back.
	 */
	BACK("back"),
	/**
	 * Examine.
	 */
	EXAMINE("examine"),
	/**
	 * Take.
	 */
	TAKE("take"),
	/**
	 * Drop.
	 */
	DROP("drop"),
	/**
	 * Inventory.
	 */
	INVENTORY("inventory"),
	/** 
	 * Unlock.
	 */
	UNLOCK("unlock"),
	/**
	 * Lock.
	 */
	LOCK("lock"),
	/**
	 * Unpack.
	 */
	UNPACK("unpack"),
	/**
	 * Pack.
	 */
	PACK("pack"),
	/**
	 * Equip.
	 */
	EQUIP("equip"),
	/**
	 * Drink.
	 */
	DRINK("drink"),
	/**
	 * Craft.
	 */
	CRAFT("craft"), 
	/**
	 * Attack.
	 */
	ATTACK("attack");
	
	/**
	 * The text associated with the enum.
	 */
	private String text;
	
	private CommandEnum(String text) {
        this.text = text;
    }
	
	public String getText() {
		return text;
	}
}
