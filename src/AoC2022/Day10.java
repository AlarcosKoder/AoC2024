package AoC2022;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
	
	private File inputFile;
	private BufferedReader fileReader;
	private long xRegister;
	private int cyclePointer;
	private String CRT[];
	private long currentInstructionNumber;
	private long strength;
	private List<Instruction> instructions;
	
	public Day10()throws Exception {
		inputFile = new File(".\\src\\input_10.txt");
		fileReader = new BufferedReader(new FileReader(inputFile));
		xRegister = 1;
		cyclePointer=0;
		strength=0;
		currentInstructionNumber=0;
		instructions=new ArrayList<Instruction>();
		CRT = new String[6*40];
//		.#
		
	}
	public void stepCycleCounter() {
		//itt kell kiertekelni, hogy fedesben vannak e regX meg a cyclePointer
		int crtPointer=cyclePointer%40;
		
		if(xRegister-1==crtPointer)
			CRT[cyclePointer]="X";
		else if(xRegister==crtPointer)
			CRT[cyclePointer]="X";
		else if(xRegister+1==crtPointer)
			CRT[cyclePointer]="X";
		else 
			CRT[cyclePointer]=".";	
			
		cyclePointer++;
		if(cyclePointer==20||cyclePointer==60||cyclePointer==100||cyclePointer==140||cyclePointer==180||cyclePointer==220) {
			strength+=cyclePointer*xRegister;
		}
	}
	
	public void drawResult() {
		for (int i = 1; i < CRT.length+1; i++) {
			System.out.print(CRT[i-1]);
			if(i%40==0) {
				System.out.print("\n");
			}
		}
	}
	
	
	public void process() {
		long maxSize = instructions.size()*2;
		for (int i = 0; i < instructions.size(); i++) {
			Instruction instruction = instructions.get(i);
			if(instruction.name.equals("noop")) {
				stepCycleCounter();
			} else if(instruction.name.equals("addx")) {
				stepCycleCounter();
				stepCycleCounter();
				xRegister += instruction.value;
			}
//			System.out.println("reg:"+xRegister);
		}
		System.out.println("str:"+strength);
	}
	
	public void readFile()throws Exception  {
		String strCurrentLine;
		while ((strCurrentLine = fileReader.readLine()) != null) {
			instructions.add(new Instruction(strCurrentLine));
		}
	}
//XX..XX..XX..XX..XX..XX..XX..XX..XX..XX..
	
	public static void main(String[] args) {
		
		Day10 d10 = null;
		try {
			d10 = new Day10();
			d10.readFile();
			d10.process();
			d10.drawResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
