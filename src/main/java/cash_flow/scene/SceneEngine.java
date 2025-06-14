package cash_flow.scene;

import cash_flow.application.SpringFXMLLoader;
import cash_flow.application.onekeytwovaluemap.OneKeyTwoValueMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

@Component
@Slf4j
public class SceneEngine {

    private static final String SCENE_MAP = "/properties/scene_map.properties";

    private final SpringFXMLLoader springFXMLLoader;
    private final OneKeyTwoValueMap<String, String, String> sceneMap;
    private final OneKeyTwoValueMap<String, Parent, String> sceneCache;
    private final CenterScene centerScene;
    private final SideBarScene sideBarScene;

    @Setter
    private Stage mainStage;

    @Autowired
    public SceneEngine(SpringFXMLLoader springFXMLLoader, OneKeyTwoValueMap<String, String, String> sceneMap, OneKeyTwoValueMap<String, Parent, String> sceneCache, CenterScene centerScene, SideBarScene sideBarScene) {
        this.springFXMLLoader = springFXMLLoader;

        this.sceneMap = sceneMap;
        this.sceneCache = sceneCache;
        this.centerScene = centerScene;
        this.sideBarScene = sideBarScene;
        loadSceneMap();


    }

    private void loadSceneMap() {
        try (InputStream input = getClass().getResourceAsStream(SCENE_MAP)) {
            if (input == null) {
                throw new IOException("Unable to find scenes.properties");
            }

            Properties sceneFiles = new Properties();
            sceneFiles.load(input);

            for (String key : sceneFiles.stringPropertyNames()) {
                String[] values = sceneFiles.getProperty(key).split("=");
                if (values.length == 2) {
                    sceneMap.put(key, values[0], values[1]);

                } else {
                    throw new IllegalArgumentException("Invalid format for key: " + key);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to load scene map", e);
        }
    }

    /**
     * Initializes the primary stage with a base scene and a starter scene.
     */
    public void initializeStage(String starterScene, String loading) {
        log.info("Initializing stage with starter scene: {}", starterScene);

        switchScene(starterScene);

        Scene scene = null;
        try {
            scene = loadScene(starterScene).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainStage.setScene(scene);

        switchScene(loading);

        mainStage.setResizable(true);
        mainStage.show();

        log.debug("Stage initialized and shown.");
    }

    /**
     * Switches the displayed scene based on its logical placement.
     */
    public void switchScene(String sceneName) {
        log.info("Switching to scene: {}", sceneName);

        String scenePlacement = sceneMap.get(sceneName).getValue2();
        log.debug("Scene placement for {} is '{}'", sceneName, scenePlacement);

        try {
            switch (scenePlacement.toLowerCase()) {
                case "side" -> updateScenes(sceneName, sideBarScene);
                case "center" -> updateScenes(sceneName, centerScene);
                case "extra" -> updateScenes(sceneName, new ExtraScene());
                default -> throw new IllegalArgumentException("Invalid scene placement: " + scenePlacement);
            }
        } catch (IOException e) {
            log.error("Error switching scene {}: {}", sceneName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Core scene update logic, handles placement, caching, and controller setup.
     */
    private void updateScenes(String sceneName, BaseScene instance) throws IOException {
        log.info("Updating scene: {}", sceneName);

        instance.setName(sceneName);
        log.debug("Scene instance set to: {}", instance.getClass().getSimpleName());

        Parent scene = loadScene(sceneName).load();
        instance.setScene(scene, instance.getController());

        // Update visual pane placement
        if (sceneName.equals(centerScene.getName())) {
            baseLayoutController.setContentToContentPane(scene);
        } else if (sceneName.equals(sideBarScene.getName())) {
            baseLayoutController.setLeftSideBarToLeftSide(scene);
        } else if (sceneName.equals(instance.getName())) {
            makeExtraStages(instance, sceneName, scene);
        }
    }

    /**
     * Opens an additional window (stage) for an "extra" scene.
     */
    private void makeExtraStages(BaseScene extraScene, String sceneName, Parent parentScene) throws IOException {
        log.debug("Creating extra stage for scene: {}", sceneName);

        ExtraScene scene = (ExtraScene) extraScene;
        scene.initializeExtraStage(parentScene, mainStage);

        Stage extraStage = scene.getExtraStage();

        log.info("Showing ExtraScene '{}' titled '{}'", scene.getName(), extraStage.getTitle());
        extraStage.showAndWait();
    }

    /**
     * Uses reflection to invoke 'onSceneLoad' on a controller, if defined.
     */
//    private void invokeOnSceneLoad(BaseScene instance) {
//        log.debug("Invoking onSceneLoad for: {}", instance.getName());
//        try {
//            Object controller = instance.getController();
//            if (controller == null) {
//                throw new NoSuchMethodException("Controller is null for scene: " + instance.getName());
//            }
//
//            Method onSceneLoadMethod = controller.getClass().getDeclaredMethod("onSceneLoad");
//            onSceneLoadMethod.setAccessible(true);
//            onSceneLoadMethod.invoke(controller);
//            onSceneLoadMethod.setAccessible(false);
//        } catch (NoSuchMethodException e) {
//            log.debug("No onSceneLoad method found for controller: {}", instance.getController());
//        } catch (Exception e) {
//            log.error("Error invoking onSceneLoad for {}: {}", instance.getName(), e.getMessage());
//            throw new RuntimeException("Failed to invoke onSceneLoad", e);
//        }
//    }

    /**
     * Returns a cached scene or loads it freshly.
     * ExtraScenes are always reloaded to avoid reuse conflicts.
     */
//    private Parent cacheScene(String sceneName, BaseScene instance) throws IOException {
//        Parent scene;
//
//        if (instance instanceof ExtraScene) {
//            log.debug("Loading ExtraScene (non-cached): {}", sceneName);
//            FXMLLoader loader = loadScene(sceneName);
//            scene = loader.load();
//            saveController(sceneName, scene, loader.getController(), instance);
//            return scene;
//        }
//
//        if (sceneCache.containsKey(sceneName)) {
//            log.debug("Using cached scene: {}", sceneName);
//            scene = sceneCache.get(sceneName).getValue1();
//        } else {
//            log.debug("Scene not cached. Loading: {}", sceneName);
//            FXMLLoader loader = loadScene(sceneName);
//            scene = loader.load();
//            sceneCache.put(sceneName, scene, sceneMap.get(sceneName).getValue1());
//            saveController(sceneName, scene, loader.getController(), instance);
//        }
//
//        return scene;
//    }

    /**
     * Stores the loaded scene/controller pair into the appropriate scene object.
     */
    private void saveController(String sceneName, Parent scene, Object controller, BaseScene instance) {
        if (instance == null || controller == null) {
            log.debug("Skipping saveController: instance or controller is null.");
            return;
        }

        log.debug("Saving controller for scene: {}", sceneName);

        if (sceneName.equals(centerScene.getName())) {
            centerScene.setScene(scene, controller);
        }
        if (sceneName.equals(sideBarScene.getName())) {
            sideBarScene.setScene(scene, controller);
        }
        if (sceneName.equals(instance.getName())) {
            instance.setScene(scene, controller);
        }
    }

    /**
     * Helper to load an FXML scene by name from the sceneMap.
     */
    private FXMLLoader loadScene(String name) throws IOException {
        try {
            log.debug("Loading FXML for scene: {}", name);
            String fxmlFile = sceneMap.get(name).getValue1();

            if (fxmlFile == null) {
                throw new IllegalArgumentException("Scene not found in map: " + name);
            }

            return springFXMLLoader.load(fxmlFile);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
