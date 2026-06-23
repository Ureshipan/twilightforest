package net.neoforged.api.distmarker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stub of NeoForge's @OnlyIn for the Fabric port. Side-stripping is handled by Fabric's
 * {@code @Environment} at runtime; this is retained only so annotated members compile.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface OnlyIn {
	Dist value();
}
