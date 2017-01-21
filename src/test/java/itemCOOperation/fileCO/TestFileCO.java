package itemCOOperation.fileCO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import itemCOOperation.common.utils.Info;


public class TestFileCO {
	private File srcDir;
	private File desDir;
	private FileCO srcFileCO;
	private FileCO desFileCO;
	private File f = null;
	//本源入口目录路径
	private static String srcDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/src";
	//目标入口目录路径
	private static String desDirPath = "C:/Users/Zhefeng/workspaces/Main_workspace/ItemListOperation/src/test/resources/des";
	
	@Before
	public void setUp() throws IOException {
		srcDir = new File(srcDirPath);
		desDir = new File(desDirPath);
		srcDir.mkdir();
		desDir.mkdir();
		//1. 校验。两者必须都是目录。
		if(!(srcDir.isDirectory() && desDir.isDirectory())) {
			Info.error("One or both of the entry dirs are not directories!");
			return;
		}
		
		srcFileCO = new FileCO(srcDir);
		desFileCO = new FileCO(desDir);
		
		//2. 构造 src 文件夹
		//2.1 src/*.txt
		createFiles(srcDir, "src");
		//2.2 src/common/*.txt
		createFiles(new File(srcDir, "common"	   ), "src");
		//2.3 src/common_blank/*.txt
		createFiles(new File(srcDir, "common_blank"), "src");
		//2.4 src/subsrc/*.txt
		createFiles(new File(srcDir, "subsrc"	   ), "src");
		//2.5 src/subsrc_blank/*.txt
		createFiles(new File(srcDir, "subsrc_blank"), "src");
		
		//3. 构造 des 文件夹
		//3.1 des/*.txt
		createFiles(desDir, "des");
		//3.2 des/common/*.txt
		createFiles(new File(desDir, "common"	   ), "des");
		//3.3 des/common_blank/*.txt
		createFiles(new File(desDir, "common_blank"), "des");
		//3.4 des/subdes/*.txt
		createFiles(new File(desDir, "subdes"	   ), "des");
		//3.5 des/subdes_blank/*.txt
		createFiles(new File(desDir, "subdes_blank"), "des");
		
	}
	
	@After
	public void tearDown() {
		Info.info("Tests end.");
	}
	
	/**
	 * 第一次运行会有部分文件的文件名被标记“(ToBeDeleted)”
	 */
	@Test
	public void testCopyToAccordingToName_FirstTime() {
		//The method be tested.
		srcFileCO.copyToAccordingToName(desFileCO);
		
		@SuppressWarnings("serial")
		Map<String, String> filesInSrc = new HashMap<String, String>(){
			{
				put("(deleted)src空.txt", null);
				
			}
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
	
	/**
	 * 创建 dir 文件夹内的文件
	 * @param dir 被创建文件所属的文件夹
	 * @param srcOrDes 枚举："src"/"des"
	 * @return
	 * @throws IOException 
	 */
	private void createFiles(File dir, String srcOrDes) throws IOException {
		dir.mkdir();
		if(!dir.isDirectory()) {
			Info.error("When creating file:"+dir.getAbsolutePath());
			Info.error(dir.getAbsolutePath()+"is not a directory！");
			throw new IOException();
		}
		if(dir.getName().contains("blank")) {
			return;
		}
		
		String t = srcOrDes;
		Map<String, String> filesToBeCreated = new HashMap<String, String>();
		filesToBeCreated.put("common.txt"		, "common"	  );//共有文件，内容相同
		filesToBeCreated.put("common_blank.txt"	, ""		  );//共有文件，内容为空
		filesToBeCreated.put("common_diff.txt"	, "common "+t );//共有文件，内容不同
		filesToBeCreated.put(t+".txt"			, t		  	  );//src独有文件，内容非空
		filesToBeCreated.put(t+"_blank.txt"		, ""		  );//src独有文件，内容为空
		
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
				Info.error("Error occurs when creating file: "+tmpFile.getAbsolutePath());
				throw e;
			}
		}
		return;
	}
	
	/**
	 * 检查 dir 文件夹内的文件是否符合要求
	 * @param dir 被检查文件所属的文件夹
	 * @param srcOrDes 枚举："src"/"des"
	 * @return
	 */
	private boolean checkFiles(File dir, String srcOrDes) {
		return false;//TODO
		
	}
	public static void main(String[] args) throws IOException {
		
	}
}
