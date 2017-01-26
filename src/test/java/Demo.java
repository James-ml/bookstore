import java.text.MessageFormat;

/**
 * Created by Jims on 2017/1/26.
 */
public class Demo {
    public static void main(String[] args) {
        /*
		 * 包含了点位符的字符串就是模板！
		 * 点位符：{0}、{1}、{2}
		 * 可变参数，需要指定模板中的点位符的值！有几个点位符就要提供几个参数
		 */
        String s = MessageFormat.format("{0}或{1}错误！", "用户名", "密码");
        System.out.println(s);
    }
}
