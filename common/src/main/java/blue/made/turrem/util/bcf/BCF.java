package blue.made.turrem.util.bcf;

import com.google.gson.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Binary Container Format
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCF {
	private static final Gson gson = new Gson();

	/**
	 * Writes the given item to a ByteBuf
	 *
	 * @param from A BCFItem containing the data
	 * @return The serialization of the data
	 * @see BCFItem#write()
	 */
	public static ByteBuf write(BCFItem from) {
		return from.write();
	}

	/**
	 * Deserialize the given binary data into an BCF item.
	 *
	 * @param from The binary data to read from
	 * @return The BCF data read from the ByteBuf
	 * @throws IOException If an unknown data type is encountered or the input data ran out of readable bytes
	 */
	public static BCFItem read(ByteBuf from) throws IOException {
		try {
			int type = from.readByte() & 0xFF;
			BCFItem item = initType(type);
			if (item != null) {
				item.read(from);
				return item;
			}
			throw new IOException("Unknown BCF type: " + type);
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("Ran out of bytes to read", e);
		}
	}

	/**
	 * Create an empty instance of the type
	 */
	private static BCFItem initType(int type) {
		switch (type) {
			case 0:
				return BCFNull.INSTANCE;
			case 1:
				return new BCFByte();
			case 2:
				return new BCFShort();
			case 3:
				return new BCFInt();
			case 4:
				return new BCFLong();
			case 5:
				return new BCFFloat();
			case 6:
				return new BCFDouble();
			case 7:
				return new BCFRaw();
			case 8:
				return new BCFString();
			case 9:
				return new BCFList();
			case 10:
				return new BCFMap();
			default:
				return null;
		}
	}

	/**
	 * Convert the given BCF data into the nearest Json equivalent. Raw data will be converted to a number array
	 * of the stored bytes, all other types have a close approximation.
	 *
	 * @param bcf The BCF data to convert
	 * @return A Json element containing the data
	 * @see BCFItem#toJson()
	 */
	public static JsonElement toJson(BCFItem bcf) {
		if (bcf instanceof BCFNumeric)
			return new JsonPrimitive((Number) bcf.getData());
		if (bcf instanceof BCFString)
			return new JsonPrimitive((String) bcf.getData());
		if (bcf instanceof BCFRaw)
			return gson.toJsonTree(((BCFRaw) bcf).getData().array());
		if (bcf instanceof BCFList) {
			JsonArray array = new JsonArray();
			for (BCFItem i : ((BCFList) bcf).list) {
				array.add(toJson(i));
			}
			return array;
		}
		if (bcf instanceof BCFMap) {
			JsonObject obj = new JsonObject();
			for (Map.Entry<String, BCFItem> e : ((BCFMap) bcf).map.entrySet()) {
				obj.add(e.getKey(), toJson(e.getValue()));
			}
			return obj;
		}
		return JsonNull.INSTANCE;
	}

	/**
	 * Converts a Json element to the nearest BCF equivalent. Booleans will be converted to bytes,
	 * all other types have a close approximation.
	 *
	 * @param json
	 * @return
	 */
	public static BCFItem fromJson(JsonElement json) {
		if (json.isJsonPrimitive()) {
			JsonPrimitive p = json.getAsJsonPrimitive();
			if (p.isNumber())
				return store(p.getAsNumber());
			if (p.isBoolean())
				return store(p.getAsBoolean());
			if (p.isString())
				return store(p.getAsString());
		}
		if (json.isJsonArray()) {
			BCFList list = new BCFList();
			JsonArray array = json.getAsJsonArray();
			array.forEach(e -> list.list.add(fromJson(e)));
			return list;
		}
		if (json.isJsonObject()) {
			BCFMap map = new BCFMap();
			JsonObject obj = json.getAsJsonObject();
			obj.entrySet().forEach(e -> map.map.put(e.getKey(), fromJson(e.getValue())));
			return map;
		}
		return BCFNull.INSTANCE;
	}

	public static BCFByte store(boolean b) {
		return new BCFByte(b ? (byte) 1 : (byte) 0);
	}

	public static BCFByte store(byte b) {
		return new BCFByte(b);
	}

	public static BCFShort store(short s) {
		return new BCFShort(s);
	}

	public static BCFInt store(int i) {
		return new BCFInt(i);
	}

	public static BCFLong store(long l) {
		return new BCFLong(l);
	}

	public static BCFFloat store(float f) {
		return new BCFFloat(f);
	}

	public static BCFDouble store(double d) {
		return new BCFDouble(d);
	}

	public static BCFNumeric store(Number n) {
		if (n instanceof Byte)
			return store(n.byteValue());
		else if (n instanceof Short)
			return store(n.shortValue());
		else if (n instanceof Integer)
			return store(n.intValue());
		else if (n instanceof Long)
			return store(n.longValue());
		else if (n instanceof Float)
			return store(n.floatValue());
		else if (n instanceof Double)
			return store(n.doubleValue());
		else
			return BCFNumeric.storeBest(n.doubleValue());
	}

	public static BCFRaw store(byte... bytes) {
		return new BCFRaw(bytes);
	}

	public static BCFRaw store(short... shorts) {
		return new BCFRaw(Unpooled.copyShort(shorts));
	}

	public static BCFRaw store(int... ints) {
		return new BCFRaw(Unpooled.copyInt(ints));
	}

	public static BCFRaw store(long... longs) {
		return new BCFRaw(Unpooled.copyLong(longs));
	}

	public static BCFRaw store(float... floats) {
		return new BCFRaw(Unpooled.copyFloat(floats));
	}

	public static BCFRaw store(double... doubles) {
		return new BCFRaw(Unpooled.copyDouble(doubles));
	}

	public static BCFRaw store(ByteBuf buf) {
		return new BCFRaw(buf);
	}

	public static BCFRaw store(ByteBuf buf, int from, int to) {
		return new BCFRaw(buf, from , to);
	}

	public static BCFRaw store(ByteBuffer buf) {
		return new BCFRaw(Unpooled.wrappedBuffer(buf));
	}

	public static BCFString store(String s) {
		return new BCFString(s);
	}

	public static BCFList store(List<BCFItem> l) {
		return new BCFList(l);
	}

	public static BCFList store(BCFItem... list) {
		return store(Arrays.asList(list));
	}

	public static BCFMap store(Map<String, BCFItem> m) {
		return new BCFMap(m);
	}
}
