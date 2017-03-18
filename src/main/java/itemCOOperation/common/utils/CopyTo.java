package itemCOOperation.common.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Zephyr Huang
 *
 */
public class CopyTo {
  
  public static void copyTo(File srcFile ,File desFile) {
    try {
      FileUtils.copyFile(srcFile, desFile);
    } catch (IOException e) {
      Info.error("Error occurs while copying "+srcFile.toString());
      e.printStackTrace();
    }
  }
  
  
  public static void main(String[] args) {
    File file1 = new File("D:/qwer.txt");
    File file2 = new File("D:/asdf.txt");
    
    copyTo(file1, file2);
    Info.info("end");
  }
}
