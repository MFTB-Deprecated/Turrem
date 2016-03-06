package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFDouble extends BCFNumeric<Double> {
	public double data;

	BCFDouble() {
		super((byte) 6);
	}

	public BCFDouble(double data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readDouble();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeDouble(this.data);
	}

	@Override
	public Double getData() {
		return this.data;
	}

	@Override
	public void setData(Double to) {
		this.data = to;
	}

	@Override
	public void setN(double n) {
		this.data = n;
	}

	@Override
	public double getN() {
		return this.data;
	}
}
