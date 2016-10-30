package itemListOperation.fileList;

import java.io.File;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import itemListOperation.commonUtils.Info;


public class TestFileList {
	private File srcDir;
	private File desDir;
	private FileList srcFileList;
	private FileList desFileList;
	private File f = null;
	
	@Before
	public void setUp() {
		srcDir = new File("E:/src");
		desDir = new File("E:/des");
		//若其中一个不是目录
		if(!(srcDir.isDirectory() && desDir.isDirectory())) {
			Info.error("One or both of the entry dirs are not directories!");
			return;
		}
		
		srcFileList = new FileList(srcDir);
		desFileList = new FileList(desDir);
	}
	
	@After
	public void tearDown() {
		Info.info("Tests ends.");
	}
	
	/**
	 * 第一次运行会有部分文件的文件名被标记“(ToBeDeleted)”
	 */
	@Test
	public void test_FileListFirstTime() {
		srcFileList.copyToAccordingToName(desFileList);
		
		String[] fileNameUnderSrc_0 = {
				"(deleted)src空.txt",
				"common空.txt",
				"des.txt",
				"des空.txt"
		};
		String[] fileNameUnderSrc_1 = {
				"(ToBeDeleted)common.txt",
				"(ToBeDeleted)src.txt",
				"common内容不同.txt"
		};
		//List<File> under_src = ;
		//src
		f = new File(srcDir, "(deleted)src空.txt");
		if(!(f.isFile() && f.length()==0)){err(f);}
		f = new File(srcDir, "(deleted)src空.txt");

		f = new File(srcDir, "(ToBeDeleted)common.txt");
		if(!(f.isFile() && f.length()==0)){err(f);}
		
	}
	
	/**
	 * 第二次运行后，之前被标记“(ToBeDeleted)”的文件会被删除并新建一个同名空文件（即将文件置空）
	 */
	@Test
	public void test_FileListSecondTime() {
		
	}
	
	private void err(File f) {
		Info.error("某处结果出错："+f.getAbsolutePath());
	}
}
