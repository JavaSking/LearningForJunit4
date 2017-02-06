package org.junit.rules;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;

/**
 * 创建临时目录的规则。<br>
 * 例子：
 * 
 * <pre>
 * public static class HasTempFolder {
 * 
 * 	&#064;Rule
 * 	public TemporaryFolder folder= new TemporaryFolder();
 *
 * 	&#064;Test
 * 	public void testUsingTempFolder() throws IOException {
 * 
 * 		File createdFile= folder.newFile(&quot;myfile.txt&quot;);
 * 		File createdFolder= folder.newFolder(&quot;subfolder&quot;);
 * 		// ...
 * 	}
 * }
 * </pre>
 *
 * @since 4.7
 * @author 注释By JavaSking 2017年2月6日
 */
public class TemporaryFolder extends ExternalResource {
	
	/**
	 * 临时目录父文件夹。
	 */
	private final File parentFolder;

	/**
	 * 临时目录。
	 */
	private File folder;

	/**
	 * 构造一个创建临时目录的规则，未指明临时目录父文件夹。
	 */
	public TemporaryFolder() {

		this(null);
	}

	/**
	 * 构造一个创建临时目录的规则，指明临时目录父文件夹。
	 * 
	 * @param parentFolder
	 *            临时目录父文件夹。
	 */
	public TemporaryFolder(File parentFolder) {

		this.parentFolder= parentFolder;
	}

	@Override
	protected void before() throws Throwable {

		create();
	}

	@Override
	protected void after() {

		delete();
	}

	/**
	 * 创建临时目录。
	 * 
	 * @throws IOException
	 *             IO异常。
	 */
	public void create() throws IOException {

		folder= createTemporaryFolderIn(parentFolder);
	}

	/**
	 * 创建指定名称的临时文件。
	 * 
	 * @param fileName
	 *            文件名。
	 * @return 临时文件。
	 * @throws IOException
	 *             IO异常。
	 */
	public File newFile(String fileName) throws IOException {

		File file= new File(getRoot(), fileName);
		if (!file.createNewFile()) {
			throw new IOException("a file with the name \'" + fileName + "\' already exists in the test folder");
		}
		return file;
	}

	/**
	 * 创建随意名称的临时文件。
	 * 
	 * @return 临时文件。
	 * @throws IOException
	 *             IO异常。
	 */
	public File newFile() throws IOException {

		return File.createTempFile("junit", null, getRoot());
	}

	/**
	 * 在临时目录下创建指定名称的文件夹并返回临时目录。
	 * 
	 * @param folder
	 *            文件夹名称。
	 * @return 临时目录。
	 * @throws IOException
	 *             IO异常。
	 */
	public File newFolder(String folder) throws IOException {

		return newFolder(new String[] { folder });
	}

	/**
	 * 在临时目录下创建指定名称的多个文件夹并返回临时目录。
	 * 
	 * @param folderNames
	 *            文件夹名称列表。
	 * @return 临时目录。
	 * @throws IOException
	 *             IO异常。
	 */
	public File newFolder(String... folderNames) throws IOException {
		
		File file= getRoot();
		for (int i= 0; i < folderNames.length; i++) {
			String folderName= folderNames[i];
			file= new File(file, folderName);
			/* 如果存在异常，则只在最后抛出 */
			if (!file.mkdir() && isLastElementInArray(i, folderNames)) {
				throw new IOException("a folder with the name \'" + folderName + "\' already exists");
			}
		}
		return file;
	}

	/**
	 * 判断目标索引是否为最后索引。
	 * 
	 * @param index
	 *            目标索引。
	 * @param array
	 *            数组。
	 * @return 如果目标索引为最后索引则返回true，否则返回false。
	 */
	private boolean isLastElementInArray(int index, String[] array) {

		return index == array.length - 1;
	}

	/**
	 * 在临时目录下新建临时文件夹。
	 * 
	 * @return 新建的临时文件夹。
	 * @throws IOException
	 *             IO异常。
	 */
	public File newFolder() throws IOException {

		return createTemporaryFolderIn(getRoot());
	}

	/**
	 * 在目标父文件夹下创建临时目录并返回。
	 * 
	 * @param parentFolder
	 *            临时目录父文件夹。
	 * @return 临时目录。
	 * @throws IOException
	 *             IO异常。
	 */
	private File createTemporaryFolderIn(File parentFolder) throws IOException {

		File createdFolder= File.createTempFile("junit", "", parentFolder);
		createdFolder.delete();
		createdFolder.mkdir();
		return createdFolder;
	}

	/**
	 * 获取临时目录路径。
	 * 
	 * @return 临时目录路径。
	 */
	public File getRoot() {

		if (folder == null) {
			throw new IllegalStateException("the temporary folder has not yet been created");
		}
		return folder;
	}

	/**
	 * 递归删除临时目录。
	 */
	public void delete() {

		if (folder != null) {
			recursiveDelete(folder);
		}
	}

	/**
	 * 递归删除文件/目录。
	 * 
	 * @param file
	 *            待删除文件，可能为目录。
	 */
	private void recursiveDelete(File file) {

		File[] files= file.listFiles();
		if (files != null) {
			for (File each : files) {
				recursiveDelete(each);
			}
		}
		file.delete();
	}
}
