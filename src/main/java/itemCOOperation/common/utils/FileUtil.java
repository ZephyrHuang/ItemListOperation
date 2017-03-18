package itemCOOperation.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
  /**
   * 判断文件夹大小是否不为 0
   * @param dir 必须是文件夹，方法内会检查
   * @return true 文件夹内至少有一个大小不为 0 的文件；false 文件夹内没有任何大小不为 0 的文件
   */
  public static boolean isDirNotBlank(final File dir) {
    if(dir == null || !dir.isDirectory()) {
      throw new RuntimeException("An existing directory is needed for the method 'isDirNotBlank'.");
    }
    
    File[] subFiles = dir.listFiles();
    List<File> subDirs = new ArrayList<File>();
    if(subFiles != null) {
      for(final File subFile : subFiles) {
        if(subFile.isFile()) {
          if(subFile.length() > 0) {
            return true;
          } else {
            continue;
          }
        } else {
          subDirs.add(subFile);
        }
      }
      if(!subDirs.isEmpty()) {
        for(final File subDir : subDirs) {
          if(isDirNotBlank(subDir)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * 判断文件夹大小是否为0
   * @param dir 必须是文件夹，方法内会检查
   * @return true 文件夹内没有任何大小不为0的文件；false 文件夹内至少有一个大小不为0的文件
   */
  public static boolean isDirBlank(final File dir) {
    return !isDirNotBlank(dir);
  }
  
  /**
   * 判断文件夹内是否没有任何文件或文件夹
   * @param dir 必须是文件夹，方法内会检查
   * @return true 文件夹内有至少一个文件或文件夹（即使大小为 0）；false 文件夹内没有任何文件或文件夹
   */
  public static boolean isDirNotEmpty(final File dir) {
    if(dir == null || !dir.isDirectory()) {
      throw new RuntimeException("An existing directory is needed for the method 'isDirNotEmpty'.");
    }
    
    File[] files = dir.listFiles();
    if(files != null && files.length != 0) {
      return true;
    }
    return false;
  }
  
  /**
   * 判断文件夹内是否有任何文件或文件夹
   * @param dir 必须是文件夹，方法内会检查
   * @return true 文件夹内没有任何文件或文件夹；false 文件夹内有至少一个文件或文件夹（即使大小为 0）
   */
  public static boolean isDirEmpty(final File dir) {
    return !isDirNotEmpty(dir);
  }
  
  public static boolean isDir(final File file) {
    return file == null ? false : (file.exists() && file.isDirectory());
  }
  
  public static boolean isNotDir(final File file) {
    return !isDir(file);
  }
  
  public static boolean isFile(final File file) {
    return file == null ? false : (file.exists() && file.isFile());
  }
  
  public static boolean isNotFile(final File file) {
    return !isFile(file);
  }
  
  public static boolean deleteDirectory(final File dir) {
    if(isNotDir(dir)) {
      return false;
    }
    
    File[] subfiles = dir.listFiles();
    if(subfiles == null || subfiles.length == 0) {
      return dir.delete();
    } else {
      for(File f : subfiles) {
        if(isDir(f) && deleteDirectory(f)) {
          continue;
        } else if(isFile(f) && f.delete()) {
          continue;
        } else {
          return false;
        }
      }
      return dir.delete();
    }
  }
}
