package nz.ac.canterbury.seng302.gardenersgrove.service;

import nz.ac.canterbury.seng302.gardenersgrove.entity.FormResult;
import nz.ac.canterbury.seng302.gardenersgrove.repository.FormRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {
    private AppUserRepository appUserRepository;

    public AppUserService(FormRepository formRepository) {
        this.appUserRepository = appUserRepository;
    }

    //Gets all FormResults from persistence
    public List<FormResult> getFormResults() {
        return appUserRepository.findAll();
    }

    // Adds a formResult to persistence
    public FormResult addFormResult(FormResult formResult) {
        return appUserRepository.save(formResult);
    }


}
