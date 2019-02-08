package kr.sys4u.httpserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheManager {

	private Map<File, FileInformation> cache;

	public CacheManager() {

		this.cache = new HashMap<File, FileInformation>();
	}

	public synchronized void addFile(File file, FileInformation fileInfo) {

		if (!cache.containsKey(file)) {
			cache.put(file, fileInfo);
		}
	}

	public synchronized void addFile(File file) throws IOException {

		if (!cache.containsKey(file)) {
			
			FileInformation fileInfo = new FileInformation();
			fileInfo.setFileData( Files.readAllBytes(file.toPath()));
			fileInfo.setModifiedDate(file.lastModified());
			cache.put(file,fileInfo);
		}
	}

	public synchronized void deleteFile(File file) {

		if (cache.containsKey(file)) {
			cache.remove(file);
		}
	}
	
	public synchronized void updateFile(File keyFile, File newFile) throws IOException {
		
		deleteFile(keyFile);
		addFile(newFile);
		
	}

	public synchronized byte[] getFileData(File file) {
		return (cache.containsKey(file)) ? cache.get(file).getFileData() : null;
	}
	
	public synchronized FileInformation getfileInfo(File file) {
		
		FileInformation fileInformation = null;
		if(cache.containsKey(file)) {
			fileInformation = cache.get(file);
		}
		return fileInformation;
	}

	//객체의 상태를 공개할때는 조심해서 사용할 것
//	public synchronized Map<String, byte[]> getCache() {
//		return cache;
//	}
	
	//Map의 keySet은 view만 제공하기 때문에 객체의 상태를 공개하는것은 아니지만
	//Map이 변경되었을 경우 동기화문제가 발생할 수 있음으로 복사본을 넘겨준다.
	public synchronized Set<File> getKeySet(){
		return Collections.unmodifiableSet(cache.keySet());
	}

}
