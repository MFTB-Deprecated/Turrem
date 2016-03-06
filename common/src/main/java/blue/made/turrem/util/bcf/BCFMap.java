package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFMap extends BCFItem<Map<String, BCFItem>> {
	public static final Charset utf8 = Charset.forName("UTF-8");

	public Map<String, BCFItem> map = new HashMap<>();

	public BCFMap() {
		super((byte) 10);
	}

	public BCFMap(Map<String, BCFItem> map) {
		this();
		this.map = map;
	}

	@Override
	protected void read(ByteBuf from) throws IOException {
		int len = from.readInt();
		for (int i = 0; i < len; i++) {
			int klen = from.readByte() & 0xFF;
			String key = from.readSlice(klen).toString(utf8);
			map.put(key, BCF.read(from));
		}
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeInt(map.size());
		for (Map.Entry<String, BCFItem> e : map.entrySet()) {
			ByteBuf key = Unpooled.copiedBuffer(e.getKey(), utf8);
			int len = key.readableBytes();
			if (len >= 256) {
				throw new IndexOutOfBoundsException("The key: \"" + e.getKey() + "\" is to large, must be <256 chars");
			}
			to.writeByte(len);
			to.writeBytes(key, len);
			e.getValue().write(to);
		}
	}

	@Override
	public Map<String, BCFItem> getData() {
		return map;
	}

	@Override
	public void setData(Map<String, BCFItem> to) {
		map = to;
	}

	public String toString() {
		String out = "{ ";
		boolean skipdel = true;
		for (Map.Entry<String, BCFItem> e : this.map.entrySet()) {
			if (skipdel)
				skipdel = false;
			else
				out += ", ";
			out += e.getKey() + ": " + e.getValue();
		}
		return out + " }";
	}
}