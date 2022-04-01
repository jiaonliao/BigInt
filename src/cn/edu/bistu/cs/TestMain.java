/**
 * 
 */
package cn.edu.bistu.cs;

/**
 * @author Wren
 *
 */
public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger bigInteger = new BigInteger("576576575555555555555555");
		BigInt anInt = bigInteger.add(new BigInteger("100002"));
		System.out.println(anInt);
		BigInt bInt = new BigInteger(+999999999999999999L);
		BigInt result = bInt.sub(new BigInteger("10000000000000000000"));
		System.out.println(result);
	}
}
