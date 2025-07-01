package cash_flow.style_manager;

public record SceneMeta(String lightCssPath, String darkCssPath) {

    public String getCssPath(Style currentTheme) {
        return switch (currentTheme) {
            case LIGHT -> lightCssPath;
            case DARK -> darkCssPath;
        };
    }
}

