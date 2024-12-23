package AoC2022;

public class Day7 extends Day {

	private static final int MARKER_LENGTH = 14;
	
	public Day7() {
		super();
	}
	
	public void processString() {
		StringBuffer marker = new StringBuffer();
		
		int i;
		for (i= 0; i < sb.length()-4; i++) {
//			System.out.println("------");
//			System.out.println("input: "+sb.toString());
			
			String candidate = sb.substring(i, i+1);
//			System.out.println("current candid: "+candidate);
			
			if (marker.indexOf(candidate)>=0) {
				//ha ez a karakter benne volt, akkor az elozo karakter utani lehet a kovetkezo marker eleje
				String newMarker = marker.substring(marker.indexOf(candidate)+1);
				marker = new StringBuffer(newMarker);
			}
			marker.append(candidate);
//			System.out.println("current marker: "+marker);
			if(marker.length()==MARKER_LENGTH) {
				break;
			}
		}
		i++;
		System.out.println("megvan: "+i);
		
	}
	
//	public void processString() {
//		Character c1;
//		Character c2;
//		Character c3;
//		Character c4;
//		int i;
//		for (i= 0; i < sb.length()-4; i++) {
//			c1=sb.charAt(i);
//			c2=sb.charAt(i+1);
//			c3=sb.charAt(i+2);
//			c4=sb.charAt(i+3);
//			
//			if(c1!=c2 && c2!=c3 && c3!=c4 && c1!=c3 && c1!=c4 && c2!=c4)
//				break;
//		}
//		i+=4;
//		System.out.println("megvan: "+i);
//		
//	}
	
	public static void main(String[] args) {
		Day7 d06=new Day7();
		d06.readFile();
		d06.processString();
	}

}
