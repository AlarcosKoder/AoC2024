package AoC2022;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Day02 {

	private File inputFile;
	private BufferedReader fileReader;
	
	public Day02() throws Exception {
		inputFile = new File(".\\src\\input_02.txt");
//		inputFile = new File(".\\src\\input_02_ctrl.txt");
		fileReader = new BufferedReader(new FileReader(inputFile));
	}
	
	public static void main(String[] args) {
		Day02 d02 = null;
		try {
			d02 = new Day02();
			d02.readFile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int eval(String opponent, String mine) {
		//A Rock 1 X
		//B Paper 2 Y
		//C Scissors 3 Z
		//X for Rock, Y for Paper, and Z for Scissors
		int score=0;
		if(mine.equals("X")){
			score+=1;
			if (opponent.equals("A"))score+=3;
			if (opponent.equals("C"))score+=6;
		} else if(mine.equals("Y")){
			score+=2;
			if (opponent.equals("A"))score+=6;
			if (opponent.equals("B"))score+=3;
		} else if(mine.equals("Z")){
			score+=3;
			if (opponent.equals("B"))score+=6;
			if (opponent.equals("C"))score+=3;
		}
		return score;
		//A-A 3
		//A-B 6
		//A-C 0
		//B-A 0
		//B-B 3
		//B-C 6
		//C-A 6
		//C-B 0
		//C-C 3
		
	}
	
	public int eval2(String opponent, String result) {
		int score=0;
		//X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win. Good luck!"
		//A Rock 1
		//B Paper 2
		//C Scissors 3
		if(result.equals("X")) { // loose
			if(opponent.equals("A"))score+=3;
			if(opponent.equals("B"))score+=1;
			if(opponent.equals("C"))score+=2;
		} else if(result.equals("Y")) { // draw
			score+=3;
			if(opponent.equals("A"))score+=1;
			if(opponent.equals("B"))score+=2;
			if(opponent.equals("C"))score+=3;
		} else if(result.equals("Z")) { // win
			score+=6;
			if(opponent.equals("A"))score+=2;
			if(opponent.equals("B"))score+=3;
			if(opponent.equals("C"))score+=1;
		}
			
			
			
		return score;
	}
	
	public void readFile()throws Exception  {
		String strCurrentLine;
		long result = 0;
		long result2 = 0;
		 	
		while ((strCurrentLine = fileReader.readLine()) != null) {
			String A = strCurrentLine.split(" ")[0];
			String B = strCurrentLine.split(" ")[1];
			System.out.println(A+":"+B);
			result += eval(A,B);
			result2 += eval2(A,B);
		}
		System.out.println("result:"+result);
		System.out.println("result2:"+result2);
	}

}
