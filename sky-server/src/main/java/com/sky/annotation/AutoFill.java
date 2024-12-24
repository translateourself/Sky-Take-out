package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xuxunne
 * technocal: AOP
 * @description: custom annotation, function tag method which need to fill public function field.
 * @since 2024/12/24 17:35
 * @annotation Note:@Target tag the custom annotation function scope
 *                 @Rentention: indicate the annotion will be use in which process
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    // this is a enum type
    OperationType value();
}
