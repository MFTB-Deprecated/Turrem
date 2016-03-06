package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFRaw extends BCFItem<ByteBuf> {
	private ByteBuf buf;

	BCFRaw() {
		super((byte) 7);
	}

	public BCFRaw(ByteBuf buf) {
		this();
		this.buf = buf.copy();
	}

	public BCFRaw(byte[] bytes) {
		this();
		buf = Unpooled.copiedBuffer(bytes);
	}

	public BCFRaw(int initialCapacity) {
		this();
		buf = Unpooled.buffer(initialCapacity);
	}

	public BCFRaw(int initialCapacity, int maxCapacity) {
		this();
		buf = Unpooled.buffer(initialCapacity, maxCapacity);
	}

	@Override
	protected void read(ByteBuf from) {
		int l = from.readInt();
		this.buf = Unpooled.buffer(l);
		from.readBytes(this.buf);
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		byte[] data = buf.array();
		to.writeInt(data.length);
		to.writeBytes(data);
	}

	@Override
	public ByteBuf getData() {
		return this.buf.duplicate();
	}

	@Override
	public void setData(ByteBuf to) {
		this.buf = to.duplicate();
	}

	public String toString() {
		return DatatypeConverter.printHexBinary(buf.array());
	}
}
