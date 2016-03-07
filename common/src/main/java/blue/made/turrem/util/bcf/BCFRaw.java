package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * A type used to store a raw blob of bytes. This type should be used to store numerical arrays (like int[]) when
 * possible as well as normal binary data such as images.
 *
 * @see BCF#store(ByteBuf)
 * @see BCF#store(ByteBuf, int, int)
 * @see BCF#store(ByteBuffer)
 * @see BCF#store(byte...)
 * @see BCF#store(short...)
 * @see BCF#store(int...)
 * @see BCF#store(long...)
 * @see BCF#store(float...)
 * @see BCF#store(double...)
 */
public class BCFRaw extends BCFItem<ByteBuf> {
	private ByteBuf buf;

	BCFRaw() {
		super((byte) 7);
	}

	public BCFRaw(ByteBuf buf) {
		this();
		this.buf = buf.duplicate();
	}

	public BCFRaw(ByteBuf buf, int from, int to) {
		this();
		this.buf = buf.slice(from, to);
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
		int len = buf.readableBytes();
		to.writeInt(len);
		to.writeBytes(buf, to.readerIndex(), len);
	}

	@Override
	public ByteBuf getData() {
		return this.buf;
	}

	@Override
	public void setData(ByteBuf to) {
		this.buf = to;
	}

	private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.ensureCapacity(buf.readableBytes() * 2);
		for (int i = buf.readerIndex(); i < buf.writerIndex(); i++){
			int b = buf.getByte(i) & 0xFF;
			build.append(hex[b >> 4]);
			build.append(hex[b & 0xF]);
		}
		return build.toString();
	}

	public static BCFRaw ofAllWrittenBytes(ByteBuf in) {
		return new BCFRaw(in, in.arrayOffset(), in.writerIndex());
	}

	public ByteBuffer asBytes() {
		return buf.nioBuffer();
	}

	public ShortBuffer asShorts() {
		return buf.nioBuffer().asShortBuffer();
	}

	public IntBuffer asInts() {
		return buf.nioBuffer().asIntBuffer();
	}

	public LongBuffer asLongs() {
		return buf.nioBuffer().asLongBuffer();
	}

	public FloatBuffer asFloats() {
		return buf.nioBuffer().asFloatBuffer();
	}

	public DoubleBuffer asDoubles() {
		return buf.nioBuffer().asDoubleBuffer();
	}

	public String asString() {
		return buf.toString(BCFString.utf8);
	}
}
