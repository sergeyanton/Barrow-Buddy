package nz.ac.canterbury.team1000.gardenersgrove.service;

import java.time.LocalDate;
import java.util.List;
import nz.ac.canterbury.team1000.gardenersgrove.entity.FriendRelationship;
import nz.ac.canterbury.team1000.gardenersgrove.entity.User;
import nz.ac.canterbury.team1000.gardenersgrove.repository.FriendRelationshipRepository;
import nz.ac.canterbury.team1000.gardenersgrove.repository.UserRepository;
import nz.ac.canterbury.team1000.gardenersgrove.util.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class FriendRelationshipServiceTest {

	@Autowired
	private FriendRelationshipRepository friendRelationshipRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FriendRelationshipService friendRelationshipService;

	private User testUser;

	private User testOtherUser;

	@BeforeEach
	public void setUp() {
		// FriendRelationship entity has a relationship with User entity so have to establish the users first
		User loggedInUser = new User("Jane", "Doe", "janedoe@gmail.com", "Password!123", LocalDate.now(), null);
		User otherUser = new User("Other", "User", "other@gmail.com", "Password!456", LocalDate.now(), null);
		testUser = userRepository.save(loggedInUser);
		testOtherUser = userRepository.save(otherUser);
	}

	@Test
	public void testAddFriendRelationship_ReturnsCorrectRelationship() {
		FriendRelationship newFriendship = new FriendRelationship(testUser, testOtherUser, Status.APPROVED);
		FriendRelationship savedFriendship = friendRelationshipService.addFriendRelationship(newFriendship);

		Assertions.assertNotNull(savedFriendship.getId());
		Assertions.assertEquals(savedFriendship.getId(), newFriendship.getId());
		Assertions.assertEquals(savedFriendship.getStatus(), Status.APPROVED);
	}

	@Test
	public void testAddFriendRelationship_SearchForRelationship_ReturnsCorrectRelationship() {
		FriendRelationship newFriendship = new FriendRelationship(testUser, testOtherUser, Status.APPROVED);
		friendRelationshipService.addFriendRelationship(newFriendship);
		FriendRelationship searchedFriendship = friendRelationshipService.getFriendRelationship(testUser.getId(), testOtherUser.getId());

		Assertions.assertNotNull(searchedFriendship.getId());
		Assertions.assertEquals(searchedFriendship.getId(), newFriendship.getId());
		Assertions.assertEquals(searchedFriendship.getStatus(), Status.APPROVED);

	}

	@Test
	public void testGetRelationshipsByReceiverId_NoRelationshipAdded_ReturnsNoRelationships() {
		List<FriendRelationship> sentFriendships = friendRelationshipService.getRelationshipsBySenderId(testUser.getId());
		List<FriendRelationship> receivedFriendships = friendRelationshipService.getRelationshipsByReceiverId(testUser.getId());

		Assertions.assertTrue(sentFriendships.isEmpty());
		Assertions.assertTrue(receivedFriendships.isEmpty());
	}

	@Test
	public void testGetRelationshipsBySenderId_AddedRelationship_HasRelationship() {
		FriendRelationship newFriendship = new FriendRelationship(testUser, testOtherUser, Status.APPROVED);
		friendRelationshipService.addFriendRelationship(newFriendship);

		List<FriendRelationship> searchedFriendship = friendRelationshipService.getRelationshipsBySenderId(testUser.getId());
		Assertions.assertTrue(searchedFriendship.contains(newFriendship));
	}

	@Test
	public void testGetRelationshipsByReceiverId_AddedRelationship_HasRelationship() {
		FriendRelationship newFriendship = new FriendRelationship(testOtherUser, testUser,
			Status.APPROVED);
		friendRelationshipService.addFriendRelationship(newFriendship);

		List<FriendRelationship> searchedFriendship = friendRelationshipService.getRelationshipsByReceiverId(
			testUser.getId());
		Assertions.assertTrue(searchedFriendship.contains(newFriendship));
	}

	@Test
	public void testUpdateRelationship_SearchRelationship_StatusIsUpdated() {
		FriendRelationship newFriendship = new FriendRelationship(testUser, testOtherUser,
			Status.PENDING);
		friendRelationshipService.addFriendRelationship(newFriendship);

		friendRelationshipService.updateFriendRelationship(testUser.getId(), testOtherUser.getId(), Status.APPROVED);
		FriendRelationship searchedFriendship = friendRelationshipService.getFriendRelationship(testUser.getId(), testOtherUser.getId());
		Assertions.assertEquals(searchedFriendship.getStatus(), Status.APPROVED);
	}

	@Test
	public void testGetRelationship_NoRelationship_ReturnsNull() {
		FriendRelationship friendRelationship = friendRelationshipService.getFriendRelationship(testUser.getId(), testOtherUser.getId());
		Assertions.assertNull(friendRelationship);
	}


	@TestConfiguration
	static class TestConfig {

		@Bean
		@Primary
		public EmailService emailService() {
			return Mockito.mock(EmailService.class);
		}

		@Bean
		@Primary
		public FriendRelationshipService friendRelationshipService(FriendRelationshipRepository friendRelationshipRepository) {
			return new FriendRelationshipService(friendRelationshipRepository);
		}
	}

}
