package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFByte extends BCFNumeric<Byte> {
	public byte data;

	BCFByte() {
		super((byte) 1);
	}

	public BCFByte(byte data) {
		this();
		this.data = data;
	}

	@Override
	protected void read(ByteBuf from) {
		this.data = from.readByte();
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeByte(this.data);
	}

	@Override
	public Byte getData() {
		return this.data;
	}

	@Override
	public void setData(Byte to) {
		this.data = to;
	}
}
