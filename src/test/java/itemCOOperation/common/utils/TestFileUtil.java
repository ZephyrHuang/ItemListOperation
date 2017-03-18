package itemCOOperation.common.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static itemCOOperation.common.utils.Info.*;

public class TestFileUtil {
  private String rootDir         = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/TestFileUtil";
  private String rootDir_subDir  = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/TestFileUtil/subDir";
  private String rootDir_subFile = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/TestFileUtil/subFile";
  private String subDir_subFile  = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/TestFileUtil/subDir/subFile";
  private File rootDirectory = null;
  
  @Before
  public void setUp() throws IOException {
    createDir(rootDir);
    createDir(rootDir_subDir);
    createFile(rootDir_subFile);
    createFile(subDir_subFile);
    rootDirectory = new File(rootDir);
  }
  
  @After
  public void tearDown() {
    info("Test ends.");
  }
  
  @Test
  public void testDeleteDirectory() {
    info("=================testDeleteDirectory=================");
    assertTrue(FileUtil.deleteDirectory(rootDirectory));
    assertTrue(!rootDirectory.exists());
  }
  
  public void createFile(String path) throws IOException {
    File file = null;
    if(StringUtils.isBlank(path) || (file = new File(path)).isDirectory()) {
      fail();
    }
    if(!file.createNewFile()) {
      fail();
    }
  }
  
  public void createDir(String path) {
    File dir = null;
    if(StringUtils.isBlank(path) || (dir = new File(path)).isFile()) {
      fail();
    }
    if(!dir.mkdir()) {
      fail();
    }
  }
}
