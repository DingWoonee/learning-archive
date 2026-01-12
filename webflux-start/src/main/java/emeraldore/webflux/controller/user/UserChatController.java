package emeraldore.webflux.controller.user;

import emeraldore.webflux.model.user.chat.UserChatRequestDto;
import emeraldore.webflux.model.user.chat.UserChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class UserChatController {

    @PostMapping("/oneshot")
    public Mono<UserChatResponseDto> oneShotChat(
            @RequestBody UserChatRequestDto userChatRequestDto
    ) {
        // 서비스에서 request 이용해서 response 돌려줘야 함.
        return Mono.empty();
    }
}
