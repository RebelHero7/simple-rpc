package github.rebelhero.scan;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author rebelhero
 * @date 2020/12/4
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(KoScannerRegistrar.class)
@Documented
public @interface KoScan {

    String[] basePackages() ;

}
