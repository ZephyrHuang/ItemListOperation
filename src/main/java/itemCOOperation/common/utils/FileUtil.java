package itemCOOperation.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
  /**
   * 判断文件夹大小是否为0
   * @param dir 必须是文件夹
   * @return true 文件夹内至少有一个大小不为0的文件；false 文件夹内没有任何大小不为0的文件
   */
	public static boolean isDirNotEmpty(final File dir) {
		if(dir == null || !dir.isDirectory()) {
			throw new RuntimeException("An existing directory is needed for the method 'isDirNotEmpty'.");
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
					if(isDirNotEmpty(subDir)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
   * 判断文件夹大小是否为0
   * @param dir 必须是文件夹
   * @return true 文件夹内没有任何大小不为0的文件；false 文件夹内至少有一个大小不为0的文件
   */
	public static boolean isDirEmpty(final File dir) {
	  return !isDirNotEmpty(dir);
	}
}
