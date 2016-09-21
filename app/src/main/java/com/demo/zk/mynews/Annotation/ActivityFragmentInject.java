package com.demo.zk.mynews.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Retention 指定标识的注解如何保存。
RetentionPolicy.SOURCE – 注解将仅保存在源代码级别，将会被编译器丢弃。
RetentionPolicy.CLASS – 将会在编译时期使用，并保存在class中，但JVM不会识别此。
RetentionPolicy.RUNTIME – 此注解将会被JVM识别，理论上将会在任意时期都会存在。

@Documented 表明该注解标识的元素所使用的注解应该出现在javadoc中。

@Target 指定哪种JAVA元素可以使用当前定义的注解，指定类型（ElementType）如下：
ElementType.ANNOTATION_TYPE 注释类型声明。
ElementType.CONSTRUCTOR 构造方法声明。
ElementType.FIELD 字段声明（包括枚举常量）。
ElementType.LOCAL_VARIABLE 局部变量声明。
ElementType.METHOD 方法声明。
ElementType.PACKAGE 包声明。
ElementType.PARAMETER 参数声明。
ElementType.TYPE 类、接口（包括注释类型）或枚举声明。

@Inherited  指示注释类型被自动继承。如果在注释类型声明中存在 Inherited 元注释，
并且用户在某一类声明中查询该注释类型，同时该类声明中没有此类型的注释，则将在该类的超类中自动查询该注释类型。
此过程会重复进行，直到找到此类型的注释或到达了该类层次结构的顶层 (Object) 为止。
如果没有超类具有该类型的注释，则查询将指示当前类没有这样的注释。
注意，如果使用注释类型注释类以外的任何事物，此元注释类型都是无效的。
还要注意，此元注释仅促成从超类继承注释；对已实现接口的注释无效。

@Repeatable （Java8中增加）使用此注解注释的注解，在使用时是可重复使用的。
注意，在Java8之前注解同一个注解在同一个元素上是不可以多次使用的。

注解的定义:

使用关键字@interface来定义，如public @interface ActivityFragmentInject，
注解的权限限定符仅支持public、default(包访问权限)。

元注解:

如上@Target(ElementType.TYPE)，表示注解ClassInfo可用于类、接口、或者枚举类型。

使用default指定变量的默认值


如果程序员的注解中定义了名为value的元素，并且在应用该注解的时候，
如果该元素是唯一需要赋值的一个元素，那么此时无需使用名-值对的语法，
而只需在括号内给出value元素所需的值即可。
 */

/**
 * Activity、Fragment初始化的用到的注解
 */
@Retention(RetentionPolicy.RUNTIME)   //仅当定义的注解的@Retention为RUNTIME时，才能够通过运行时的反射机制来处理注解
@Target({ElementType.TYPE})
public @interface ActivityFragmentInject {

    /**
     * 顶部局的id
     *
     * @return
     */
    int contentViewId() default -1;

    /**
     * 菜单id
     *
     * @return
     */
    int menuId() default -1;

    /**
     * 是否开启侧滑
     *
     * @return
     */
    boolean enableSlidr() default false;

    /**
     * 是否存在NavigationView
     *
     * @return
     */
    boolean hasNavigationView() default false;

    /**
     * 是否处理RefreshLayout，对应父布局为CoordinateLayout加AppbarLayout与RefreshLayout造成的事件冲突
     *
     * @return
     */
    boolean handleRefreshLayout() default false;

    /**
     * toolbar的标题id
     *
     * @return
     */
    int toolbarTitle() default -1;

    /**
     * toolbar的菜单按钮
     *
     * @return
     */
    int toolbarIndicator() default -1;

    /**
     * toolbar菜单默认选中项
     *
     * @return
     */
    int menuDefaultCheckedItem() default -1;

}
