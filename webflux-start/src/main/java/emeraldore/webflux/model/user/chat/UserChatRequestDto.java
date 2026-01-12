package emeraldore.webflux.model.user.chat;

import emeraldore.webflux.model.llmclient.LlmModel;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserChatRequestDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3973614333168836466L;

    private String request;
    private LlmModel llmModel;
}
