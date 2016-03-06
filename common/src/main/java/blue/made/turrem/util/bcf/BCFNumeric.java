package blue.made.turrem.util.bcf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public abstract class BCFNumeric<D extends Number> extends BCFItem<D> {
	public BCFNumeric(byte type) {
		super(type);
	}

	public abstract void setN(double n);

	public abstract double getN();

	public static BCFNumeric storeBest(double n) {
		if ((long) n == n) {
			if (n >= Byte.MIN_VALUE && n <= Byte.MAX_VALUE) {
				return BCF.store((byte) n);
			} else if (n >= Integer.MIN_VALUE && n <= Integer.MAX_VALUE) {
				return BCF.store((short) n);
			} else if (n >= Long.MIN_VALUE && n <= Long.MAX_VALUE) {
				return BCF.store((int) n);
			} else {
				return BCF.store((long) n);
			}
		}
		return BCF.store((double) n);
	}

	@Override
	public String toString() {
		return "" + this.getN();
	}
}
