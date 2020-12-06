package github.rebelhero.scan;

import github.rebelhero.extension.KoService;
import github.rebelhero.generator.RpcBeanNameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author rebelhero
 * @date 2020/12/4
 */
@Slf4j
public class KoClassScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;

    void setAnnotationClass() {

        this.annotationClass = KoService.class;
    }

    KoClassScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        setBeanNameGenerator(new RpcBeanNameGenerator());
    }

    void registerFilters() {
        boolean acceptAllInterfaces = true;
        if (this.annotationClass != null) {
            this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }
        if (acceptAllInterfaces) {
            this.addIncludeFilter(((metadataReader, metadataReaderFactory) -> true));
        }
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            log.warn("未扫描到任何Bean!");
        }
        return beanDefinitionHolders;
    }
}
