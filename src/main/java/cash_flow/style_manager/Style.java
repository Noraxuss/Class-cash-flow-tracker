package cash_flow.style_manager;

import lombok.Getter;

@Getter
public enum Style {

    DARK("/base_css/dark.css"),
    LIGHT("/base_css/light.css"),;

    private final String stylePath;

    Style(String stylePath) {
        this.stylePath = stylePath;
    }
}
