package AoC2022;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day11 {

	enum OPERATION_ENUM {
		ADD,
		MULTIPLE,
		SQUARE
	}
	
	public class Monkey {
		long number;
		List<BigInteger> items;
		int indexIfTRUE;
		int indexIfFALSE;
		BigInteger testValue;
		OPERATION_ENUM operation;
		long inspectionCounter;
		
		BigInteger operationValue;
		
		Monkey(StringBuffer sb){
			int monkeyStart = sb.indexOf("Monkey ");
			int itemStart = sb.indexOf("Starting items:");
			int opStart = sb.indexOf("Operation: new = ");
			int testStart = sb.indexOf("Test: divisible by ");
			int ifTrueStart = sb.indexOf("If true: throw to monkey");
			int ifFalseStart = sb.indexOf("If false: throw to monkey");
			
			inspectionCounter = 0;
			operationValue = BigInteger.valueOf(0);
			
			try {
				number = Long.parseLong(sb.subSequence(7, 8).toString().trim());
				String[] stringItems = sb.substring(itemStart+15,opStart).replaceAll(" ","").split(",");
				items = new ArrayList<>();
				for (int i = 0; i < stringItems.length; i++) {
					items.add(BigInteger.valueOf(Long.parseLong(stringItems[i])));
							
				}
				String operationStr = sb.substring(opStart+20,testStart).toString().replaceAll(" ","");
				if(operationStr.substring(0, 2).equals("*o")) {
					operation=OPERATION_ENUM.SQUARE;
				} else if(operationStr.substring(0, 1).equals("*")) {
					operation=OPERATION_ENUM.MULTIPLE;
					
					operationValue = BigInteger.valueOf(Long.parseLong(operationStr.replaceAll("\\*", "")));
				} else if(operationStr.substring(0, 1).equals("+")) {
					operation=OPERATION_ENUM.ADD;
					operationValue = BigInteger.valueOf(Long.parseLong(operationStr.replaceAll("\\+", "")));
				}
				String testValueStr = sb.substring(testStart+19,ifTrueStart).toString().replaceAll(" ","");
				testValue = BigInteger.valueOf(Long.parseLong(testValueStr));

				String ifTrue = sb.substring(ifTrueStart+24,ifFalseStart).toString().replaceAll(" ","");
				String ifFalse = sb.substring(ifFalseStart+25,sb.length()).toString().replaceAll(" ","");
			
				indexIfTRUE=Integer.parseInt(ifTrue);
				indexIfFALSE=Integer.parseInt(ifFalse);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public BigInteger evalValue(BigInteger value) {
			BigInteger newValue = null;
			switch (operation) {
				case ADD: {
					newValue=value.add(operationValue);
//					System.out.println("\t\tWorry level is added by "+operationValue +" to "+newValue);
					break;
				}
				case MULTIPLE: {
					newValue=value.multiply(operationValue);
//					System.out.println("\t\tWorry level is multiplied by "+operationValue +" to "+newValue);
					break;
				}
				case SQUARE: {
					newValue=value.multiply(value);
//					System.out.println("\t\tWorry level is squared to "+newValue);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + operation);
				}
			return newValue;
		}
		
	}
	
	// 2x Cikkszám: 118103 + 1x Cikkszám: 118126E
	
	private File inputFile;
	private BufferedReader fileReader;
	private List<Monkey> monkeys;
	private BigInteger kozos;
	
	public Day11() throws Exception {
		inputFile = new File(".\\src\\input_11.txt");
//		inputFile = new File(".\\src\\input_11_ctrl.txt");
		fileReader = new BufferedReader(new FileReader(inputFile));
		monkeys = new ArrayList<>();
		kozos = BigInteger.valueOf(1);
	}
	
	public void evalMonkeys() {
		for (int i = 1; i <= 10000; i++) {
			
			for (Monkey monkey : monkeys) {
				
//				System.out.println("Monkey "+monkey.number+":");
				
				for (BigInteger item : monkey.items) {
//					System.out.println("\tMonkey inspects an item with a worry level of "+item);
					monkey.inspectionCounter++;
//					BigInteger worryLevel = monkey.evalValue(item).divide(BigInteger.valueOf(3));
					BigInteger worryLevel = monkey.evalValue(item);
					worryLevel=worryLevel.mod(kozos);
					
//					System.out.println("\t\tMonkey gets bored with item. Worry level is divided by 3 to "+worryLevel);
					if(worryLevel.mod(monkey.testValue) == BigInteger.valueOf(0)) {
//						System.out.println("\t\tCurrent worry level is divisible by "+monkey.testValue);
						monkeys.get(monkey.indexIfTRUE).items.add(worryLevel);
//						System.out.println("\t\tItem with worry level "+worryLevel+" is thrown to monkey "+monkey.indexIfTRUE);
						
					} else {
//						System.out.println("\t\tCurrent worry level is NOT divisible by "+monkey.testValue);
						monkeys.get(monkey.indexIfFALSE).items.add(worryLevel);
//						System.out.println("\t\tItem with worry level "+worryLevel+" is thrown to monkey "+monkey.indexIfFALSE);
					}
					
				}
				monkey.items.clear();
			}
			
//			for (Monkey monkey : monkeys) {
//				System.out.print("Monkey "+monkey.number+": ");
//				for (int j = 0; j < monkey.items.size(); j++) {
//					System.out.print(monkey.items.get(j)+", ");
//				}
//				System.out.println();
//			}
			if (i==1 || i==20 || i%1000==0) {
				System.out.println("\nROUND "+i+" FINISHED");
				for (Monkey monkey : monkeys) {
					System.out.println("Monkey "+monkey.number+" inspected items "+monkey.inspectionCounter);
				}
			}	
		}
		
	}
	
	public void finalResult() {
		List<Long> result = new ArrayList<>();
		for (Monkey monkey : monkeys) {
			result.add(monkey.inspectionCounter);
		}
		Collections.sort(result);
		System.out.println("1st:" + result.get(result.size()-1));
		System.out.println("2nd:" + result.get(result.size()-2));
		System.out.println("resut: "+( result.get(result.size()-1)*result.get(result.size()-2)));
	}
	
	public void readFile()throws Exception {
		String strCurrentLine;
		StringBuffer sb = new StringBuffer();
		while ((strCurrentLine = fileReader.readLine()) != null) {
			if(strCurrentLine.length()>0) {
				sb.append(strCurrentLine);
			} else {
				Monkey m = new Monkey(sb);
				monkeys.add(m);
				kozos=kozos.multiply(m.testValue);
				sb.delete(0, sb.length());
			}
		}
	}

	public static void main(String[] args) {
		Day11 d11 = null;
		try {
			d11 = new Day11();
			d11.readFile();
			d11.evalMonkeys();
			d11.finalResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
