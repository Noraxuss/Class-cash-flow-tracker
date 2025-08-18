package cash_flow.style_manager;

import cash_flow.scene.SceneConfiguration;
import javafx.scene.Scene;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * StyleManager is responsible for managing the application's current theme (light or dark),
 * notifying registered UI controllers about theme changes, and applying the correct CSS
 * stylesheets to their associated scenes.
 *
 * Listeners (typically controllers) implement ThemeChangeListener and register themselves
 * along with the scene-specific CSS paths using addListener().
 */
@Component
@Slf4j
public class StyleManager {

    @Getter
    private Style currentTheme = Style.LIGHT;

    // Registered listeners that will be notified when theme changes
    private final List<ThemeChangeListener> listeners = new ArrayList<>();

    // Maps each listener to the corresponding scene-specific CSS paths
    private final WeakHashMap<ThemeChangeListener, SceneMeta> sceneMetaMap = new WeakHashMap<>();

    private final SceneConfiguration sceneConfiguration;

    public StyleManager(SceneConfiguration sceneConfiguration) {
        this.sceneConfiguration = sceneConfiguration;
    }

    /**
     * Toggles the current theme between LIGHT and DARK and notifies all registered listeners.
     */
    public void toggleTheme() {
        currentTheme = (currentTheme == Style.LIGHT) ? Style.DARK : Style.LIGHT;
        log.info("Toggled theme to: {}", currentTheme);
        notifyListeners();
    }

    /**
     * Registers a ThemeChangeListener and associates it with its SceneMeta (scene-specific CSS).
     *
     * @param listener the controller or UI component interested in theme changes
     */
    public void addListener(ThemeChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            sceneMetaMap.put(listener,
                    new SceneMeta(sceneConfiguration.getCssLight(), sceneConfiguration.getCssDark()));

            log.debug("Registered theme listener: {} with scene CSS: light={}, dark={}",
                    listener.getClass().getSimpleName(),
                    sceneConfiguration.getCssLight(), sceneConfiguration.getCssDark());
        }
    }

    /**
     * Unregisters a listener and removes its SceneMeta.
     * Should be called when a controller is no longer active.
     *
     * @param listener the listener to remove
     */
    public void removeListener(ThemeChangeListener listener) {
        listeners.remove(listener);
        sceneMetaMap.remove(listener);
        log.debug("Removed theme listener: {}", listener.getClass().getSimpleName());
    }

    /**
     * Notifies all registered listeners about the current theme change.
     * They can then apply the correct styles to their scenes.
     */
    private void notifyListeners() {
        log.debug("Notifying {} listeners of theme change to: {}", listeners.size(), currentTheme);
        for (ThemeChangeListener listener : listeners) {
            try {
                listener.onThemeChanged(currentTheme);
            } catch (Exception e) {
                log.error("Failed to notify listener: {}", listener.getClass().getSimpleName(), e);
            }
        }
    }

    /**
     * Applies the correct theme stylesheet (base + scene-specific) to the given scene,
     * based on the registered listener’s SceneMeta.
     *
     * @param scene the JavaFX Scene to update
     * @param listener the associated listener that holds the scene’s CSS paths
     */
    public void toggleSceneStyle(Scene scene, ThemeChangeListener listener) {
        SceneMeta meta = sceneMetaMap.get(listener);
        if (meta == null) {
            log.warn("No SceneMeta found for listener: {}", listener.getClass().getSimpleName());
            return;
        }

        log.info("Applying theme to scene: {}, base={}, scene-specific={}",
                currentTheme,
                currentTheme.getStylePath(),
                meta.getCssPath(currentTheme));

        scene.getStylesheets().clear();
        try {
            String baseCssUrl = getClass().getResource(currentTheme.getStylePath()).toExternalForm();
            scene.getStylesheets().add(baseCssUrl);
            log.info("Base stylesheet applied: {}", baseCssUrl);
        } catch (Exception e) {
            log.error("Failed to load base stylesheet: {}", currentTheme.getStylePath(), e);
        }

        try {
            String sceneCssUrl = getClass().getResource(meta.getCssPath(currentTheme)).toExternalForm();
            scene.getStylesheets().add(sceneCssUrl);
            log.info("Scene-specific stylesheet applied: {}", sceneCssUrl);
        } catch (Exception e) {
            log.error("Failed to load scene-specific stylesheet: {}", meta.getCssPath(currentTheme), e);
        }
        for (String stylesheet : scene.getStylesheets()) {
            log.info("Applying theme to scene: {}", stylesheet);
        }
    }
}
