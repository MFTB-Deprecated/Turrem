package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;


/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFFloat extends BCFNumeric<Float> {
	public float data;

	BCFFloat() {
		super((byte) 5);
	}

	public BCFFloat(float data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readFloat();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeFloat(this.data);
	}

	@Override
	public Float getData() {
		return this.data;
	}

	@Override
	public void setData(Float to) {
		this.data = to;
	}

	@Override
	public void setN(double n) {
		this.data = (float) n;
	}

	@Override
	public double getN() {
		return this.data;
	}
}
