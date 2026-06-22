package net.neoforged.neoforge.client.extensions.common;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
public interface IClientBlockExtensions {
    default boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) { return false; }
    default boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager) { return false; }
}