package AoC2022;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Day01 {

	private File inputFile;
	private BufferedReader fileReader;
	private long maxValue;
	
	private long max1Value;
	private long max2Value;
	private long max3Value;
		
	public static void main(String[] args) {
		Day01 d01 = null;
		try {
			d01 = new Day01();
			d01.readFile();
			
		} catch (Exception e) {
			System.out.println("maxValue = "+String.valueOf(d01.maxValue));
			e.printStackTrace();
		}
		
		
	}
	
	public Day01() throws Exception {
		maxValue = 0;
		max1Value = 0;
		max2Value = 0;
		max3Value = 0;
		inputFile = new File(".\\src\\input.txt");
		fileReader = new BufferedReader(new FileReader(inputFile));
	}
	
	public long readBlock()throws Exception  {
		long blockSize = 0;
		String strCurrentLine;
		
//		while ((strCurrentLine = objReader.readLine()) != null) {
//
//		    System.out.println(strCurrentLine);
//		   }
		
		while ((strCurrentLine = fileReader.readLine()) != null && !strCurrentLine.isEmpty()) {
			
			blockSize += Long.parseLong(strCurrentLine);
			System.out.println("l:"+strCurrentLine);
			System.out.println("bs: "+blockSize);
		}
		
		return blockSize;
	}
	
	public void readFile()throws Exception  {
		
		
		String strCurrentLine;
		long blockSize = 0;
		 	
		while ((strCurrentLine = fileReader.readLine()) != null) {
			
			if(strCurrentLine.isEmpty()) {
//				System.out.println("most");
//				if (blockSize>maxValue)maxValue=blockSize;
//				System.out.println("blocksize: "+blockSize);
//				System.out.println("maxvalue: "+maxValue);
//				blockSize = 0;
				
				if (blockSize>max1Value) {
					max1Value=blockSize;
				} else if(blockSize>max2Value) {
					max2Value = blockSize;
				} else if(blockSize>max3Value) {
					max3Value = blockSize;
				}
				System.out.println("blocksize:"+blockSize);
				blockSize=0;
			} else {
				blockSize += Long.parseLong(strCurrentLine);
			}
				
				
				
//		    System.out.println(strCurrentLine);
		}
		System.out.println("max1value: "+max1Value);
		System.out.println("max2value: "+max2Value);
		System.out.println("max3value: "+max3Value);
		System.out.println("sum:"+String.valueOf(max1Value+max2Value+max3Value));

//		while (true) {
//		   
//			long currentBlock = readBlock();
//			if(currentBlock > maxValue) maxValue=currentBlock;
//			System.out.println("mv: "+maxValue);
//				   
//	   }
	   
	}
}
