package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.*;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFList extends BCFItem<List<BCFItem>> {
	public List<BCFItem> list = new ArrayList<>();

	public BCFList() {
		super((byte) 9);
	}

	public BCFList(List<BCFItem> list) {
		this();
		this.list = list;
	}

	@Override
	protected void read(ByteBuf from) throws IOException {
		int len = from.readInt();
		for (int i = 0; i < len; i++) {
			list.add(BCF.read(from));
		}
	}

	@Override
	protected void write(ByteBuf to) {
		super.write(to);
		to.writeInt(list.size());
		for (BCFItem i : list) {
			i.write(to);
		}
	}

	@Override
	public List<BCFItem> getData() {
		return list;
	}

	@Override
	public void setData(List<BCFItem> to) {
		list = to;
	}

	public String toString() {
		String out = "[ ";
		boolean skipdel = true;
		for (BCFItem i : this.list) {
			if (skipdel)
				skipdel = false;
			else
				out += ", ";
			out += i;
		}
		return out + " ]";
	}
}