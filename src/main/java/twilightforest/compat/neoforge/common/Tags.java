package twilightforest.compat.neoforge.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Tags {

	public static class Items {
		public static final TagKey<Item> DUSTS_REDSTONE = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dusts/redstone"));
		public static final TagKey<Item> ORES_COAL = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ores/coal"));
		public static final TagKey<Item> GEMS_DIAMOND = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "gems/diamond"));
		public static final TagKey<Item> GEMS_EMERALD = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "gems/emerald"));
		public static final TagKey<Item> INGOTS_IRON = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/iron"));
		public static final TagKey<Item> INGOTS_GOLD = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "ingots/gold"));
	}
}
