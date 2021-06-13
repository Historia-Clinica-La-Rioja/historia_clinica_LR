package net.pladema.hsi.extensions.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(ExtensionsAutoConfiguration.class)
@Configuration
public @interface EnableExtensions {

}
