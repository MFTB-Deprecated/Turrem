package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFShort extends BCFNumeric<Short> {
	public short data;

	BCFShort() {
		super((byte) 2);
	}

	public BCFShort(short data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readShort();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeShort(this.data);
	}

	@Override
	public Short getData() {
		return this.data;
	}

	@Override
	public void setData(Short to) {
		this.data = to;
	}
}
