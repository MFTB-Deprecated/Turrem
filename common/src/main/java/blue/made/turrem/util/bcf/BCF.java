package blue.made.turrem.util.bcf;

import com.google.gson.*;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Binary Container Format
 * Created by doctorocclusion on 3/5/2016.
 */
public class BCF {
	private static final Gson gson = new Gson();

	public static ByteBuf write(BCFItem from) {
		return from.write();
	}

	public static BCFItem read(ByteBuf from) throws IOException {
		int type = from.readByte() & 0xFF;
		BCFItem item = initType(type);
		if (item != null) {
			try {
				item.read(from);
			} catch (IndexOutOfBoundsException e) {
				throw new IOException("Ran out of bytes to read", e);
			}
			return item;
		}
		throw new IOException("Unknown BCF type: " + type);
	}

	private static BCFItem initType(int type) {
		switch(type) {
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

	public static BCFRaw store(byte[] bytes) {
		return new BCFRaw(bytes);
	}

	public static BCFRaw store(ByteBuf buf) {
		return new BCFRaw(buf);
	}

	public static BCFString store(String s) {
		return new BCFString(s);
	}

	public static BCFList store(List<BCFItem> l) {
		return new BCFList(l);
	}

	public static BCFList store(BCFItem...list) {
		return store(Arrays.asList(list));
	}

	public static BCFMap store(Map<String, BCFItem> m) {
		return new BCFMap(m);
	}
}
