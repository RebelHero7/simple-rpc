package github.rebelhero.generator;

import cn.hutool.core.util.StrUtil;
import github.rebelhero.extension.KoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @author rebelhero
 * @date 2020/12/4
 */
@Slf4j
public class RpcBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String name = getNameByServiceFindAnnotation(definition);
        if (StrUtil.isNotBlank(name)) {
            return name;
        }
        return super.generateBeanName(definition, registry);
    }

    private String getNameByServiceFindAnnotation(BeanDefinition definition) {
        String beanClassName = definition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            KoService annotation = aClass.getAnnotation(KoService.class);
            if (annotation != null) {
                return annotation.name();
            }
        } catch (ClassNotFoundException e) {
            log.error("获取bean name 出错！");
        }
        return null;
    }

}
