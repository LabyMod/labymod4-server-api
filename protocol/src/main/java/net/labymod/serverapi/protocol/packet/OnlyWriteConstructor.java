package net.labymod.serverapi.protocol.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate this on a no-args constructor of a packet that should not be used for reading. If the
 * no-args constructor is annotated with this (or it doesn't exist at all), the instance of the
 * packet will be created via {@link sun.misc.Unsafe#allocateInstance(Class)}
 */
@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyWriteConstructor {

}
