package nz.ac.canterbury.team1000.gardenersgrove.service;

import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for moderating text content using OpenAI's moderation API.
 */
@Service
public class ModerationService {
	final Logger logger = LoggerFactory.getLogger(ModerationService.class);
	private final OpenAiService service;

	/**
	 * Constructs a new ModerationService and initializes the OpenAiService with the API key
	 * from the environment variable.
	 */
	public ModerationService() {
		final String key = System.getenv("OPENAI_API_KEY");
		service = new OpenAiService(key);
	}

	/**
	 * Checks if the provided text is allowed based on OpenAI's moderation API.
	 *
	 * @param text the text to be checked for moderation
	 * @return true if the text is allowed, false otherwise
	 */
	public boolean isAllowed(String text) {
		ModerationRequest moderationRequest = ModerationRequest.builder().input(text).build();
		boolean isFlagged = service.createModeration(moderationRequest).getResults().get(0)
			.isFlagged();
		return !isFlagged;
	}
}
