package MidTermProject.controller.impl;

import MidTermProject.controller.interfaces.IThirdPartyController;
import MidTermProject.model.Users.ThirdParty;
import MidTermProject.repository.ThirdPartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @PostMapping("/users/third_party")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveThirdParty(@RequestBody @Valid ThirdParty user) {
        thirdPartyRepository.save(user);
    }
}
