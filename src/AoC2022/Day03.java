package AoC2022;

public class Day03 extends Day {

	//solution-part1:7581
	//solution-part2:2525
	
	public Day03() throws Exception {
		super();
	}
	
	public int returnPriority(String p1, String p2, String p3) {
//		Lowercase item types a through z have priorities 1 through 26.
//		Uppercase item types A through Z have priorities 27 through 52.
//		System.out.println("checking compartments");
//		System.out.println("p1:"+p1);
//		System.out.println("p2:"+p2);
//		System.out.println("p3:"+p3);
		//"z".charAt(0)
		int prio=0;
		for(int i=0;i<p1.length();i++) {
			String examChar = p1.substring(i,i+1);
			if(p2.indexOf(examChar)>=0 && (p3==null || p3.indexOf(examChar)>=0)) {
//				System.out.println("found prio:"+examChar);
				if(examChar.charAt(0)>=97) {
					prio= examChar.charAt(0)-96;
				} else {
					prio= examChar.charAt(0)-38;
				}
			}
		}
//		System.out.println("prio value:"+prio);
//		System.out.println("--------------");
		return prio;
	}

	public void execute() {
		String[] rucksacks = sb.toString().split("\n");
		long solution=0;
		for (String rucksack : rucksacks) {
			String p1=rucksack.substring(0,rucksack.length()/2);
			String p2=rucksack.substring(rucksack.length()/2);
			solution+=returnPriority(p1,p2,null);
		}
		System.out.println("solution-part1:"+solution);
	}
	
	public void execute2() {
		String[] rucksacks = sb.toString().split("\n");
		long solution=0;
		for (int i = 0; i < rucksacks.length; i+=3) {
			String p1=rucksacks[i];
			String p2=rucksacks[i+1];
			String p3=rucksacks[i+2];
			solution+=returnPriority(p1,p2,p3);
		}
		System.out.println("solution-part2:"+solution);
	}
	
	public static void main(String[] args) {
		Day03 d03 = null;
		try {
			d03 = new Day03();
			d03.readFile();
			d03.execute();
			d03.execute2();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
