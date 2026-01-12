package emeraldore.webflux.model.user.chat;

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
public class UserChatResponseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5155000316834938274L;

    private String response;
}
