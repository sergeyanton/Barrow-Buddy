package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.AppUser;
import nz.ac.canterbury.seng302.gardenersgrove.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {
    private AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    // Gets all AppUsers
    public List<AppUser> getFormResults() {
        return appUserRepository.findAll();
    }

    // Adds a AppUser
    public AppUser addFormResult(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

}
