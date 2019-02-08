package kr.sys4u.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import kr.sys4u.httpserver.exception.CanNotReadFileException;

public class FileSender {

	private final Socket socket;
	private final CacheManager cacheManager;
	
	public FileSender(Socket socket, CacheManager cacheManager) {
		this.socket = socket;
		this.cacheManager = cacheManager;
	}
	public void send(File file) {

		
		try(FileInputStream in = new FileInputStream(file);
				OutputStream out = socket.getOutputStream();) {
			
			if(cacheManager.getFileData(file)==null) {
				byte[] data = new byte[8192];
				int count = 0;
				while ((count = in.read(data)) != -1) {

					out.write(data, 0, count);
					out.flush();
			
				}
				System.out.println("Add New File to Cache");
				cacheManager.addFile(file);
			}else {
				System.out.println("File is already exist in cache");
				out.write(cacheManager.getFileData(file));
			}
				
		} catch (IOException e) {
			throw new CanNotReadFileException(e);
			
		}
	}

}
