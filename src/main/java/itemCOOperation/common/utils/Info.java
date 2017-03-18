package itemCOOperation.common.utils;

public class Info {
  public static void info() {
    System.out.println("[INFO]");
  }
  public static void info(String s) {
    System.out.println("[INFO] "+s);
  }
  
  public static void info(boolean b) {
    System.out.println("[INFO] "+b);
  }
  
  public static void info(long l) {
    System.out.println("[INFO] "+l);
  }
  
  public static void warn(String s) {
    System.out.println("[WARN] "+s);
  }
  
  public static void error(String s) {
    System.out.println("[ERROR] "+s);
  }
}
