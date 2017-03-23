package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 反射工具类
 *
 * @author yangliqiang
 * @date 2016/9/18
 */
public class ReflectionUtils {

    /**
     * 设置某个成员变量的值
     *
     * @param owner
     * @param fieldName
     * @param value
     * @throws Exception
     * @title: setField
     * @return: void
     */
    public static void setField(Object owner, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> ownerClass = owner.getClass();
        Field field = ownerClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(owner, value);
    }

    /**
     * 当前类中如果找不到变量，则可向上依次查找父类并赋值
     *
     * @param owner
     * @param fieldName
     * @param value
     * @throws Exception
     * @title: setFieldAll
     * @return: void
     */
    public static void setFieldAll(Object owner, String fieldName, Object value) throws IllegalAccessException {
        Class<?> ownerClass = owner.getClass();
        Field field = null;
        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                //LogUtils.d(field + " find : in " + clazz.getName());
                break;
            } catch (Exception e) {
                //LogUtils.d(fieldName + " not find in " + clazz.getName());
            }
        }
        field.setAccessible(true);
        field.set(owner, value);
    }

    /**
     * 获取某个对象的属性
     *
     * @param obj
     * @param field
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getField(Object obj, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Class<?> cl = obj.getClass();
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 当前类中如果找不到变量，则可向上依次查找父类
     * @param obj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static Object getFieldAll(Object obj, String fieldName) throws IllegalAccessException {
        Class<?> ownerClass = obj.getClass();
        Field field = null;
        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                //LogUtils.d(field + " find : in " + clazz.getName());
                break;
            } catch (Exception e) {
               // LogUtils.d(fieldName + " not find in " + clazz.getName());
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 执行某对象方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法返回值
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Class<?> ownerClass = owner.getClass();

        Class<?>[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            if (args[i].getClass() == Integer.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = int.class;
            } else if (args[i].getClass() == Float.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = float.class;
            } else if (args[i].getClass() == Double.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = double.class;
            } else {
                argsClass[i] = args[i].getClass();
            }
        }

        Method method = ownerClass.getDeclaredMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(owner, args);
    }

    /**
     * 调用所有的函数, 包括父类的所有函数
     * @param owner
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     * @title: invokeMethodAll
     * @return: Object
     */
    public static Object invokeMethodAll(Object owner, String methodName, Object... args) throws InvocationTargetException, IllegalAccessException {

        Class<?> ownerClass = owner.getClass();

        Class<?>[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            if (args[i].getClass() == Integer.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = int.class;
            } else if (args[i].getClass() == Float.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = float.class;
            } else if (args[i].getClass() == Double.class) { //一般的函数都是 int 而不是Integer
                argsClass[i] = double.class;
            } else {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = null;

        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, argsClass);
                //LogUtils.d(method + " find : in " + clazz.getName());
            } catch (Exception e) {
                //e.printStackTrace();
                //LogUtils.d(methodName + " not find in " + clazz.getName());
            }
        }
        method.setAccessible(true);
        return method.invoke(owner, args);
    }

    /**
     * 查找函数（可逐级向上查找父类的函数）
     * @param owner
     * @param methodName
     * @param parameterTypes
     * @return
     * @throws Exception
     * @title: invokeMethodAll
     * @return: Method
     */
    public static Method findMethodStepup(Object owner, String methodName, Class<?>... parameterTypes) throws InvocationTargetException, IllegalAccessException {

        Class<?> ownerClass = owner.getClass();
        Method method = null;

        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                //LogUtils.d(method + " find : in " + clazz.getName());
            } catch (Exception e) {
               // LogUtils.d(methodName + " not find in " + clazz.getName());
            }
        }
        method.setAccessible(true);
        return method;
    }

    /**
     * 新建实例
     *
     * @param className 类名
     * @param args      构造函数的参数 如果无构造参数，args 填写为 null
     * @return 新建的实例
     * @throws Exception
     */
    public static Object newInstance(String className, Object[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return newInstance(className, args, null);

    }

    /**
     * 新建实例
     *
     * @param className 类名
     * @param args      构造函数的参数 如果无构造参数，args 填写为 null
     * @return 新建的实例
     * @throws Exception
     */
    public static Object newInstance(String className, Object[] args, Class<?>[] argsType) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> newoneClass = Class.forName(className);

        if (args == null) {
            return newoneClass.newInstance();

        } else {
            Constructor<?> cons;
            if (argsType == null) {
                Class<?>[] argsClass = new Class[args.length];

                for (int i = 0, j = args.length; i < j; i++) {
                    argsClass[i] = args[i].getClass();
                }

                cons = newoneClass.getConstructor(argsClass);
            } else {
                cons = newoneClass.getConstructor(argsType);
            }
            return cons.newInstance(args);
        }

    }
}

