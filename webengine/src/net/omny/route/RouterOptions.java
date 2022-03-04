package net.omny.route;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.omny.utils.HTTPUtils;

@Retention(RUNTIME)
@Target({
        ElementType.TYPE
})
public @interface RouterOptions {

    public String namespace() default HTTPUtils.DEFAULT_NAMESPACE;

}
