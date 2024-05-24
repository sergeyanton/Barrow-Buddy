package nz.ac.canterbury.team1000.gardenersgrove.service;

import java.util.Optional;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.Garden;
import nz.ac.canterbury.team1000.gardenersgrove.repository.FriendRelationshipRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.GardenRepository;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendRelationshipService {

	private final FriendRelationshipRepository friendRelationshipRepository;


	@Autowired
	public FriendRelationshipService(FriendRelationshipRepository friendRelationshipRepository) {
		this.friendRelationshipRepository = friendRelationshipRepository;
	}

	public FriendRelationship getRelationshipsBySenderId(Long senderId) {
		return friendRelationshipRepository.findBySenderId(senderId).orElse(null);
	}

	public FriendRelationship getRelationshipsByReceiverId(Long receiverId) {
		return friendRelationshipRepository.findByReceiverId(receiverId).orElse(null);
	}

	public FriendRelationship addFriendRelationship(FriendRelationship friendRelationship) {
		return friendRelationshipRepository.save(friendRelationship);
	}

	public void updateFriendRelationship(Long senderId, Long receiverId, Status status) {
		// TODO implement this
	}


}
