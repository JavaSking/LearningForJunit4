package org.junit.rules;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;

/**
 * ������ʱĿ¼�Ĺ���<br>
 * ���ӣ�
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
 * @author ע��By JavaSking 2017��2��6��
 */
public class TemporaryFolder extends ExternalResource {
	
	/**
	 * ��ʱĿ¼���ļ��С�
	 */
	private final File parentFolder;

	/**
	 * ��ʱĿ¼��
	 */
	private File folder;

	/**
	 * ����һ��������ʱĿ¼�Ĺ���δָ����ʱĿ¼���ļ��С�
	 */
	public TemporaryFolder() {

		this(null);
	}

	/**
	 * ����һ��������ʱĿ¼�Ĺ���ָ����ʱĿ¼���ļ��С�
	 * 
	 * @param parentFolder
	 *            ��ʱĿ¼���ļ��С�
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
	 * ������ʱĿ¼��
	 * 
	 * @throws IOException
	 *             IO�쳣��
	 */
	public void create() throws IOException {

		folder= createTemporaryFolderIn(parentFolder);
	}

	/**
	 * ����ָ�����Ƶ���ʱ�ļ���
	 * 
	 * @param fileName
	 *            �ļ�����
	 * @return ��ʱ�ļ���
	 * @throws IOException
	 *             IO�쳣��
	 */
	public File newFile(String fileName) throws IOException {

		File file= new File(getRoot(), fileName);
		if (!file.createNewFile()) {
			throw new IOException("a file with the name \'" + fileName + "\' already exists in the test folder");
		}
		return file;
	}

	/**
	 * �����������Ƶ���ʱ�ļ���
	 * 
	 * @return ��ʱ�ļ���
	 * @throws IOException
	 *             IO�쳣��
	 */
	public File newFile() throws IOException {

		return File.createTempFile("junit", null, getRoot());
	}

	/**
	 * ����ʱĿ¼�´���ָ�����Ƶ��ļ��в�������ʱĿ¼��
	 * 
	 * @param folder
	 *            �ļ������ơ�
	 * @return ��ʱĿ¼��
	 * @throws IOException
	 *             IO�쳣��
	 */
	public File newFolder(String folder) throws IOException {

		return newFolder(new String[] { folder });
	}

	/**
	 * ����ʱĿ¼�´���ָ�����ƵĶ���ļ��в�������ʱĿ¼��
	 * 
	 * @param folderNames
	 *            �ļ��������б�
	 * @return ��ʱĿ¼��
	 * @throws IOException
	 *             IO�쳣��
	 */
	public File newFolder(String... folderNames) throws IOException {
		
		File file= getRoot();
		for (int i= 0; i < folderNames.length; i++) {
			String folderName= folderNames[i];
			file= new File(file, folderName);
			/* ��������쳣����ֻ������׳� */
			if (!file.mkdir() && isLastElementInArray(i, folderNames)) {
				throw new IOException("a folder with the name \'" + folderName + "\' already exists");
			}
		}
		return file;
	}

	/**
	 * �ж�Ŀ�������Ƿ�Ϊ���������
	 * 
	 * @param index
	 *            Ŀ��������
	 * @param array
	 *            ���顣
	 * @return ���Ŀ������Ϊ��������򷵻�true�����򷵻�false��
	 */
	private boolean isLastElementInArray(int index, String[] array) {

		return index == array.length - 1;
	}

	/**
	 * ����ʱĿ¼���½���ʱ�ļ��С�
	 * 
	 * @return �½�����ʱ�ļ��С�
	 * @throws IOException
	 *             IO�쳣��
	 */
	public File newFolder() throws IOException {

		return createTemporaryFolderIn(getRoot());
	}

	/**
	 * ��Ŀ�길�ļ����´�����ʱĿ¼�����ء�
	 * 
	 * @param parentFolder
	 *            ��ʱĿ¼���ļ��С�
	 * @return ��ʱĿ¼��
	 * @throws IOException
	 *             IO�쳣��
	 */
	private File createTemporaryFolderIn(File parentFolder) throws IOException {

		File createdFolder= File.createTempFile("junit", "", parentFolder);
		createdFolder.delete();
		createdFolder.mkdir();
		return createdFolder;
	}

	/**
	 * ��ȡ��ʱĿ¼·����
	 * 
	 * @return ��ʱĿ¼·����
	 */
	public File getRoot() {

		if (folder == null) {
			throw new IllegalStateException("the temporary folder has not yet been created");
		}
		return folder;
	}

	/**
	 * �ݹ�ɾ����ʱĿ¼��
	 */
	public void delete() {

		if (folder != null) {
			recursiveDelete(folder);
		}
	}

	/**
	 * �ݹ�ɾ���ļ�/Ŀ¼��
	 * 
	 * @param file
	 *            ��ɾ���ļ�������ΪĿ¼��
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
