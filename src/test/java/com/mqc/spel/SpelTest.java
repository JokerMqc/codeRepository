package com.mqc.spel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SPEL表达式测试
 * @author maoqichuan
 * @date 2021年06月30日 10:01
 */
public class SpelTest {
    @Test
    public void helloWorld(){
        // 创建SpEL表达式的解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 解析表达式 'hello' + 'World'
        Expression expression = parser.parseExpression("'hello' + 'World'");
        // 取出解析结果
        String result = expression.getValue().toString();
        System.out.println(result);
    }

    /**
     * 文字表达式
     * @author maoqichuan
     * @date 2021/6/30 10:17
     */
    @Test
    public void literalExpression(){
        ExpressionParser ep = new SpelExpressionParser();

        System.out.println(ep.parseExpression("'HelloWorld'").getValue());
        System.out.println(ep.parseExpression("0xffffff").getValue());
        System.out.println(ep.parseExpression("1.234345e+3").getValue());
        System.out.println(ep.parseExpression("new java.util.Date()").getValue());
    }

    /**
     * SpEL表达式语言特性 变量设置 array
     * @author maoqichuan
     * @date 2021/6/30 10:22
     */
    @Test
    public void spelLanguageFeaturesArray(){
        // 创建SpEL表达式容器
        ExpressionParser ep = new SpelExpressionParser();

        // 创建一个虚拟的容器
        EvaluationContext context = new StandardEvaluationContext();
        String[] students = new String[]{"tom", "jack", "rose", "mark", "lucy"};
        context.setVariable("students",students);

        String result = ep.parseExpression("#students[3]").getValue(context, String.class);
        System.out.println(result);
    }

    /**
     * SpEL表达式语言特性 变量设置 list
     * @author maoqichuan
     * @date 2021/6/30 10:22
     */
    @Test
    public void spelLanguageFeaturesList(){
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context =  new StandardEvaluationContext();
        List<Integer> numbers = (List<Integer>) parser.parseExpression("{1,2,3,4,5}").getValue(List.class);
        System.out.println(String.format("numbers = %s", numbers));
        List<List<String>> listOfLists = (List<List<String>>) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(List.class);
        System.out.println(String.format("listOfLists = %s", listOfLists));
        System.out.println(String.format("listOfLists[0][0] = %s", (listOfLists.get(0)).get(0)));
    }

    /**
     * SpEL表达式语言特性 变量设置 index
     * @author maoqichuan
     * @date 2021/6/30 10:22
     */
    @Test
    public void spelLanguageFeaturesIndex(){
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext ctx = new StandardEvaluationContext();
        User user1 = new User(1, "张三");
        User user2 = new User(2, "李四");
        List<User> users = Arrays.asList(user1, user2);
        ctx.setVariable("users", users);
        String name = parser.parseExpression("#users[1].name").getValue(ctx, String.class);
        System.out.println(String.format("name = %s", name));
    }

    /**
     * SPEL语言特性 变量设置 动态编码
     * @author maoqichuan
     * @date 2021/6/30 11:08
     */
    @Test
    public void spelLanguageFeaturesDynamicCode(){
        ExpressionParser parser = new SpelExpressionParser();
        String value = parser.parseExpression("'abcdef'.substring(2, 3)").getValue(String.class);
        System.out.println(String.format("value = %s", value));
    }

    /**
     * SPEL语言特性 变量设置 操作符
     * @author maoqichuan
     * @date 2021/6/30 11:08
     */
    @Test
    public void spelLanguageFeaturesOperationalCharacter(){
        ExpressionParser parser = new SpelExpressionParser();

        Boolean trueValue1 = parser.parseExpression("2 == 2").getValue(Boolean.class);
        System.out.println("2 == 2 : " + trueValue1);

        Boolean falseValue1 = parser.parseExpression(" 2 < -5.2").getValue(Boolean.class);
        System.out.println("2 < -5.2 : " + falseValue1 );

        //true
        boolean trueValue2 = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        System.out.println("'black' < 'block': " + trueValue2);

        boolean falseValue2 = parser.parseExpression("'xyz' instanceof T(int)").getValue(Boolean.class);
        System.out.println("'xyz' instanceof T(int) :" + falseValue2);

        //true，正则是否匹配
        boolean trueValue3 = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        System.out.println("'5.00' matches '^-?\\d+(\\.\\d{2})?$': " + trueValue3);

        //false
        boolean falseValue3 = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        System.out.println("'5.0067' matches '^-?\\d+(\\.\\d{2})?$' : " + falseValue3);

        // -- AND 与运算 --
        //false
        boolean falseValue4 = parser.parseExpression("true and false").getValue(Boolean.class);
        System.out.println("true and false : " + falseValue4);

        boolean trueValue5 = parser.parseExpression("true or false").getValue(Boolean.class);
        System.out.println("true or false : " + trueValue5);

        // -- ! 非运算--
        //false
        boolean falseValue5 = parser.parseExpression("!true").getValue(Boolean.class);
        System.out.println("!true : " + falseValue5);

        ExpressionParser ep = new SpelExpressionParser();
        // 关系操作符
        System.out.println("5>2" + ep.parseExpression("5>2").getValue());

        System.out.println("2 between {1,9}:" + ep.parseExpression("2 between {1,9}").getValue());
        // 逻辑运算符
        System.out.println("(5>2) and (2==1) :" +ep.parseExpression("(5>2) and (2==1)").getValue());
        // 算术操作符
        System.out.println("100-2^2 :" +ep.parseExpression("100-2^2").getValue());
    }

    /**
     * 算术运算
     * @author maoqichuan
     * @date 2021/6/30 14:06
     */
    @Test
    public void spelLanguageFeaturesArithmeticOperation(){
        ExpressionParser parser = new SpelExpressionParser();
        // Addition
        // 2
        int two = parser.parseExpression("1 + 1").getValue(Integer.class);
        // 'test string'
        String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class); //
        // Subtraction
        // 4
        int four = parser.parseExpression("1 - -3").getValue(Integer.class);
        // -9000
        double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class);
        // Multiplication
        // 6
        int six = parser.parseExpression("-2 * -3").getValue(Integer.class);
        // 24.0
        double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class);
        // Division
        // -2
        int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class);
        // 1.0
        double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class);
        // Modulus
        // 3
        int three = parser.parseExpression("7 % 4").getValue(Integer.class);
        // 1
        int one2 = parser.parseExpression("8 / 5 % 2").getValue(Integer.class);
        // Operator precedence
        // -21
        int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class);
    }

    /**
     * 变量与赋值
     * @author maoqichuan
     * @date 2021/6/30 14:08
     */
    @Test
    public void spelLanguageFeatures(){
        ExpressionParser parser = new SpelExpressionParser();

        User user = new User(1324,"周星驰");
        // 解析表达式所需要的上下文，解析时有一个默认的上下文为：ApplicationContext
        EvaluationContext context = new StandardEvaluationContext();
        // 在上下文中设置变量，变量名为user，内容为user对象
        context.setVariable("user",user);
        // 从用户对象中获得id并+1900，获得解析后的值在ctx上下文中
        Integer value = parser.parseExpression("#user.age + 1900").getValue(context, Integer.class);
        System.out.println(value);

        context.setVariable("name","小小鸟");
        System.out.println(parser.parseExpression("#name").getValue(context));
    }

    /**
     * map过滤
     * @author maoqichuan
     * @date 2021/6/30 16:05
     */
    @Test
    public void test(){
//        ExpressionParser parser = new SpelExpressionParser();
//        StandardEvaluationContext context = new StandardEvaluationContext();
//
//        context.registerFunction("reverseString",
//                StringUtils.class.getDeclaredMethod("reverseString", new Class[] { String.class }));  //注册函数reverseString
//
//        String helloWorldReversed = parser.parseExpression(
//                "#reverseString('hello')").getValue(context, String.class);

        ExpressionParser parser = new SpelExpressionParser();
        Map<String,Integer> root = new HashMap<>();
        root.put("3",3);
        root.put("6",6);
        root.put("8",8);
        StandardEvaluationContext context = new StandardEvaluationContext(root);
//        除了返回所有选定的元素以外，也可以用来获取第一或最后一个值。获得第一条目相匹配的选择的语法是 ^[...]而获得最后一个匹配选择语法是$[...]
        Object subMap = parser.parseExpression("#root.$[#this.value>5]").getValue(context);
        System.out.println(subMap);
    }

    @Data
    @AllArgsConstructor
    class User{
        private int age;
        private String name;
    }
}
