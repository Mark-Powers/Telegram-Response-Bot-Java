package cafe.seafarers.bot;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendLocation;
import com.pengrad.telegrambot.response.BaseResponse;

import cafe.seafarers.plugins.PluginManager;

public class ResponseBot {
	private TelegramBot bot;
	private boolean isRunning;

	public ResponseBot(String token) {
		bot = new TelegramBot(token);
		isRunning = false;
	}

	public boolean startUpdateListener(PluginManager manager) {
		if (!isRunning) {
			bot.setUpdatesListener(new UpdatesListener() {
				public int process(List<Update> list) {
					for (Update update : list) {
						BaseRequest request = null;
						if(update.message().text() == null) {
							if(request instanceof SendLocation) {
								SendLocation location = (SendLocation) request;
								System.out.println(location.getParameters().get("latitude"));
								System.out.println(location.getParameters().get("longitude"));
								System.out.println("location!");	
							}
							System.out.println(update);
							continue;
						}
						if (update.message().text().startsWith("/")) {
							request = manager.handleCommand(update);
						} else {
							request = manager.handleMessage(update);
						}
						if (request != null) {
							bot.execute(request);
						}
					}
					return UpdatesListener.CONFIRMED_UPDATES_ALL;
				}
			});

			isRunning = true;
			return true;
		}
		return false;

	}

	public boolean stopUpdateListener() {
		if (isRunning) {
			bot.removeGetUpdatesListener();
			isRunning = false;
			return true;
		}
		return false;
	}
}
