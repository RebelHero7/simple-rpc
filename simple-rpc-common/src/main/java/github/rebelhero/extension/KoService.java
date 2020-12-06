package github.rebelhero.extension;

import java.lang.annotation.*;

/**
 * @author rebelhero
 * @date 2020/12/4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface KoService {

    String name();

}
