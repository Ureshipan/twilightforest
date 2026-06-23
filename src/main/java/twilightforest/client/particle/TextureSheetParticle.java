package twilightforest.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Port compat shim for MC's removed {@code net.minecraft.client.particle.TextureSheetParticle}.
 * <p>
 * 1.21.6+ reworked particle rendering: the base is now {@link SingleQuadParticle} with an abstract
 * {@code getLayer()} and a render-state {@code extract(...)} path instead of the old
 * {@code render(VertexConsumer, Camera, float)} + {@code getRenderType()} API.
 * <p>
 * This class lives in {@code twilightforest.client.particle} so the mod's existing particle classes
 * (same package) keep compiling unchanged. It bridges the old surface they rely on:
 * {@code getRenderType()}, {@code pickSprite(SpriteSet)}, and a {@code render(...)} hook.
 * <p>
 * TODO(port): wire custom per-particle rendering into the new {@code extract(...)} model — until then
 * these particles compile and tick but use the default quad rendering.
 */
public abstract class TextureSheetParticle extends SingleQuadParticle {

	protected TextureSheetParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z, (TextureAtlasSprite) null);
	}

	protected TextureSheetParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
		super(level, x, y, z, xd, yd, zd, (TextureAtlasSprite) null);
	}

	/** Old API retained for subclasses; defaults to translucent sheet. Drives {@link #getLayer()}. */
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.SINGLE_QUADS;
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	/** Old TextureSheetParticle helper: pick a single sprite from the set. */
	public void pickSprite(SpriteSet sprites) {
		this.setSprite(sprites.get(this.random));
	}

	/**
	 * Legacy render hook. The engine no longer calls this (it uses {@code extract(...)}); kept so
	 * subclasses that {@code @Override} it (and call {@code super.render(...)}) still compile.
	 */
	public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
	}
}
