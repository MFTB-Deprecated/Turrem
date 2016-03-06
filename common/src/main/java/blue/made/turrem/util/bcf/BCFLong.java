package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFLong extends BCFNumeric<Long> {
	public long data;

	BCFLong() {
		super((byte) 4);
	}

	public BCFLong(long data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readLong();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeLong(this.data);
	}

	@Override
	public Long getData() {
		return this.data;
	}

	@Override
	public void setData(Long to) {
		this.data = to;
	}

	@Override
	public void setN(double n) {
		this.data = (long) n;
	}

	@Override
	public double getN() {
		return this.data;
	}

	@Override
	public String toString() {
		return "" + this.data;
	}
}
