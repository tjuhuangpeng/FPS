/*
package bean;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;


public class JavaCalls {
    private static final String LOG_TAG = "JavaCalls";
    private static final HashMap<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap();
    private static final HashSet<Class<?>> NATIVE_TYPE = new HashSet();

    public JavaCalls() {
    }

    public static <T> T callMethod(Object targetInstance, String methodName, Object... args) {
        try {
            return callMethodOrThrow(targetInstance, methodName, args);
        } catch (Exception var4) {
            LogUtil.w("JavaCalls", "Meet exception when call Method \'" + methodName + "\' in " + targetInstance + var4.getMessage());
            return null;
        }
    }

    public static <T> T callMethodOrThrow(Object targetInstance, String methodName, Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class clazz = targetInstance.getClass();
        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));
        Object result = method.invoke(targetInstance, getParameters(args));
        return (T) result;
    }

    public static <T> T callStaticMethod(String className, String methodName, Object... args) {
        try {
            Class e = Class.forName(className);
            return (T) callStaticMethodOrThrow(e, methodName, args);
        } catch (Exception var4) {
            LogUtil.w("JavaCalls", "Meet exception when call Method \'" + methodName + "\' in " + className + var4.getMessage());
            return null;
        }
    }

    private static Method getDeclaredMethod(Class<?> clazz, String name, Class... parameterTypes) throws NoSuchMethodException, SecurityException {
        Method[] methods = clazz.getDeclaredMethods();
        Method method = findMethodByName(methods, name, parameterTypes);
        return method;
    }

    private static Method findMethodByName(Method[] list, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
        if(name == null) {
            throw new NullPointerException("Method name must not be null.");
        } else {
            Method[] var3 = list;
            int var4 = list.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Method method = var3[var5];
                if(method.getName().equals(name) && compareClassLists(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }

            throw new NoSuchMethodException(name);
        }
    }

    private static boolean compareClassLists(Class<?>[] a, Class<?>[] b) {
        if(a == null) {
            return b == null || b.length == 0;
        } else {
            int length = a.length;
            if(b == null) {
                return length == 0;
            } else if(length != b.length) {
                return false;
            } else {
                boolean result = true;

                for(int i = length - 1; i >= 0; --i) {
                    if(!LangUtils.equals(a[i], b[i]) && (b[i] != null || NATIVE_TYPE.contains(a[i])) && !a[i].isAssignableFrom(b[i]) && (!PRIMITIVE_MAP.containsKey(a[i]) || !((Class)PRIMITIVE_MAP.get(a[i])).equals(PRIMITIVE_MAP.get(b[i])))) {
                        result = false;
                        break;
                    }
                }

                return result;
            }
        }
    }

    public static <T> T callStaticMethodOrThrow(String className, String methodName, Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class clazz = Class.forName(className);
        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));
        Object result = method.invoke((Object)null, getParameters(args));
        return (T) result;
    }

    public static <T> T callStaticMethodOrThrow(Class<?> clazz, String methodName, Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));
        Object result = method.invoke((Object)null, getParameters(args));
        return (T) result;
    }

    public static <T> T getInstance(Class<?> clazz, Object... args) {
        try {
            return getInstanceOrThrow(clazz, args);
        } catch (Exception var3) {
            LogUtil.w("JavaCalls", "Meet exception when make instance as a " + clazz.getSimpleName() + var3.getMessage());
            return null;
        }
    }

    public static <T> T getInstanceOrThrow(Class<?> clazz, Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor constructor = clazz.getConstructor(getParameterTypes(args));
        return (T) constructor.newInstance(getParameters(args));
    }

    public static Object getInstance(String className, Object... args) {
        try {
            return getInstanceOrThrow(className, args);
        } catch (Exception var3) {
            LogUtil.w("JavaCalls", "Meet exception when make instance as a " + className + var3.getMessage());
            return null;
        }
    }

    public static Object getInstanceOrThrow(String className, Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        return getInstanceOrThrow(Class.forName(className), getParameters(args));
    }

    private static Class<?>[] getParameterTypes(Object... args) {
        Class[] parameterTypes = null;
        if(args != null && args.length > 0) {
            parameterTypes = new Class[args.length];

            for(int i = 0; i < args.length; ++i) {
                Object param = args[i];
                if(param != null && param instanceof JavaCalls.JavaParam) {
                    parameterTypes[i] = ((JavaCalls.JavaParam)param).clazz;
                } else {
                    parameterTypes[i] = param == null?null:param.getClass();
                }
            }
        }

        return parameterTypes;
    }

    private static Object[] getParameters(Object... args) {
        Object[] parameters = null;
        if(args != null && args.length > 0) {
            parameters = new Object[args.length];

            for(int i = 0; i < args.length; ++i) {
                Object param = args[i];
                if(param != null && param instanceof JavaCalls.JavaParam) {
                    parameters[i] = ((JavaCalls.JavaParam)param).obj;
                } else {
                    parameters[i] = param;
                }
            }
        }

        return parameters;
    }

    static {
        PRIMITIVE_MAP.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_MAP.put(Byte.class, Byte.TYPE);
        PRIMITIVE_MAP.put(Character.class, Character.TYPE);
        PRIMITIVE_MAP.put(Short.class, Short.TYPE);
        PRIMITIVE_MAP.put(Integer.class, Integer.TYPE);
        PRIMITIVE_MAP.put(Float.class, Float.TYPE);
        PRIMITIVE_MAP.put(Long.class, Long.TYPE);
        PRIMITIVE_MAP.put(Double.class, Double.TYPE);
        PRIMITIVE_MAP.put(Boolean.TYPE, Boolean.TYPE);
        PRIMITIVE_MAP.put(Byte.TYPE, Byte.TYPE);
        PRIMITIVE_MAP.put(Character.TYPE, Character.TYPE);
        PRIMITIVE_MAP.put(Short.TYPE, Short.TYPE);
        PRIMITIVE_MAP.put(Integer.TYPE, Integer.TYPE);
        PRIMITIVE_MAP.put(Float.TYPE, Float.TYPE);
        PRIMITIVE_MAP.put(Long.TYPE, Long.TYPE);
        PRIMITIVE_MAP.put(Double.TYPE, Double.TYPE);
        NATIVE_TYPE.add(Boolean.TYPE);
        NATIVE_TYPE.add(Byte.TYPE);
        NATIVE_TYPE.add(Character.TYPE);
        NATIVE_TYPE.add(Short.TYPE);
        NATIVE_TYPE.add(Integer.TYPE);
        NATIVE_TYPE.add(Float.TYPE);
        NATIVE_TYPE.add(Long.TYPE);
        NATIVE_TYPE.add(Double.TYPE);
    }

    public static class JavaParam<T> {
        public final Class<? extends T> clazz;
        public final T obj;

        public JavaParam(Class<? extends T> clazz, T obj) {
            this.clazz = clazz;
            this.obj = obj;
        }
    }
}
*/
