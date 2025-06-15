//package cash_flow.scene;
//
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Getter
//@Setter
//public class ExtraScene extends BaseScene {
//
//    private Stage extraStage;
//    private final Logger logger = LoggerFactory.getLogger(ExtraScene.class);
//
//    public ExtraScene() {
//        this.extraStage = new Stage();
//    }
//
//    public void initializeExtraStage(Parent scene, Stage parentStage) {
//        logger.info("Initializing extra stage");
//        this.extraStage.setTitle(getName());
//        this.extraStage.setScene(new Scene(scene));
//        this.extraStage.setResizable(true);
////        this.extraStage.setMaxHeight(ScreenBounds.getScreenBounds().getHeight());
////        this.extraStage.setMaxWidth(ScreenBounds.getScreenBounds().getWidth());
//        this.extraStage.sizeToScene();
//        this.extraStage.centerOnScreen();
//        this.extraStage.initOwner(parentStage);
//        this.extraStage.initModality(Modality.WINDOW_MODAL);
//    }
//
//    public void closeExtraScene() {
//        if (extraStage != null) {
//            extraStage.close();
//            extraStage = null;
//        }
//    }
//}
