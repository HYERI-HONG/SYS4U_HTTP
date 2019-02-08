package kr.sys4u.httpserver;

import java.io.File;
import java.io.IOException;

public class RunnableCacheChecker implements Runnable {

	private CacheManager cacheManager;

	public RunnableCacheChecker(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(10000);

				for (File cacheFile : cacheManager.getKeySet()) {

					File diskFile = new File(cacheFile.getAbsolutePath());

					if (!diskFile.exists()) {
						cacheManager.deleteFile(cacheFile);
						continue;
					}
					
					if (diskFile.lastModified() != cacheManager.getfileInfo(cacheFile).getModifiedDate()) {
						try {
							cacheManager.updateFile(diskFile, cacheFile);
						} catch (IOException e) {
							//처리
						}
						continue;
					}
				}
			} catch (InterruptedException e) {

			}
		}
	}
}
