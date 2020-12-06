package github.rebelhero.scan;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rebelhero
 * @date 2020/12/4
 */
public class KoScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public KoScannerRegistrar() {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 获取所有注解的属性和值
        AnnotationAttributes mapperScanAttrs =
                AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(KoScan.class.getName()));

        if (mapperScanAttrs == null) {
            return;
        }
        KoClassScanner scanner = new KoClassScanner(registry);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        List<String> basePackages = Arrays.stream(mapperScanAttrs.getStringArray("basePackages"))
                .filter(StringUtils::hasText).collect(Collectors.toList());

        scanner.setAnnotationClass();
        scanner.registerFilters();
        Set<BeanDefinitionHolder> beanDefinitionHolders = scanner.doScan(StringUtils.toStringArray(basePackages));
        beanDefinitionHolders.forEach(e -> System.out.println(e.getBeanName()));

    }

}
