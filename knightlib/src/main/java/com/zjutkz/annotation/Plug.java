package com.zjutkz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kangzhe on 16/4/10.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Plug {

    String resName();

    String[] nightResName();

    String[] dayResName();

    String[] valueName();

    String packageName();
}
