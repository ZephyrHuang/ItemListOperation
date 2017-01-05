package itemListOperation.fileList;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
		srcDir.mkdir();
		desDir.mkdir();
		//校验。两者必须都是目录。
		if(!(srcDir.isDirectory() && desDir.isDirectory())) {
			Info.error("One or both of the entry dirs are not directories!");
			return;
		}
		
		srcFileList = new FileList(srcDir);
		desFileList = new FileList(desDir);
		
		//构造src文件夹
		//子文件
		FileWriter fw = null;
		String[] filesIn_srcDir = {
				"common.txt",
				"common_blank.txt",
				"common_diff_content.txt",
				"src.txt",
				"src_blank.txt"
		};
		for(String fileName: filesIn_srcDir) {
			new File(srcDir, fileName);
			//给非空文件写入内容
			if(!fileName.contains("blank")) {
				
			}
		}
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
	
	/**
	 * 对比两组文件名的不同
	 */
	@Test
	public void checkDiffOfFiles() {
		List<String> list = new ArrayList<String>();
		list.add("SNIS-782");
		list.add("IDBD-745");
		list.add("SNIS-759");
		list.add("SNIS-742");
		list.add("IPZ-802");
		list.add("IPZ-784");
		list.add("IPZ-758R");
		list.add("IPZ-740");
		list.add("IPZ-722");
		list.add("IPZ-588");
		list.add("IDBD-704");
		list.add("IPZ-659R");
		list.add("IPZ-707");
		list.add("IPZ-677");
		list.add("IDBD-664");
		list.add("IPZ-602");
		list.add("IPZ-628R");
		list.add("IPZ-642");
		list.add("IPZ-616R");
		list.add("IPZ-443");
		list.add("IPZ-516");
		list.add("IPZ-545R");
		list.add("IPZ-503");
		list.add("IPZ-531");
		list.add("IPZ-487");
		list.add("IPZ-473");
		list.add("IPZ-559");
		list.add("IPZ-574R");
		list.add("IPZ-471");
		list.add("IPZ-429");
		list.add("IPZ-410");
		list.add("IPZ-393");
		list.add("IPZ-373");
		list.add("IPZ-349");
		list.add("IPZ-323");
		list.add("IDBD-505");
		list.add("IPZ-252");
		list.add("IPZ-228");
		list.add("IPZ-201");
		list.add("SUPD-100");
		list.add("IPZ-120R");
		list.add("IDBD-446");
		list.add("IPZ-095");
		list.add("IPZ-138R");
		list.add("IPZ-070");
		list.add("IPZ-050");
		list.add("IPZ-160");
		list.add("IPZ-182");
		list.add("IDBD-419");
		list.add("IPTD-873");
		list.add("IPTD-977");
		list.add("IPTD-894");
		list.add("IPTD-878");
		list.add("IPTD-959R");
		list.add("IPTD-932R");
		list.add("IPTD-911R");
		list.add("IPZ-001");
		list.add("IDBD-409");
		list.add("IPSD-044");
		list.add("IPTD-851");
		list.add("IPTD-838");
		list.add("IPTD-826");
		list.add("IPTD-813R");
		list.add("IPTD-797");
		list.add("IPTD-786");
		list.add("IPTD-796");
		list.add("IPTD-777");
		list.add("IPTD-760");
		list.add("IDBD-303");
		list.add("IPTD-661");
		list.add("IPTD-750");
		list.add("IPTD-701");
		list.add("IPTD-683");
		list.add("IDBD-304");
		list.add("IDBD-287");
		list.add("IDBD-290");
		list.add("IPTD-681");
		list.add("IPTD-724");
		list.add("IPTD-714");
		list.add("IPTD-553");
		list.add("IPTD-635");
		list.add("IPTD-570");
		list.add("IDBD-235");
		list.add("IPSD-041");
		list.add("IDBD-245");
		list.add("IPTD-609");
		list.add("IPTD-601");
		list.add("IPTD-576");
		list.add("IPTD-625");
		list.add("IPTD-540");
		list.add("IPTD-530");
		list.add("IPTD-523");
		list.add("IPTD-508");
		list.add("IPTD-496");
		list.add("IDBD-197");
		list.add("IPTD-489");
		list.add("IPTD-476");
		list.add("IPTD-466");
		list.add("IPTD-460");
		list.add("IPTD-433");
		list.add("IPTD-427");
		list.add("IPTD-414");
		list.add("IPTD-405");
		list.add("IPTD-446");
		list.add("IPTD-442");
		list.add("IPTD-393");
		list.add("IPTD-385");
		
		List<String> list2 = new ArrayList<String>();
		File rootDir = new File("E:\\directory\\ACTRICE\\jessica");
		//检查是否为文件夹
		if(!rootDir.isDirectory()) {
			Info.error("rootDir is NOT a directory!");
			return;
		}
		for(File file: rootDir.listFiles()) {
			list2.add(file.getName().substring(0, file.getName().indexOf(".")));
		}
		
		Info.info("The followings are files in list but not in list2:");
		List<String> tempList = new ArrayList<String>();
		tempList.addAll(list2);
		for(String s: list) {
			if(tempList.contains(s)) {
				tempList.remove(s);
			} else {
				Info.info("    "+s);
			}
		}
		
		Info.info("======================================================");
		
		Info.info("The followings are files in list2 but not in list:");
		tempList.clear();
		tempList.addAll(list);
		for(String s: list2) {
			if(tempList.contains(s)) {
				tempList.remove(s);
			} else {
				Info.info("    "+s);
			}
		}
		
		Info.info("=========================END==========================");
	}
	
	public void main(String[] args) {
		Info.info("====================MAIN BEGINS====================");
		checkDiffOfFiles();
		Info.info("====================MAIN ENDS====================");
	}
}
