package twilightforest.compat;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

/**
 * Drop-in replacement for NeoForge / pre-1.21.2 ArmorItem.
 *
 * MC 1.21.11 removed ArmorItem — armors are now plain Items with the Equippable component.
 * This class preserves the old (ArmorMaterial, ArmorType, Properties) constructor surface
 * so existing TF code needs no changes.
 */
public class ArmorItem extends Item {

	private final ArmorMaterial material;
	private final ArmorType type;

	/** Old-API constructor: plain ArmorMaterial (not Holder). */
	public ArmorItem(ArmorMaterial material, ArmorType type, Item.Properties properties) {
		super(withEquippable(Holder.direct(material), type, properties));
		this.material = material;
		this.type = type;
	}

	/** Holder variant for callers that already have a Holder<ArmorMaterial>. */
	public ArmorItem(Holder<ArmorMaterial> materialHolder, ArmorType type, Item.Properties properties) {
		super(withEquippable(materialHolder, type, properties));
		this.material = materialHolder.value();
		this.type = type;
	}

	private static Item.Properties withEquippable(Holder<ArmorMaterial> material, ArmorType type,
			Item.Properties properties) {
		Equippable equippable = Equippable.builder(type.getSlot())
			.setEquipSound(material.value().equipSound())
			.setAsset(material.value().assetId())
			.setDamageOnHurt(true)
			.build();
		return properties.component(DataComponents.EQUIPPABLE, equippable);
	}

	public ArmorMaterial getMaterial() {
		return material;
	}

	public ArmorType getType() {
		return type;
	}
}
