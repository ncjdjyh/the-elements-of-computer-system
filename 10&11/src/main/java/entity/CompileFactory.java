package entity;

import core.CompilationEngine;
import entity.BuiltInType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompileFactory {
    /**
     * @Author: ncjdjyh
     * @Date: 2019/1/19 14:01
     * @Description:
     */
    private static Map<BuiltInType, String> compileStrategyMap = new HashMap<>();
    private static final String COMPILE_METHOD_PREFIX = "compile";

    static {
        // 将对应类型生成子元的策略放到 map 中, 通过反射调用
        BuiltInType[] bt = BuiltInType.values();
        for (BuiltInType t : bt) {
            // 名字首字母转大写
            String name = t.name.substring(0, 1).toUpperCase() + t.name.substring(1);
            compileStrategyMap.put(t, COMPILE_METHOD_PREFIX + name);
        }
    }

    public static void executeCompile(Token token) {
//        BuiltInType builtInType = token.getBuiltInType();
//        String className = CompilationEngine.class.getName();
//        String methodName = compileStrategyMap.get(builtInType);
//        try {
//            Class<?> clazz = Class.forName(className);
//            var instance = CompilationEngine.getInstance();
//            Method method = clazz.getMethod(methodName, Token.class);
//            method.invoke(instance, token);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
