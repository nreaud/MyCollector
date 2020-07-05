package service.thread;

import org.springframework.stereotype.Component;

import service.parser.LireScanParser;

@Component
public class UpdaterLauncherThread extends Thread {

	private final int SECOND_SLEEPING = 30;
	private final String URL_LIRESCAN = "https://www.lirescan.me/";
	private final String PATH_MY_CURRENT_STATE = "src/resource/mangaState.json";

	@Override
	public void run() {
		System.out.println("Actuator launcher running");
		do {
			try {
				Thread updaterThread = new UpdaterThread(URL_LIRESCAN, PATH_MY_CURRENT_STATE, new LireScanParser());
				updaterThread.start();
				UpdaterLauncherThread.sleep(SECOND_SLEEPING * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

}
