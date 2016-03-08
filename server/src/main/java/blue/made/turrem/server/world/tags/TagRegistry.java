package blue.made.turrem.server.world.tags;

import java.util.ArrayList;

/**
 * Created by sam on 3/8/16.
 */
public class TagRegistry {
	private static short nextId = 0;
	private static ArrayList<TileTag> tags = new ArrayList<>();

	public static void register(TileTag tag) {
		if (tag.id == -1) {
			tag.id = nextId++;
			tags.add(tag);
		}
	}

	public static TileTag fromId(short id) {
		return tags.get(id);
	}
}
