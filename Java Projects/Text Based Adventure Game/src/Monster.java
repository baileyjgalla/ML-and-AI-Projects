
import java.util.Random;

public class Monster {
	/** String used to store monster's name. **/
	private String name;
	/** int used to store total Health of monster. */
	private int monsterHealth;
	/** int used to store Damage dealt by monster onto player. **/
	private int damageDealt;
	/** randomized int to signify chance of being hit by monster.**/
	private int hitProbability;
	
	public Monster(String name, int monsterHealth, int damageDealt) {
		this.name = name;
		this.monsterHealth = monsterHealth;
		this.damageDealt = damageDealt;
	}
	
	public int getHitProbability() {
		Random probability = new Random();
		hitProbability = probability.nextInt(100);
		return hitProbability;
	}
	public int getDamage() {
		return damageDealt;
	}
	public int getMonsterHealth() {
		return monsterHealth;
	}
	public void setMonsterHealth(int health) {
		monsterHealth = health;
	}
	public String getMonsterName() {
		return name;
	}
	
	
	
	
	
	
	
	
}
