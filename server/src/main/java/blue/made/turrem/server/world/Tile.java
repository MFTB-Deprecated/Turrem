package blue.made.turrem.server.world;

import blue.made.turrem.server.world.fogofwar.WorldView;
import blue.made.turrem.server.world.tags.TagRegistry;
import blue.made.turrem.server.world.tags.TileTag;
import io.netty.buffer.ByteBuf;

import java.util.*;

/**
 * Created by doctorocclusion on 3/7/16.
 */
public class Tile {
	public float height;
	public short[] tags = new short[0];

	public boolean addTag(TileTag tag) {
		short id = tag.getId();
		if (id == -1) {
			throw new IllegalArgumentException("Tag: " + tag + " has not been registered");
		}
		short[] newtags = new short[tags.length + 1];
		int j = 0;
		boolean added = false;
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == id)
				return false;
			if (!added && tags[i] > id) { // if we reach a tag greater, insert before (to keep sorted)
				newtags[j++] = id;
				added = true;
			}
			newtags[j] = tags[i]; // copy over old tag
			j++;
		}
		if (!added) {
			newtags[j] = id; // append to end, is greatest tag so far
		}
		tags = newtags;
		return true;
	}

	public Set<TileTag> getTags() {
		return new AbstractSet<TileTag>() {
			@Override
			public Iterator<TileTag> iterator() {
				return new Iterator<TileTag>() {
					private int pos = 0;

					@Override
					public boolean hasNext() {
						return pos < tags.length;
					}

					@Override
					public TileTag next() {
						return TagRegistry.fromId(tags[pos++]);
					}

					@Override
					public void remove() {
						if (pos == 0 || pos > size())
							throw new IndexOutOfBoundsException("Can not remove non-existent tag");
						short[] newtags = new short[tags.length - 1];
						int j = 0;
						for (int i = 0; i < tags.length; i++) {
							if (i != pos - 1) { // if not tag we want to remove, copy over
								newtags[j] = tags[i];
								j++;
							}
						}
						tags = newtags;
					}
				};
			}

			@Override
			public boolean add(TileTag tag) {
				return addTag(tag);
			}

			@Override
			public int size() {
				return tags.length;
			}

			@Override
			public boolean contains(Object o) {
				if (o instanceof TileTag) {
					short id = ((TileTag) o).getId();
					for (int i = 0; i < tags.length; i++) {
						if (tags[i] == id)
							return true;
						if (tags[i] > id) // tags is sorted, so if we have reached a tag > id,
							return false; // there is no change of finding one == later on
					}

				}
				return false;
			}
		};
	}

	public static void write(ByteBuf to, Tile from) {
		to.writeFloat(from.height);
		to.writeByte(from.tags.length);
		for (int i = 0; i < from.tags.length; i++)
			to.writeShort(from.tags[i]);
	}

	//TODO Make sure that TagRegistry is also stored in the save file to protect against updates and mods
	public static Tile read(ByteBuf from) {
		Tile t = new Tile();
		t.height = from.readFloat();
		t.tags = new short[from.readByte() & 0xFF];
		for (int i = 0; i < t.tags.length; i++) {
			t.tags[i] = from.readShort();
		}
		return t;
	}
}
