package twilightforest;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.FoliageColorHandler;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.util.ArmorUtil;
import twilightforest.util.WorldUtil;
import twilightforest.util.multiparts.MultipartEntityUtil;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;
import twilightforest.config.TFConfig;
import twilightforest.block.CloudBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Iterator;

@SuppressWarnings({"unused", "deprecation"})
public class ASMHooks {

	// ── Injected dependencies ────────────────────────────────────────────────
	// Populated during mod initialisation (replaces beanification @Autowired).

	private static ArmorUtil armorUtil;
	private static MultipartEntityUtil multipartEntityUtil;
	private static FoliageColorHandler foliageColorHandler;

	public static void init(ArmorUtil armorUtilIn, MultipartEntityUtil multipartEntityUtilIn,
			FoliageColorHandler foliageColorHandlerIn) {
		armorUtil = armorUtilIn;
		multipartEntityUtil = multipartEntityUtilIn;
		foliageColorHandler = foliageColorHandlerIn;
	}

	// ── armor ────────────────────────────────────────────────────────────────

	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}

	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			return false;
		}
		return o;
	}

	// ── beardifier ───────────────────────────────────────────────────────────

	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(
			StructureManager structureManager, net.minecraft.world.level.ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);
		for (StructureStart structureStart : structureManager.startsForStructure(
				chunkPos, s -> s instanceof CustomDensitySource)) {
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(
					customDensitySource.getStructureTerraformer(chunkPos, structureStart));
		}
		return customStructureTerraforms.iterator();
	}

	public static double getCustomDensity(double o, DensityFunction.FunctionContext context,
			@Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null) return o;
		double newDensity = 0;
		while (customDensities.hasNext()) newDensity += customDensities.next().compute(context);
		customDensities.back(Integer.MAX_VALUE);
		return o + newDensity;
	}

	// ── chunk ────────────────────────────────────────────────────────────────

	public static void chunkBlanketing(ChunkAccess chunkAccess, WorldGenRegion worldGenRegion) {
		ChunkBlanketProcessors.chunkBlanketing(chunkAccess, worldGenRegion);
	}

	/** Called from ChunkStatusTasksMixin when WorldGenContext is available but not WorldGenRegion. */
	public static void chunkBlanketingFromContext(
			net.minecraft.world.level.chunk.status.WorldGenContext ctx, ChunkAccess chunk) {
		// TODO: adapt ChunkBlanketProcessors to accept WorldGenContext when WorldGenRegion unavailable
	}

	// ── cloud ────────────────────────────────────────────────────────────────

	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			LevelChunk chunk = level.getChunkAt(pos);
			for (int y = pos.getY(); y < pos.getY() + TFConfig.commonCloudBlockPrecipitationDistance; y++) {
				BlockPos newPos = pos.atY(y);
				BlockState state = chunk.getBlockState(newPos);
				if (state.getBlock() instanceof CloudBlock cloudBlock
					&& cloudBlock.getCurrentPrecipitation(newPos, level, level.getRainLevel(1.0F))
						.getLeft() == Biome.Precipitation.RAIN) {
					return true;
				}
				if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(state)) {
					return false;
				}
			}
		}
		return isRaining;
	}

	// ── conquered ────────────────────────────────────────────────────────────

	public static StructureStart loadStaticStart(StructureStart start,
			PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(),
				start.getReferences(), piecesContainer, nbt);
		return start;
	}

	// ── foliage ──────────────────────────────────────────────────────────────

	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return foliageColorHandler.get(o, biome, x, z);
	}

	// ── lead ────────────────────────────────────────────────────────────────

	public static boolean leashFenceKnotSurvives(boolean o, LeashFenceKnotEntity entity) {
		if (o) return true;
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE)
			&& fenceState.getValue(twilightforest.block.WroughtIronFenceBlock.POST)
				!= twilightforest.block.WroughtIronFenceBlock.PostState.NONE;
	}

	// ── map ─────────────────────────────────────────────────────────────────

	@Nullable
	public static Pair<BlockPos, Holder<Structure>> resolveNearestNonRandomSpreadMapStructure(
			@Nullable Pair<BlockPos, Holder<Structure>> o,
			ServerLevel level, HolderSet<Structure> targetStructures,
			BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		return WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius,
			skipKnownStructures).orElse(o);
	}

	// ── multipart ────────────────────────────────────────────────────────────

	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	@Nullable
	public static EntityRenderer<?, ?> resolveEntityRenderer(@Nullable EntityRenderer<?, ?> renderer,
			Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

	// ── shroom ───────────────────────────────────────────────────────────────

	/** Checks if any block adjacent to {@code pos} at y-1 is a TF Twilight Portal. */
	public static boolean mushroomCanSurviveOnPortal(LevelReader level, BlockPos pos) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0) continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL))
					return true;
			}
		}
		return false;
	}

	// ── mob ─────────────────────────────────────────────────────────────────

	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		// TODO Phase 4: replace hasData with Fabric equivalent once data attachments are migrated
		// return prior && !mob.hasAttached(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
		return prior;
	}
}
