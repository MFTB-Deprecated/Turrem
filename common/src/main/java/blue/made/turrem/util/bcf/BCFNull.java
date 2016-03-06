package blue.made.turrem.util.bcf;

import io.netty.buffer.ByteBuf;

/**
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCFNull extends BCFItem<Object> {
	public static final BCFNull INSTANCE = new BCFNull();

	private BCFNull() {
		super((byte) 0);
	}

	@Override
	protected void read(ByteBuf from) {

	}

	@Override
	public Object getData() {
		return null;
	}

	@Override
	public void setData(Object to) {

	}
}
