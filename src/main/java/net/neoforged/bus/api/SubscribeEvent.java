package net.neoforged.bus.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stub of NeoForge's @SubscribeEvent for the Fabric port. Retained so annotated handler
 * methods compile; the NeoForge event bus is not active — events are wired via Fabric callbacks.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent {
	EventPriority priority() default EventPriority.NORMAL;

	boolean receiveCanceled() default false;
}
