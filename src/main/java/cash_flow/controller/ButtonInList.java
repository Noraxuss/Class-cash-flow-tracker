//package cash_flow.controller;
//
//import javafx.application.Application;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//
//public class ButtonInList extends Application {
//
//
//    @Override
//    public void start(Stage primaryStage) {
//        ObservableList<String> items = FXCollections.observableArrayList();
//        items.addAll(
//                "Adam", "Alex", "Alfred", "Albert"
//        );
//        final ListView<String> listView = new ListView<>(items);
//        listView.setCellFactory(list -> new ButtonCell());
//
//        Button refButton = new Button("Reference");
//        refButton.setOnAction(arg0 -> System.out.println("Reference Button Handler"));
//
//        Pane root = new VBox();
//        root.getChildren().addAll(refButton, listView);
//        primaryStage.setScene(new Scene(root, 200, 250));
//        primaryStage.show();
//    }
//
//    static class ButtonCell extends ListCell<String> {
//
//        @Override
//        public void updateItem(final String item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (item != null) {
//                Button button = new Button(item);
//                button.setOnAction
//                        (arg0 -> System.out.println(item + " Button Handler"));
//                setGraphic(button);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
