package com.zjutkz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kangzhe on 16/4/2.
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface Knight {

    //要替换的资源名称，比如TextColor
    String resName();

    //夜间资源id
    int[] nightResId();

    //日常资源id
    int[] dayResId();
}
