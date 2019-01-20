package entity;

import core.CompilationEngine;
import entity.BuiltInType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompileFactory {
    /**
     * @Auther: ncjdjyh
     * @Date: 2019/1/19 14:01
     * @Description:
     */
    private static Map<BuiltInType, String> compileStrategyMap = new HashMap<>();

    static {
        compileStrategyMap.put(BuiltInType.CLASS_VAR_DEC, "compileClassVarDec");
        compileStrategyMap.put(BuiltInType.SUBROUTINE, "compileSubroutine");
    }

    public static void executeCompile(BuiltInType builtInType) {
        String className = "core.CompilationEngine";
        String methodName = compileStrategyMap.get(builtInType);
        try {
            Class<?> clazz = Class.forName(className);
            // 实例化
            var instance = CompilationEngine.getInstance();
            Method method = clazz.getDeclaredMethod(methodName);
            method.invoke(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
