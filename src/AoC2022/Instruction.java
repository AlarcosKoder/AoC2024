package AoC2022;

public class Instruction {

	public String name;
	public Long value;
	
	public Instruction(String line) {
		name = line.split(" ")[0];
		
		value = null;
		try {
			value = Long.parseLong(line.split(" ")[1]);
		} catch (Exception e) {
		}
	}

}
