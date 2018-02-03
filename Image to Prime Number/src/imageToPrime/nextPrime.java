package imageToPrime;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

public class nextPrime {
	
	public static String integrateNextPrime(String in, BufferedImage image, int area, String integrate) {
		String inTemp = "";
		
		if(integrate == "2") {
			inTemp = in.replaceAll("[\r\n]+", "");
			int startLength = inTemp.length();
			
			for(int i = 0; i < (area - startLength); i++) {
				inTemp = inTemp + "1";
			}
		}
		else if(integrate == "0"
				+ "") {
			inTemp = in.replaceAll("[\r\n]+", "");
		}
		else {
			for(int i = 0; i < image.getWidth(); i++) {
				in = in +"1";
			}
			
			inTemp = in.replaceAll("[\r\n]+", "");
		}
		
		BigInteger inInt = new BigInteger(inTemp);
		
		final long startTime = System.currentTimeMillis();
		BigInteger primeInt = calcNextPrime(inInt);
		
		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime) );
		
		String primeString = primeInt.toString();
		
		for(int i = 0; i < primeString.length(); i += (image.getWidth() + 1)) {
			primeString = primeString.substring(0, i) + "\n" + primeString.substring(i, primeString.length());
		}
		
		primeString = primeString.substring(1);
		
		return primeString;
	}
	
	public static BigInteger calcNextPrime(BigInteger in) {
		if(in.isProbablePrime(Integer.MAX_VALUE)) {
			return in;
		}
		else {
			do {
				in = in.nextProbablePrime();
			} while(in.isProbablePrime(Integer.MAX_VALUE) == false);
			
			return in;
		}
	}

}
