package presentation;


import java.util.ArrayList;

import domain.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GUI {
    private Scene myScene;
    private AnchorPane root;

    private AnchorPane paneButtons;
    private AnchorPane paneScreen;
    private AnchorPane paneTable;

    private Button bAmerica;
    private Button bEuropa;
    private Button bAsia;

	private Button bVer;
	private Button bkillInfected;
	private Button bKillPlayer;

    private Label lInfeccion;
    private Label lVer;

	private GridPane gpMatrix;
	private Label[][] lMatrix;
	private ScrollPane scrollPaneBoard;
	private ScrollPane scrollPaneTable;
	private TableView<Event> tEvent;
 	private ObservableList<Event> oEvent = FXCollections.observableArrayList();

        public GUI() {
        root = new AnchorPane();
        root.setPrefSize(750, 600);

        setPaneButtonsMenu();
        setPaneScreen();
        setPaneTable();

        myScene = new Scene(root, 750, 600);
    }

	public void start(Stage primaryStage) {
		primaryStage.setTitle("Infección");
		primaryStage.setScene(getMyScene());
		primaryStage.setMinWidth(750);
		primaryStage.setMinHeight(600);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	


	public void setLMatrix(String[][] data) {
		lMatrix = new Label[data.length][data[0].length];
		double cellWidth = 500.0 / data[0].length;
		double cellHeight = 400.0 / data.length;

		for (int i = 0; i < lMatrix.length; i++) {
			for (int j = 0; j < lMatrix[0].length; j++) {
				lMatrix[i][j] = new Label(data[i][j]);
				lMatrix[i][j].setPrefSize(cellWidth, cellHeight);
				lMatrix[i][j].setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-size: 14px;");
				lMatrix[i][j].getStyleClass().add("map-cell");
			}
		}
	}
	
	public void setGPMatrix(Label[][] lMatrix) {
		gpMatrix = new GridPane();
		for (int i = 0; i < lMatrix.length; i++) {
			for (int j = 0; j < lMatrix[0].length; j++) {
				gpMatrix.add(lMatrix[i][j], j, i);
			}
		}
		scrollPaneBoard = new ScrollPane(gpMatrix);
		scrollPaneBoard.setPrefViewportWidth(500);
		scrollPaneBoard.setPrefViewportHeight(400);
	
		AnchorPane.setTopAnchor(scrollPaneBoard, 0.0);
		AnchorPane.setLeftAnchor(scrollPaneBoard, 0.0);
		AnchorPane.setRightAnchor(scrollPaneBoard, 0.0);
		AnchorPane.setBottomAnchor(scrollPaneBoard, 0.0);
	
		paneScreen.getChildren().add(scrollPaneBoard);
	}

    public void setPaneScreen() {
        paneScreen = new AnchorPane();
        paneScreen.setPrefSize(500, 400);

        lInfeccion = new Label("Infeccion");
        lInfeccion.setLayoutX(200);
        lInfeccion.setLayoutY(200);

        lVer = new Label("Ver Continente");
        lVer.setLayoutX(180);
        lVer.setLayoutY(300);
        paneScreen.getChildren().addAll(lInfeccion, lVer);
		root.getChildren().add(paneScreen);
    }

	public void setPaneTable() {
			paneTable = new AnchorPane();
			paneTable.setLayoutX(500.0);
			paneTable.setPrefSize(250, 400);
	
			tEvent = new TableView<>();
			tEvent.setPrefSize(250, 400);
			tEvent.setLayoutX(0);
			tEvent.setLayoutY(10);
	
			TableColumn<Event, String> caseColumn = new TableColumn<>("Encuentro");
			caseColumn.setCellValueFactory(new PropertyValueFactory<>("collition"));
			caseColumn.setPrefWidth(180);
	
			TableColumn<Event, String> efectColumn = new TableColumn<>("Efecto");
			efectColumn.setCellValueFactory(new PropertyValueFactory<>("eventEfect"));
			efectColumn.setPrefWidth(110);
	
			TableColumn<Event, String> descColumn = new TableColumn<>("Resultado"); // Cambiado de "Descripción" a "Resultado"
			descColumn.setCellValueFactory(new PropertyValueFactory<>("eventResult"));
			descColumn.setPrefWidth(110);
	
			tEvent.getColumns().setAll(caseColumn, efectColumn, descColumn);


			tEvent.setItems(oEvent);
	
			scrollPaneTable  = new ScrollPane(tEvent);
			scrollPaneTable.setFitToWidth(true);
			scrollPaneTable.setFitToHeight(true);
		

		
			AnchorPane.setTopAnchor(scrollPaneTable, 0.0);
			AnchorPane.setLeftAnchor(scrollPaneTable, 0.0);
			AnchorPane.setRightAnchor(scrollPaneTable, 0.0);
			AnchorPane.setBottomAnchor(scrollPaneTable, 0.0);
			paneTable.getChildren().add(scrollPaneTable);

	
			root.getChildren().add(paneTable);
		}

	public void updateTable(ArrayList<Event> events) {
		oEvent.setAll(events);
	}
	
	public void setPaneButtonsMenu() {
		paneButtons = new AnchorPane();
		paneButtons.setLayoutY(400.0);
		paneButtons.setPrefSize(750, 200);
	
		int[] prefSize = new int[]{90, 40};

        bAmerica = createButton("America", 30, 200, prefSize);
        bEuropa = createButton("Europa", 85, 200, prefSize);
        bAsia = createButton("Asia", 145, 200, prefSize);
	
		paneButtons.getChildren().addAll(bAmerica, bEuropa, bAsia);
		root.getChildren().add(paneButtons);
	}

	public void setGameMap(String[][] data) {
		paneScreen.getChildren().clear();
		setLMatrix(data);
		Label[][] lMatrix = getlMatrix();

		setGPMatrix(lMatrix);
	}

	public void setPaneButtonsGame() {
		paneButtons.getChildren().clear();
		int[] prefSize = new int[]{160, 40};

		bVer = createButton("Ver", 30, 560,prefSize);
		bkillInfected = createButton("Matar Infeccion", 85, 560,prefSize);
		bKillPlayer = createButton("Matar Humanos", 145, 560,prefSize);
		
		paneButtons.getChildren().addAll(bVer, bkillInfected, bKillPlayer);
	}

	private Button createButton(String text, int layoutY, int layoutX, int[] prefZize) {
		Button button = new Button(text);
		button.setLayoutY(layoutY);
		button.setLayoutX(layoutX);
		button.setPrefSize(prefZize[0], prefZize[1]);
		return button;
	}

	public Button getbVer() {
		return bVer;
	}
	public Button getbkillInfected() {
		return bkillInfected;
	}
	public Button getbKillPlayer() {
		return bKillPlayer;
	}

	public GridPane getGPMatrix() {
		return gpMatrix;
	}

	public Label[][] getlMatrix() {
		return lMatrix;
	}

    public Scene getMyScene() {
        return myScene;
    }

	public Button getbAmerica() {
		return bAmerica;
	}

	public Button getbEuropa() {
		return bEuropa;
	}

	public Button getbAsia() {
		return bAsia;
	}
}