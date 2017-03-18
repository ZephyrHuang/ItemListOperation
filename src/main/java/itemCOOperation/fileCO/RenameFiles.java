package itemCOOperation.fileCO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import itemCOOperation.common.utils.Info;

public class RenameFiles {
  
  public static void renameFiles(File tarFile) {
    if(!tarFile.isDirectory()) {
      Info.error("入口不是文件夹！");
      return;
    }
    List<File> childFileList = new ArrayList<File>();
    File childFile = null;
    for(String childFileName: tarFile.list()) {
      childFile = new File(tarFile, childFileName);
      if(childFile.isFile() && childFileName.startsWith("(deleted)") && childFile.length()==0) {
        if(!childFile.renameTo(new File(tarFile, childFileName.substring(9)))) {
          Info.error("重命名失败！"+tarFile.getAbsolutePath()+"\\"+childFileName);
          return;
        }
      }
      else if(childFile.isDirectory()) {
        RenameFiles.renameFiles(childFile);
      }
    }
      
  }

  public static void main(String[] args) {
    RenameFiles.renameFiles(new File("E:/directory1"));
    Info.info("end");
  }

}
