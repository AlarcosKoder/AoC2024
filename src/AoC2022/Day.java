package AoC2022;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day {
	
	protected static int LOG_LEVEL = 1;
	protected static boolean INPUT_REAL = true;
	
	private static final BigDecimal SQRT_DIG = new BigDecimal(150);
	private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());
	
	class Point implements Comparable<Point>{
		
		int x;
		int y;
		
		public Point(int x, int y) {
			
			this.x=x;//=new BigInteger(df.format(Math.ceil(x)));
			this.y=y;//new BigInteger(df.format(Math.ceil(y)));
		}
		
		private BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
		    BigDecimal fx = xn.pow(2).add(c.negate());
		    BigDecimal fpx = xn.multiply(new BigDecimal(2));
		    BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
		    xn1 = xn.add(xn1.negate());
		    BigDecimal currentSquare = xn1.pow(2);
		    BigDecimal currentPrecision = currentSquare.subtract(c);
		    currentPrecision = currentPrecision.abs();
		    if (currentPrecision.compareTo(precision) <= -1){
		        return xn1;
		    }
		    return sqrtNewtonRaphson(c, xn1, precision);
		}
		
		public BigDecimal sqrt(BigDecimal A, final int SCALE) {
			try {
				BigDecimal x0 = new BigDecimal("0");
			    BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
			    while (!x0.equals(x1)) {
			        x0 = x1;
			        x1 = A.divide(x0, SCALE, BigDecimal.ROUND_HALF_UP);
			        x1 = x1.add(x0);
			        x1 = x1.divide(BigDecimal.ONE.add(BigDecimal.ONE), SCALE, BigDecimal.ROUND_HALF_UP);

			    }
			    return x1;
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return BigDecimal.ZERO;
		}
		
		public int mDistance(Point p) {
			int xB = Math.abs(x-p.x);
			int yB = Math.abs(y-p.y);
			return xB+yB;
//			return xB.abs().add(yB.abs());
		}
		
		public double distance(Point p) {
			return Math.sqrt((p.x-x)*(p.x-x)+(p.y-y)*(p.y-y));
		}
		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
		@Override
		public boolean equals(Object obj) {
			if(obj!=null && obj instanceof Point) {
				Point p = (Point)obj;
				return p.x==x&&p.y==y;
			} else if(obj!=null && obj instanceof Integer) {
				Integer i = (Integer)obj;
				if(i!=null)return x==i.intValue();
			}
			return false;
		}
		@Override
		public int compareTo(Point o) {
			if(x==o.x)return 0;
			return x<o.x?-1:1;
		}
		public int compareTo(Integer i) {
			return i.compareTo(Integer.valueOf(x));
		}
	}
	
	protected File inputFile;
	protected BufferedReader fileReader;
	protected BufferedWriter bw;
	protected StringBuffer sb;
	
	//5183 too low
	
	public Day() {
		try {
			String daySeq = super.getClass().getName().replace("AoC2024.Day", "").replace("coa.", "");
			File file = new File(".\\input_2024\\day"+daySeq+".txt");
			
			File outputFile = new File(".\\logs\\day"+daySeq+"_out.txt");
			if (outputFile.exists()) {
				outputFile.delete();
			}

			outputFile.createNewFile();
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readFile() {
		Pattern p = Pattern.compile(".*Day(\\d+).*");
		Matcher m = p.matcher(super.getClass().getName());
		String daySeq = "00";
		if(m.find())
			daySeq = m.group(1);//super.getClass().getName().replaceAll("AoC2024.Day", "").replace("coa.", "").replace("_alt", "").replace("_old", "");
		if(INPUT_REAL)
			inputFile = new File(".\\input_2024\\day"+daySeq+"_data.txt");
			//inputFile = new File(".\\input_"+daySeq+".txt");
		else
			inputFile = new File(".\\input_2024\\day"+daySeq+"_sample.txt");
			//inputFile = new File(".input_"+daySeq+"_ctrl.txt");
		
		
		String strCurrentLine;
		
		try {
			fileReader = new BufferedReader(new FileReader(inputFile));
			
			sb = new StringBuffer();
			
			while ((strCurrentLine = fileReader.readLine()) != null) {
				sb.append(strCurrentLine).append("\n");
				//log(strCurrentLine+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void log(String log, int level) {
		try {
			if(level>0)System.out.print(log);
			bw.append(log);
//			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void flush() {
		try {
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void log(String log) {
		log(log, LOG_LEVEL);
	}
	
	public void logln(String log) {
		log(log, LOG_LEVEL);
		log("\n", LOG_LEVEL);
	}
	
	public int getLOGLEVEL() {
		return 0;
	}
	
	public boolean getINPUTCTRL() {
		return false;
	}

}
