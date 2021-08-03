package yte.intern.project.common.event;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import yte.intern.project.security.domain.Authority;
import yte.intern.project.security.repository.AuthorityRepository;

import java.util.Set;

@Component
public class CustomEventListener {

    final AuthorityRepository authorityRepository;

    public CustomEventListener(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }


    @EventListener(classes = ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        /*Authority adminAuthority = new Authority(null, "ADMIN", Set.of());
        Authority userAuthority = new Authority(null, "USER", Set.of());

        authorityRepository.save(adminAuthority);
        authorityRepository.save(userAuthority);*/
    }
}
