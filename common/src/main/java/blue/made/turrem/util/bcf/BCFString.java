package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFString extends BCFItem<String> {
	public static final Charset utf8 = Charset.forName("UTF-8");

	public String data;

	BCFString() {
		super((byte) 8);
	}

	public BCFString(String s) {
		this();
		data = s;
	}

	@Override
	protected void read(ByteBuf from) {
		int len = from.readInt();
		this.data = from.readBytes(len).toString(utf8);
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		ByteBuf bytes = Unpooled.copiedBuffer(this.data, utf8);
		int len = bytes.readableBytes();
		to.writeInt(len);
		to.writeBytes(bytes, len);
	}

	@Override
	public String getData() {
		return this.data;
	}

	@Override
	public void setData(String to) {
		this.data = to;
	}

	public String toString() {
		return "\"" + this.data + "\"";
	}
}
