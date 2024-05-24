package nz.ac.canterbury.team1000.gardenersgrove.service;

import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {

	final Logger logger = LoggerFactory.getLogger(ModerationService.class);
	private final OpenAiService service;

	public ModerationService() {
		final String key = System.getenv("OPENAI_API_KEY");
		service = new OpenAiService(key);
	}

	public boolean isAllowed(String text) {
		ModerationRequest moderationRequest = ModerationRequest.builder().input(text).build();
		boolean isFlagged = service.createModeration(moderationRequest).getResults().getFirst()
			.isFlagged();
		return !isFlagged;
	}
}
