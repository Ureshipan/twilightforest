package net.neoforged.fml.common;

import net.neoforged.api.distmarker.Dist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stub of NeoForge's @EventBusSubscriber for the Fabric port. Retained so annotated handler
 * classes compile; auto-registration is not active — handlers are wired via Fabric callbacks.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {
	Dist[] value() default {};

	String modid() default "";

	Bus bus() default Bus.GAME;

	enum Bus { GAME, MOD }
}
