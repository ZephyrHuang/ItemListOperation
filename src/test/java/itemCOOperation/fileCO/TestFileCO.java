package itemCOOperation.fileCO;

import static org.junit.Assert.*;
import static itemCOOperation.common.utils.Info.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import itemCOOperation.common.utils.FileUtil;


public class TestFileCO {
  private static File srcDir;
  private static File desDir;
  private static FileCO srcFileCO;
  private static FileCO desFileCO;
  //本源入口目录路径
  private static String srcDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/src";
  //目标入口目录路径
  private static String desDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/des";
  
  @BeforeClass
  public static void setUp() throws Exception {
    srcDir = new File(srcDirPath);
    desDir = new File(desDirPath);
    srcDir.mkdir();
    desDir.mkdir();
    //1. 校验。两者必须都是目录。
    if(!(srcDir.isDirectory() && desDir.isDirectory())) {
      error("One or both of the entry dirs are not directories!");
      fail();
    }
    
    //2. 清空之前创建的测试目录
    if(!FileUtil.deleteDirectory(srcDir) || !FileUtil.deleteDirectory(desDir)) {
      error("Error occurs when deleting remaining resources!");
      fail();
    }
    
    //3. 构造 src 文件夹
    //3.1 src/*.txt
    createFiles(srcDir, "src");
    //3.2 src/common/*.txt
    createFiles(new File(srcDir, "common"      ), "src");
    //3.3 src/common_empty/*.txt
    createFiles(new File(srcDir, "common_empty"), "src");
    //3.4 src/subsrc/*.txt
    createFiles(new File(srcDir, "subsrc"      ), "src");
    //3.5 src/subsrc_empty/*.txt
    createFiles(new File(srcDir, "subsrc_empty"), "src");
    //3.6 src/subsrc_blank/*.txt
    createFiles(new File(srcDir, "subsrc_blank"), "src");
    
    //4. 构造 des 文件夹
    //4.1 des/*.txt
    createFiles(desDir, "des");
    //4.2 des/common/*.txt
    createFiles(new File(desDir, "common"      ), "des");
    //4.3 des/common_empty/*.txt
    createFiles(new File(desDir, "common_empty"), "des");
    //4.4 des/subdes/*.txt
    createFiles(new File(desDir, "subdes"      ), "des");
    //4.5 des/subdes_empty/*.txt
    createFiles(new File(desDir, "subdes_empty"), "des");
    
    srcFileCO = new FileCO(srcDir);
    desFileCO = new FileCO(desDir);
  }
  
  @AfterClass
  public static void tearDown() {
    info("Tests end.");
  }
  
  /**
   * 第一次运行
   * @throws IOException 
   */
  @SuppressWarnings("serial")
  @Test
  public void testCopyToAccordingToName_FirstTime() throws IOException {
    info("==================== 第一遍 ====================");
    //The method to be tested.
    srcFileCO.copyToAccordingToName(desFileCO);
    
    //key:filename，value:content
    //src文件夹内的所有文件和文件夹
    Map<String, String> filesInSrc = new HashMap<String, String>() {
      {
        put("(Deleted)src_blank.txt",  "");
        put("(ToBeDeleted)common.txt", "common");
        put("(ToBeDeleted)src.txt",    "src");
        put("common_blank.txt",        "");
        put("common_diff.txt",         "commonsrc");
        put("des.txt",                 "");
        put("des_blank.txt",           "");
      }
    };
    Map<String, String> dirsInSrc = new HashMap<String, String>() {
      {
        put("(Deleted)subsrc_blank" , "");
        put("common"                , "");
        put("common_empty"          , "");
        put("subsrc"                , "");
        put("(Deleted)subsrc_empty" , "");
        put("subdes"                , "");
        put("subdes_empty"          , "");
      }
    };
    Map<String, String> filesInSrc_common = new HashMap<String, String>() {
      {
        put("(ToBeDeleted)common.txt", "common");
        put("common_blank.txt",        "");
        put("common_diff.txt",         "commonsrc");
        put("(ToBeDeleted)src.txt",    "src");
        put("(Deleted)src_blank.txt",  "");
        put("des.txt",                 "");
        put("des_blank.txt",           "");
      }
    };
    Map<String, String> filesInSrc_common_empty = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInSrc_subsrc = new HashMap<String, String>() {
      {
        put("(Deleted)common_blank.txt",    "");
        put("(Deleted)src_blank.txt",       "");
        put("(ToBeDeleted)common_diff.txt", "commonsrc");
        put("(ToBeDeleted)common.txt",      "common");
        put("(ToBeDeleted)src.txt",         "src");
      }
    };
    Map<String, String> filesInSrc_subsrc_empty = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInSrc_subdes = new HashMap<String, String>() {
      {
        put("common.txt",       "");
        put("common_blank.txt", "");
        put("common_diff.txt",  "");
        put("des.txt",          "");
        put("des_blank.txt",    "");
      }
    };
    Map<String, String> filesInSrc_subdes_empty = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInSrc_subsrc_blank = new HashMap<String, String>() {
      {
        put("subsrc_blank.txt", "");
      }
    };
    
    //des文件夹内的所有文件和文件夹
    Map<String, String> filesInDes = new HashMap<String, String>() {
      {
        put("common.txt",       "common");
        put("common_blank.txt", "");
        put("common_diff.txt",  "commondesdes");
        put("des.txt",          "desdes");
        put("des_blank.txt",    "");
        put("src.txt",          "src");
      }
    };
    Map<String, String> dirsInDes = new HashMap<String, String>() {
      {
        put("common",       "");
        put("common_empty", "");
        put("subsrc",       "");
        put("subdes",       "");
        put("subdes_empty", "");
      }
    };
    Map<String, String> filesInDes_common = new HashMap<String, String>() {
      {
        put("common.txt",       "common");
        put("common_blank.txt", "");
        put("common_diff.txt",  "commondesdes");
        put("src.txt",          "src");
        put("des.txt",          "desdes");
        put("des_blank.txt",    "");
      }
    };
    Map<String, String> filesInDes_common_blank = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInDes_subdes = new HashMap<String, String>() {
      {
        put("common.txt",       "common");
        put("common_blank.txt", "");
        put("common_diff.txt",  "commondesdes");
        put("des.txt",          "desdes");
        put("des_blank.txt",    "");
      }
    };
    Map<String, String> filesInDes_subdes_blank = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInDes_subsrc = new HashMap<String, String>() {
      {
        put("common.txt",      "common");
        put("common_diff.txt", "commonsrc");
        put("src.txt",         "src");
      }
    };

    //检查
    List<File> actualFileInSrc = new LinkedList<File>();
    List<File> actualDirInSrc  = new LinkedList<File>();
    for(File f : srcDir.listFiles()) {
      if(f.isFile()) {
        actualFileInSrc.add(f);
      } else {
        actualDirInSrc.add(f);
      }
    }
    List<File> actualFileInDes = new LinkedList<File>();
    List<File> actualDirInDes  = new LinkedList<File>();
    for(File f : desDir.listFiles()) {
      if(f.isFile()) {
        actualFileInDes.add(f);
      } else {
        actualDirInDes.add(f);
      }
    }
    File tmp = null;
    //src
    assertFilesMeetRequirements(srcDir, filesInSrc, actualFileInSrc);
    assertFilesMeetRequirements(srcDir, dirsInSrc , actualDirInSrc);
    assertFilesMeetRequirements(tmp = new File(srcDir, "common"),                filesInSrc_common,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "common_empty"),          filesInSrc_common_empty, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(srcDir, "subsrc"),                filesInSrc_subsrc,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "(Deleted)subsrc_empty"), filesInSrc_subsrc_empty, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(srcDir, "subdes"),                filesInSrc_subdes,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "subdes_empty"),          filesInSrc_subdes_empty, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(srcDir, "(Deleted)subsrc_blank"), filesInSrc_subsrc_blank, Arrays.asList(tmp.listFiles()));
    //des
    assertFilesMeetRequirements(desDir, filesInDes, actualFileInDes);
    assertFilesMeetRequirements(desDir, dirsInDes , actualDirInDes);
    assertFilesMeetRequirements(tmp = new File(desDir, "common"),       filesInDes_common,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "common_empty"), filesInDes_common_blank, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(desDir, "subsrc"),       filesInDes_subsrc,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "subdes"),       filesInDes_subdes,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "subdes_empty"), filesInDes_subdes_blank, new ArrayList<File>());
    
    //测试第二次运行
    testCopyToAccordingToName_SecondTime();
  }
  
  /**
   * 第二次运行
   * @throws IOException 
   */
  //@Test
  @SuppressWarnings("serial")
  public void testCopyToAccordingToName_SecondTime() throws IOException {
    info();
    info("==================== 第二遍 ====================");
    //The method to be tested.
    srcFileCO.copyToAccordingToName(desFileCO);
    
    //key:filename，value:content
    //src文件夹内的所有文件和文件夹
    Map<String, String> filesInSrc = new HashMap<String, String>() {
      {
        put("common_blank.txt", "");
        put("common_diff.txt", "commonsrc");
        put("common.txt", "");
        put("des_blank.txt", "");
        put("des.txt", "");
        put("src.txt", "");
      }
    };
    Map<String, String> dirsInSrc = new HashMap<String, String>() {
      {
        put("common", "");
        put("common_empty", "");
        put("subsrc", "");
        put("subdes", "");
        put("subdes_empty", "");
      }
    };
    Map<String, String> filesInSrc_common = new HashMap<String, String>() {
      {
        put("common.txt", "");
        put("common_blank.txt", "");
        put("common_diff.txt", "commonsrc");
        put("src.txt", "");
        put("des.txt", "");
        put("des_blank.txt", "");
      }
    };
    Map<String, String> filesInSrc_common_blank = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInSrc_subsrc = new HashMap<String, String>() {
      {
        put("common.txt", "");
        put("common_diff.txt", "");
        put("src.txt", "");
      }
    };
    Map<String, String> filesInSrc_subdes = new HashMap<String, String>() {
      {
        put("common.txt", "");
        put("common_blank.txt", "");
        put("common_diff.txt", "");
        put("des.txt", "");
        put("des_blank.txt", "");
      }
    };
    Map<String, String> filesInSrc_subdes_blank = new HashMap<String, String>() {
      //blank
    };
    
    //des文件夹内的所有文件和文件夹
    Map<String, String> filesInDes = new HashMap<String, String>() {
      {
        put("common.txt", "common");
        put("common_blank.txt", "");
        put("common_diff.txt", "commondesdes");
        put("des.txt", "desdes");
        put("des_blank.txt", "");
        put("src.txt", "src");
      }
    };
    Map<String, String> dirsInDes = new HashMap<String, String>() {
      {
        put("common", "");
        put("common_empty", "");
        put("subsrc", "");
        put("subdes", "");
        put("subdes_empty", "");
      }
    };
    Map<String, String> filesInDes_common = new HashMap<String, String>() {
      {
        put("common.txt", "common");
        put("common_blank.txt", "");
        put("common_diff.txt", "commondesdes");
        put("src.txt", "src");
        put("des.txt", "desdes");
        put("des_blank.txt", "");
      }
    };
    Map<String, String> filesInDes_common_blank = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInDes_subdes = new HashMap<String, String>() {
      {
        put("common.txt", "common");
        put("common_blank.txt", "");
        put("common_diff.txt", "commondesdes");
        put("des.txt", "desdes");
        put("des_blank.txt", "");
      }
    };
    Map<String, String> filesInDes_subdes_blank = new HashMap<String, String>() {
      //blank
    };
    Map<String, String> filesInDes_subsrc = new HashMap<String, String>() {
      {
        put("common.txt", "common");
        put("common_diff.txt", "commonsrc");
        put("src.txt", "src");
      }
    };

    
    //检查
    List<File> actualFileInSrc = new LinkedList<File>();
    List<File> actualDirInSrc  = new LinkedList<File>();
    for(File f : srcDir.listFiles()) {
      if(f.isFile()) {
        actualFileInSrc.add(f);
      } else {
        actualDirInSrc.add(f);
      }
    }
    List<File> actualFileInDes = new LinkedList<File>();
    List<File> actualDirInDes  = new LinkedList<File>();
    for(File f : desDir.listFiles()) {
      if(f.isFile()) {
        actualFileInDes.add(f);
      } else {
        actualDirInDes.add(f);
      }
    }
    File tmp = null;
    //src
    assertFilesMeetRequirements(srcDir, filesInSrc, actualFileInSrc);
    assertFilesMeetRequirements(srcDir, dirsInSrc , actualDirInSrc);
    assertFilesMeetRequirements(tmp = new File(srcDir, "common"),       filesInSrc_common,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "common_empty"), filesInSrc_common_blank, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(srcDir, "subsrc"),       filesInSrc_subsrc,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "subdes"),       filesInSrc_subdes,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(srcDir, "subdes_empty"), filesInSrc_subdes_blank, new ArrayList<File>());
    //des
    assertFilesMeetRequirements(desDir, filesInDes, actualFileInDes);
    assertFilesMeetRequirements(desDir, dirsInDes , actualDirInDes);
    assertFilesMeetRequirements(tmp = new File(desDir, "common"),       filesInDes_common,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "common_empty"), filesInDes_common_blank, new ArrayList<File>());
    assertFilesMeetRequirements(tmp = new File(desDir, "subsrc"),       filesInDes_subsrc,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "subdes"),       filesInDes_subdes,       Arrays.asList(tmp.listFiles()));
    assertFilesMeetRequirements(tmp = new File(desDir, "subdes_empty"), filesInDes_subdes_blank, new ArrayList<File>());
  }
  
  /**
   * 创建 dir 文件夹内的文件
   * @param dir 被创建文件所属的文件夹
   * @param srcOrDes 枚举："src"/"des"
   * @return
   * @throws IOException 
   */
  private static void createFiles(File dir, String srcOrDes) throws Exception {
    if(dir == null) {
      error("The directory can't be null in which files to be created.");
      fail();
    }
    dir.mkdir();
    if(FileUtil.isNotDir(dir) || StringUtils.isBlank(srcOrDes)) {
      error("When creating files in directory:" + dir.getAbsolutePath());
      fail();
    }
    if(dir.getName().contains("empty")) {
      return;
    }
    
    String s = srcOrDes;
    String t = srcOrDes.equals("src") ? "src" : "desdes";//为保证*_diff.txt文件的大小不同
    Map<String, String> filesToBeCreated = new HashMap<String, String>(5);
    if(dir.getName().contains("blank")) {
      filesToBeCreated.put("subsrc_blank.txt", "");
    } else {
      filesToBeCreated.put("common.txt"       , "common"    );//共有文件，内容相同
      filesToBeCreated.put("common_blank.txt" , ""          );//共有文件，内容为空
      filesToBeCreated.put("common_diff.txt"  , "common" + t);//共有文件，内容不同
      filesToBeCreated.put(s + ".txt"         , t           );//src独有文件，内容非空
      filesToBeCreated.put(s + "_blank.txt"   , ""          );//src独有文件，内容为空
    }
    
    File tmpFile = null;
    BufferedWriter bw = null;
    for(Entry<String, String> entry: filesToBeCreated.entrySet()) {
      //创建文件并写入内容
      tmpFile = new File(dir, entry.getKey());
      try {
        tmpFile.createNewFile();
        bw = new BufferedWriter(new FileWriter(tmpFile));
        bw.write(entry.getValue());
        bw.flush();
        bw.close();
      } catch (IOException e) {
        error("Error occurs when creating file: " + tmpFile.getAbsolutePath());
        throw e;
      } finally {
        bw.close();
      }
    }
    
    return;
  }
  
  /**
   * 检查文件及其内容或文件夹是否符合要求<br/>
   * 1. 理应存在的文件和文件夹与实际存在的文件和文件夹应该一一对应，不多不少<br/>
   * 2. 文件与文件内容应相符
   * @param rootDir 被检查文件或文件夹所在的目录
   * @param filesAndContents 理应存在的文件及其各自对应的内容
   * @param actualFiles 实际存在的文件
   * @return true if the check is passed;false otherwise
   * @throws IOException 
   */
  private void assertFilesMeetRequirements(File rootDir, Map<String, String> filesAndContents, List<File> actualFiles) throws IOException {
    String fileOrDir = " 空文件夹 ";//标识所检查的是空文件夹/文件列表/文件夹列表
    if(CollectionUtils.isNotEmpty(actualFiles)) {
      if(actualFiles.get(0).isFile()) {
        fileOrDir = " 文件 ";
      } else {
        fileOrDir = " 文件夹 ";
      }
    }
    
    List<String> correctFileName = new LinkedList<String>();
    for(Entry<String, String> e : filesAndContents.entrySet()) {
      if(StringUtils.isNotBlank(e.getKey())) {
        correctFileName.add(e.getKey());
      }
    }
    List<String> actualFileName = new LinkedList<String>();
    for(File f : actualFiles) {
      if(StringUtils.isNotBlank(f.getName())) {
        actualFileName.add(f.getName());
      }
    }
    
    //检查是否一一对应，不多不少
    if(!" 空文件夹 ".equals(fileOrDir)) {
      assertTrue(fileOrDir + "检查，实际文件（夹）中缺少若干文件（夹）： " + rootDir.getAbsolutePath(),
          actualFileName.containsAll(correctFileName));
      assertTrue(fileOrDir + "检查，实际文件（夹）中冗余若干文件（夹）：" + rootDir.getAbsolutePath(),
          correctFileName.containsAll(actualFileName));
    }
    
    File file = null;
    if(CollectionUtils.isNotEmpty(actualFiles) && actualFiles.get(0).isFile()) {
      //检查文件
      for(Entry<String, String> e : filesAndContents.entrySet()) {
        file = new File(rootDir, e.getKey());
        //文件存在
        assertTrue("文件不存在：" + rootDir.getAbsolutePath() + "\\" + file.getName(), FileUtil.isFile(file));
        //内容符合
        assertTrue("内容不符合：" + rootDir.getAbsolutePath() + "\\" + file.getName(), e.getValue().equals(FileUtils.readFileToString(file)));
      }
    } else {
      //检查文件夹
      for(Entry<String, String> e : filesAndContents.entrySet()) {
        file = new File(rootDir, e.getKey());
        //文件夹存在
        assertTrue("文件夹不存在 " + rootDir.getAbsolutePath() + "\\" + file.getName(), FileUtil.isDir(file));
      }
    }
  }
  
  public static void main(String[] args) throws IOException {

  }
}
