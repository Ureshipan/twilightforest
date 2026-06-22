package twilightforest.init;

import com.mojang.serialization.MapCodec;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Unit;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.components.item.OreScannerComponent;
import twilightforest.util.Codecs;

import java.util.function.Consumer;

/**
 * Ported from NeoForge data attachments to the Fabric Data Attachment API
 * ({@code fabric-data-attachment-api-v1}). Attachments register eagerly here; call
 * {@link #init()} once during mod initialization to force class-loading.
 *
 * <p>NeoForge call-site mapping: {@code holder.getData(TYPE)} → {@code holder.getAttachedOrCreate(TYPE)},
 * {@code setData}→{@code setAttached}, {@code hasData}→{@code hasAttached}, {@code removeData}→{@code removeAttached}.
 */
public class TFDataAttachments {

	private static <T> AttachmentType<T> register(String name, Consumer<AttachmentRegistry.Builder<T>> cfg) {
		return AttachmentRegistry.create(TwilightForestMod.prefix(name), cfg);
	}

	public static final AttachmentType<Boolean> FEATHER_FAN = register("feather_fan_falling", b -> b.initializer(() -> false).persistent(Codec.BOOL));
	public static final AttachmentType<PotionFlaskTrackingAttachment> FLASK_DOSES = register("flask_doses", b -> b.initializer(PotionFlaskTrackingAttachment::new).persistent(PotionFlaskTrackingAttachment.CODEC));
	public static final AttachmentType<FortificationShieldAttachment> FORTIFICATION_SHIELDS = register("fortification_shields", b -> b.initializer(FortificationShieldAttachment::new).persistent(FortificationShieldAttachment.CODEC));
	public static final AttachmentType<GiantPickaxeMiningAttachment> GIANT_PICKAXE_MINING = register("giant_pickaxe_mining", b -> b.initializer(GiantPickaxeMiningAttachment::new));
	public static final AttachmentType<OreScannerComponent> ORE_SCANNER = register("ore_scanner", b -> b.initializer(OreScannerComponent::getEmpty).persistent(OreScannerComponent.CODEC));
	public static final AttachmentType<YetiThrowAttachment> YETI_THROWING = register("yeti_throwing", b -> b.initializer(YetiThrowAttachment::new));
	public static final AttachmentType<MultiplayerInclusivityAttachment> MULTIPLAYER_FIGHT = register("multiplayer_fight", b -> b.initializer(MultiplayerInclusivityAttachment::new));
	public static final AttachmentType<TFPortalAttachment> TF_PORTAL_COOLDOWN = register("tf_portal_cooldown", b -> b.initializer(TFPortalAttachment::new));
	public static final AttachmentType<SmashBlocksEnchantmentAttachment> SMASH_BLOCKS = register("smash_blocks", b -> b.initializer(SmashBlocksEnchantmentAttachment::new).persistent(SmashBlocksEnchantmentAttachment.CODEC));
	public static final AttachmentType<GameProfile> ZOMBIFIED_PLAYER = register("zombified_player", b -> b.initializer(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).persistent(Codecs.SIMPLE_GAME_PROFILE));
	public static final AttachmentType<Unit> LEASH_PATHFINDER_OVERRIDE = register("leashed_pathfinder_override", b -> b.initializer(() -> Unit.INSTANCE).persistent(MapCodec.unitCodec(() -> Unit.INSTANCE)));
	public static final AttachmentType<Unit> BANISHED_TO_TWILIGHT_FOREST = register("twilightforest_banished", b -> b.initializer(() -> Unit.INSTANCE).persistent(MapCodec.unitCodec(() -> Unit.INSTANCE)).copyOnDeath());

	/** Forces class-loading so the eager registrations above run. Call once from mod init. */
	public static void init() {}
}
