package com.mqc.ssm.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SpEL表达式解析
 * @author maoqichuan
 * @date 2021年06月30日 16:59
 */
public class SpelUtil {
    /**
     * 支持 #p0 参数索引的表达式解析
     * @author maoqichuan
     * @date 2021/6/30 17:00
     * @param rootObject 根对象,method 所在的对象
     * @param spel spel表达式
     * @param method 目标方法
     * @param args 方法入参
     * @return java.lang.String 解析之后的字符串
     */
    public static String parse(Object rootObject, String spel, Method method, Object[] args){
        if (StrUtil.isBlank(spel)){
            return StrUtil.EMPTY;
        }

        // 获取被拦截方法参数名列表(这里使用Spring支持的类库)
        LocalVariableTableParameterNameDiscoverer discoverer
                = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        if (ArrayUtil.isEmpty(parameterNames)){
            return spel;
        }
        // 使用SPEL容器进行解析
        ExpressionParser parser = new SpelExpressionParser();
        // 创建SPEL上下文
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject,method,args,discoverer);
        // 把方法参数放入到SPEL上下文中
        for (int i = 0; i < parameterNames.length; i++){
            context.setVariable(parameterNames[i],args[i]);
        }

        return parser.parseExpression(spel).getValue(context,String.class);
    }
}
