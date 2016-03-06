package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFInt extends BCFNumeric<Integer> {
	public int data;

	BCFInt() {
		super((byte) 3);
	}

	public BCFInt(int data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readInt();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeInt(this.data);
	}

	@Override
	public Integer getData() {
		return this.data;
	}

	@Override
	public void setData(Integer to) {
		this.data = to;
	}

	@Override
	public void setN(double n) {
		this.data = (int) n;
	}

	@Override
	public double getN() {
		return this.data;
	}
}
