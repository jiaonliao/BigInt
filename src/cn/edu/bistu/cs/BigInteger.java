/**
 *
 */
package cn.edu.bistu.cs;


import java.util.ArrayList;

/**
 * @author Wren
 *
 */
public class BigInteger implements BigInt {
    private int value;
    private BigInteger next;
    private Sign sign;

    public BigInteger(String integer) {
        if (integer == null || "".equals(integer.trim())) {
            throw new IllegalArgumentException("Integer can't be empty");
        }
        boolean sign = integer.startsWith("-");
        this.sign = sign ? Sign.NEGATIVE : Sign.POSITIVE;
        integer = integer
                .replaceAll("-", "")
                .replaceAll("//+", "")
                .trim();
        char[] nums = integer.toCharArray();
        if (nums.length == 0) {
            this.value = 0;
            this.next = null;
        } else if (nums.length == 1) {
            this.value = Integer.parseInt(String.valueOf(nums[0]));
            this.next = null;
        } else {
            BigInteger pointer = null;
            for (int i = 0; i < nums.length; i++) {
                BigInteger bigInteger = new BigInteger();
                bigInteger.setValue(Integer.parseInt(String.valueOf(nums[nums.length - i - 1])));
                bigInteger.next = i == 0 ? null : pointer;
                pointer = bigInteger;
            }
            this.value = pointer.value;
            this.next = pointer.next;
        }
    }

    private BigInteger() {
    }

    public BigInteger(Long integer) {
        this(integer.toString());
    }

    public BigInteger(Integer integer) {
        this(integer.toString());
    }

    @Override
    public BigInt add(BigInt bInt) {
        if (!(bInt instanceof BigInteger)) {
            throw new IllegalArgumentException("bInt must be BigInteger");
        }
        BigInteger b2 = (BigInteger) bInt;
        if (Sign.POSITIVE.equals(this.sign) && Sign.POSITIVE.equals(b2.sign)) {
            BigInteger integer = add(this, b2);
            integer.setSign(Sign.POSITIVE);
            return integer;
        } else if (Sign.NEGATIVE.equals(this.sign) && Sign.NEGATIVE.equals(b2.sign)) {
            BigInteger integer = add(this, b2);
            integer.setSign(Sign.NEGATIVE);
            return integer;
        } else {
            boolean compareTo = compareTo(b2);
            BigInteger res;
            if (compareTo) {
                res = sub(this, b2);
                if (this.getSign().equals(Sign.POSITIVE)) {
                    res.setSign(Sign.POSITIVE);
                } else {
                    res.setSign(Sign.NEGATIVE);
                }
            } else {
                res = sub(b2, this);
                if (b2.getSign().equals(Sign.POSITIVE)) {
                    res.setSign(Sign.POSITIVE);
                } else {
                    res.setSign(Sign.NEGATIVE);
                }
            }
            return res;
        }
    }

    @Override
    public BigInt sub(BigInt bInt) {
        if (!(bInt instanceof BigInteger)) {
            throw new IllegalArgumentException("bInt must be BigInteger");
        }
        BigInteger b2 = (BigInteger) bInt;
        boolean compareTo = compareTo(b2);
        if (Sign.POSITIVE.equals(this.sign) && Sign.POSITIVE.equals(b2.sign)) {
            if (compareTo) {
                return sub(this, b2);
            } else {
                BigInteger sub = sub(b2, this);
                sub.setSign(Sign.NEGATIVE);
                return sub;
            }
        } else if (Sign.NEGATIVE.equals(this.sign) && Sign.NEGATIVE.equals(b2.sign)) {
            BigInteger integer = add(this, b2);
            integer.setSign(Sign.NEGATIVE);
            return integer;
        } else {
            if (this.getSign().equals(Sign.POSITIVE) && b2.getSign().equals(Sign.NEGATIVE)) {
                BigInteger sub = add(this, b2);
                sub.setSign(Sign.POSITIVE);
                return sub;
            } else {
                BigInteger integer = add(this, b2);
                integer.setSign(Sign.NEGATIVE);
                return integer;
            }

        }
    }

    @Override
    public BigInt add(long bLong) {
        BigInteger integer = new BigInteger(bLong);
        return add(this, integer);
    }

    @Override
    public BigInt sub(long bLong) {
        BigInteger integer = new BigInteger(bLong);
        return sub(this, integer);
    }

    @Override
    public BigInt add(int bInt) {
        BigInteger integer = new BigInteger(bInt);
        return add(this, integer);
    }

    @Override
    public BigInt sub(int bInt) {
        BigInteger integer = new BigInteger(bInt);
        return sub(this, integer);
    }

    @Override
    public boolean isPositive() {
        return this.sign.equals(Sign.POSITIVE);
    }

    @Override
    public Sign getSign() {
        return this.sign;
    }

    @Override
    public void setSign(Sign sign) {
        this.sign = sign;

    }



    public void setValue(int value) {
        this.value = value;
    }

    public BigInteger getNext() {
        return next;
    }

    public void setNext(BigInteger next) {
        this.next = next;
    }


    private static BigInteger add(BigInteger b1, BigInteger b2) {
        ArrayList<Integer> num = getIntegers(b1);
        ArrayList<Integer> addNum = getIntegers(b2);
        BigInteger result = null;
        int index1 = num.size() - 1;
        int index2 = addNum.size() - 1;
        boolean carry = false;
        while (index1 >= 0 || index2 >= 0) {
            int num1 = index1 >= 0 ? num.get(index1--) : 0;
            int num2 = index2 >= 0 ? addNum.get(index2--) : 0;
            int sum = (num1 + num2) + (carry ? 1 : 0);
            BigInteger integer = new BigInteger();
            integer.setValue(sum % 10);
            integer.setNext(result);
            result = integer;
            carry = sum >= 10;
        }
        if (carry) {
            BigInteger res = new BigInteger();
            res.setValue(1);
            res.setNext(result);
            result = res;
        }
        return result;
    }

    private static BigInteger sub(BigInteger b1, BigInteger b2) {
        ArrayList<Integer> num = getIntegers(b1);
        ArrayList<Integer> addNum = getIntegers(b2);
        BigInteger result = null;
        int index1 = num.size() - 1;
        int index2 = addNum.size() - 1;
        boolean borrow = false;
        while (index1 >= 0 || index2 >= 0) {
            int num1 = index1 >= 0 ? num.get(index1--) : 0;
            int num2 = index2 >= 0 ? addNum.get(index2--) : 0;
            int sub = num1 - num2 - (borrow ? 1 : 0);
            if (sub < 0) {
                borrow = true;
                sub = 10 + sub;
            }
            BigInteger integer = new BigInteger();
            integer.setValue(sub);
            integer.setNext(result);
            result = integer;
        }
        return result;
    }

    private boolean compareTo(BigInteger bigInteger) {
        ArrayList<Integer> integers = getIntegers(this);
        ArrayList<Integer> integers1 = getIntegers(bigInteger);
        if (integers.size() > integers1.size()) {
            return true;
        }
        if (integers.size() < integers1.size()) {
            return false;
        }
        for (int i = 0; i < integers.size(); i++) {
            if (integers.get(i) < integers1.get(i)) {
                return false;
            }
            if (integers.get(i) > integers1.get(i)) {
                return true;
            }
        }
        return true;
    }


    private static ArrayList<Integer> getIntegers(BigInteger b1) {
        ArrayList<Integer> num = new ArrayList<>();
        BigInteger pointer1 = b1;
        while (pointer1 != null) {
            num.add(pointer1.value);
            pointer1 = pointer1.next;
        }
        return num;
    }

    @Override
    public String toString() {
        ArrayList<Integer> integers = getIntegers(this);
        int split = 3;
        StringBuilder res = new StringBuilder();
        boolean deleted = true;
        for (Integer integer : integers) {
            if (integer != 0 && deleted) {
                deleted = false;
            }
            if (!deleted) {
                split--;
                res.append(integer);
            }
            if (split == 0) {
                res.append(",");
                split = 3;
            }
        }
        if (res.length() == 0) {
            res.append(0);
        }
        return (Sign.NEGATIVE.equals(this.getSign()) ? "-" : "") + res;
    }
}
