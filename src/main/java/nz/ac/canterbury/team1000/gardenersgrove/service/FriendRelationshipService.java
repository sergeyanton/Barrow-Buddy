package nz.ac.canterbury.team1000.gardenersgrove.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.repository.FriendRelationshipRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for FriendRelationships, defined by the @link{Service} annotation.
 * This class links automatically with @link{FriendRelationshipRepository}, see the @link{Autowired} annotation below.
 */
@Service
public class FriendRelationshipService {

	private final FriendRelationshipRepository friendRelationshipRepository;

	@Autowired
	public FriendRelationshipService(FriendRelationshipRepository friendRelationshipRepository) {
		this.friendRelationshipRepository = friendRelationshipRepository;
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the sender id.
	 *
	 * @param senderId id of the user who initiated the relationships.
	 * @return the list of FriendRelationship objects initiated by the user.
	 */
	public List<FriendRelationship> getRelationshipsBySenderId(Long senderId) {
		return friendRelationshipRepository.findBySenderId(senderId).orElse(null);
	}

	/**
	 * Gets a list of friend relationships from persistence by searching by the receiver id.
	 *
	 * @param receiverId id of the user who is the recipient of the relationship request.
	 * @return the list of FriendRelationship objects sent to the user.
	 */
	public List<FriendRelationship> getRelationshipsByReceiverId(Long receiverId) {
		return friendRelationshipRepository.findByReceiverId(receiverId).orElse(null);
	}

	/**
	 * Adds a new FriendRelationship to persistence
	 *
	 * @param friendRelationship object to persist
	 * @return the saved friend relationship object
	 */
	public FriendRelationship addFriendRelationship(FriendRelationship friendRelationship) {
		return friendRelationshipRepository.save(friendRelationship);
	}


	/**
	 * Updates the relationship of the given sender and receiver to the new status.
	 * @param senderId id of the user who initiated the relationship.
	 * @param receiverId id of the user who received the relationship request.
	 * @param status Status object representing the new state of the relationship.
	 */
	@Transactional
	public void updateFriendRelationship(Long senderId, Long receiverId, Status status) {
		Optional<FriendRelationship> currentRelationship = friendRelationshipRepository.findBySenderIdAndReceiverId(senderId, receiverId);
		currentRelationship.ifPresent(friendRelationship -> {
			friendRelationship.setStatus(status);
			friendRelationshipRepository.save(friendRelationship);
		});
	}

}
