package twilightforest.compat.neoforge.common;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Tags {

	public static class Items {
		public static final TagKey<Item> DUSTS_REDSTONE = ItemTags.create(Identifier.fromNamespaceAndPath("c", "dusts/redstone"));
		public static final TagKey<Item> ORES_COAL = ItemTags.create(Identifier.fromNamespaceAndPath("c", "ores/coal"));
		public static final TagKey<Item> GEMS_DIAMOND = ItemTags.create(Identifier.fromNamespaceAndPath("c", "gems/diamond"));
		public static final TagKey<Item> GEMS_EMERALD = ItemTags.create(Identifier.fromNamespaceAndPath("c", "gems/emerald"));
		public static final TagKey<Item> INGOTS_IRON = ItemTags.create(Identifier.fromNamespaceAndPath("c", "ingots/iron"));
		public static final TagKey<Item> INGOTS_GOLD = ItemTags.create(Identifier.fromNamespaceAndPath("c", "ingots/gold"));
	}
}
