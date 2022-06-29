package util;

public class AntiInjection {
    public static String bonify(String s){
        return s.replaceAll("['\"]", "`");
    }

}
