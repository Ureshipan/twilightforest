package twilightforest.entity;

import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ai.goal.AttemptToGoHomeGoal;

public interface EnforcedHomePoint {

	default <T extends PathfinderMob & EnforcedHomePoint> void addRestrictionGoals(T entity, GoalSelector selector) {
		selector.addGoal(5, new AttemptToGoHomeGoal<>(entity, 1.25D));
	}

	default void saveHomePointToNbt(ValueOutput tag) {
		if (this.getRestrictionPoint() != null) {
			tag.store("HomePos", GlobalPos.CODEC, this.getRestrictionPoint());
		}
	}

	default void loadHomePointFromNbt(ValueInput tag) {
		// Port note: legacy "Home" ListTag format dropped; only the codec-based "HomePos" is read.
		tag.read("HomePos", GlobalPos.CODEC).ifPresent(this::setRestrictionPoint);
	}

	default boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(entity.level().dimension())) return true;
		return this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());
	}

	default boolean isRestrictionPointValid(ResourceKey<Level> currentMobLevel) {
		return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(currentMobLevel);
	}

	@Nullable
	GlobalPos getRestrictionPoint();

	void setRestrictionPoint(@Nullable GlobalPos pos);

	int getHomeRadius();
}
