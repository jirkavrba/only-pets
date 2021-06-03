package dev.vrba.onlypets.security;

import dev.vrba.onlypets.entity.User;
import dev.vrba.onlypets.repository.UsersRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RegistrationHandler implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final UsersRepository repository;

    @Autowired
    public RegistrationHandler(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    @EventListener
    public void onApplicationEvent(@NotNull InteractiveAuthenticationSuccessEvent event) {
        OAuth2User authenticatedUser = ((OAuth2AuthenticationToken) event.getSource()).getPrincipal();
        Map<String, Object> attributes = authenticatedUser.getAttributes();

        String id = (String) attributes.get("id");
        String username = (String) attributes.get("username");
        String avatar = this.getAvatarUrl(id, (String) attributes.get("avatar"));

        User user = this.repository.findByDiscordId(id)
                .map(entity -> updateUserProperties(entity, username, avatar))
                .orElseGet(() -> registerUser(id, username, avatar));

        this.updateSecurityContext(user, authenticatedUser);
    }

    private User updateUserProperties(@NotNull User user, @NotNull String username, @NotNull String avatar) {
        user.setUsername(username);
        user.setAvatar(avatar);

        return this.repository.save(user);
    }

    private User registerUser(@NotNull String discordId, @NotNull String username, @NotNull String avatar) {
        User user = new User(discordId, username, avatar);

        return this.repository.save(user);
    }

    private void updateSecurityContext(@NotNull User user, @NotNull OAuth2User original) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                original,
                original.getAuthorities()
        );

        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getAvatarUrl(@NotNull String id, @Nullable String avatar) {
        if (avatar == null) {
            // TODO: Supply an actual URL
            return "Default avatar URL";
        }

        boolean animated = avatar.contains("a_");
        String extension = animated ? "gif" : "png";

        return String.format("https://cdn.discordapp.com/avatars/%s/%s.%s?size=128", id, avatar, extension);
    }
}
