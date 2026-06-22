package net.neoforged.neoforge.client.event;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
public class RegisterParticleProvidersEvent {
    public <T extends net.minecraft.core.particles.ParticleOptions> void registerSpriteSet(ParticleType<T> type, ParticleEngine.SpriteParticleRegistration<T> factory) {}
    public <T extends net.minecraft.core.particles.ParticleOptions> void registerSpecial(ParticleType<T> type, ParticleProvider<T> provider) {}
}