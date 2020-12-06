package github.rebelhero.extension;

import java.lang.annotation.*;

/**
 * @author rebelhero
 * @date 2020/12/3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface KO {

    String value() default "";
}
