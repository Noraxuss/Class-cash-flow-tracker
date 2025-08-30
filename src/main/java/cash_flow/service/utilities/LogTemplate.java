package cash_flow.service.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LogTemplate {
    @JsonProperty("template_key")
    private String templateKey;

    private String hu;
}


