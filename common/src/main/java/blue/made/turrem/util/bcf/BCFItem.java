package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

/**
 * Created by doctorocclusion on 3/5/2016.
 */

public abstract class BCFItem<D> {
	public final byte type;

	protected BCFItem(byte type) {
		this.type = type;
	}

	protected abstract void read(ByteBuf from) throws IOException;

	protected void write(ByteBuf to) {
		to.writeByte(this.type);
	}

	public abstract D getData();

	public abstract void setData(D to);

	public ByteBuf write() {
		ByteBuf bytes = Unpooled.buffer(64);
		this.write(bytes);
		return bytes;
	}
}
