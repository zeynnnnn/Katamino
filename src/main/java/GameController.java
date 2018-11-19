/** Game controller controls the game board logic and tile actions.
 * @author Bartu Atabek
 * @version 1.0
 */

import java.net.URL;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import kataminoDragCell.KataminoDragCell;

public class GameController implements Initializable {

    private ArrayList<Color> colorList = new ArrayList<Color>(){{
        add(Color.ANTIQUEWHITE);
        add(Color.GRAY);
        add(Color.ALICEBLUE);
        add(Color.AZURE);
        add(Color.SALMON);
        add(Color.CHARTREUSE);
        add(Color.CORNFLOWERBLUE);
        add(Color.MEDIUMPURPLE);
        add(Color.LIME);
        add(Color.TEAL);
        add(Color.OLIVE);
        add(Color.ORANGERED);
    }};
    private Level currentLevel;
    private Boolean isPaused;
    private int count;

    @FXML
    private GridPane gameGridPane;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Label playerLabel;

    public boolean isFull(){
            return false;
    }

    public void placePentomino() {}

    public void movePentomino () {}

    public boolean clashCheck() {
//        int pentominoID = 0;
//        if( board.getGrid()[0][0].getPentominoInstanceID() == pentominoID) //wrong
//            return true;
        return false;
    }

    public void colorClashingCells(){}

    public void loadLevel() throws FileNotFoundException {
       currentLevel = new FileManager().loadLevels();
       Integer[][] board = currentLevel.getBoard();

        for (int i= 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                KataminoDragCell currentCell = (KataminoDragCell) gameGridPane.getChildren().get((i*22)+j);
                Color cellColor = ((board[i][j] % 12) >= 0 && (board[i][j] != 0)) ? colorList.get(board[i][j] % 12) : Color.web("#262626");
                currentCell.customizeCell(board[i][j], board[i][j] != 0, cellColor);

                if (currentCell.getPentominoInstanceID() == 0) {
                    currentCell.setBorderColor(Color.WHITE);
                }
            }
        }
    }

    Timeline stopwatchChecker;

    public void updateStopwatch() {
        stopwatchChecker = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            stopwatchLabel.setText(secondsToString(count));
            count++;
        }));
        stopwatchChecker.setCycleCount(Timeline.INDEFINITE);
        stopwatchChecker.play();
    }

    public void startGame(){
        updateStopwatch();
    }

    public void pauseGame() {
        stopwatchLabel.setText("▶️" + stopwatchLabel.getText());
        stopwatchChecker.stop();
    }

    public void playPause(MouseEvent e) {
        if (isPaused) {
            isPaused = !isPaused;
            startGame();
        } else {
            isPaused = !isPaused;
            pauseGame();
        }
    }

    public int[][] findSiblings(MouseEvent event){
        ArrayList<Node> oldNodes = new ArrayList<>();
        Node source = (Node) event.getSource();
        KataminoDragCell currentPentomino;
        int pentominoInstanceID = ((KataminoDragCell) source).getPentominoInstanceID();
        ObservableList<Node> cells = gameGridPane.getChildren();
        Stack<Node> currentSearch = new Stack<>();
        int[][] currentShape= new int[11][22];
        currentSearch.push(source);
        Integer colIndex;
        Integer rowIndex;

        while(!currentSearch.empty()) {
            colIndex = GridPane.getColumnIndex(currentSearch.peek());
            rowIndex = GridPane.getRowIndex(currentSearch.peek());
            if(colIndex == null)
                colIndex = 0;
            if(rowIndex == null)
                rowIndex = 0;
            currentShape[rowIndex][colIndex] = pentominoInstanceID;
            oldNodes.add(currentSearch.pop());

            if(colIndex + 1 <= 21)
            {
                currentPentomino = (KataminoDragCell)cells.get(rowIndex*22+colIndex + 1);
                if(!oldNodes.contains(currentPentomino) && currentPentomino.getPentominoInstanceID() == pentominoInstanceID)
                    currentSearch.push(currentPentomino);
            }

            if(colIndex - 1 >= 0)
            {
                currentPentomino = (KataminoDragCell)cells.get(rowIndex*22+colIndex - 1);
                if(!oldNodes.contains(currentPentomino) &&currentPentomino.getPentominoInstanceID() == pentominoInstanceID)
                    currentSearch.push(currentPentomino);
            }
            if(rowIndex + 1 <= 10)
            {
                currentPentomino = (KataminoDragCell)cells.get((rowIndex + 1)*22+colIndex);
                if(!oldNodes.contains(currentPentomino) &&currentPentomino.getPentominoInstanceID() == pentominoInstanceID)
                    currentSearch.push(currentPentomino);
            }
            if(rowIndex - 1 >= 0)
            {
                currentPentomino = (KataminoDragCell)cells.get((rowIndex - 1)*22+colIndex);
                if(!oldNodes.contains(currentPentomino) &&currentPentomino.getPentominoInstanceID() == pentominoInstanceID)
                    currentSearch.push(currentPentomino);
            }
        }
        return currentShape;
    }

    public String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        count = 0;
        isPaused = false;
        try {
            loadLevel();
        } catch (Exception e) {
            System.out.println(e);

        }
        playerLabel.setText("Adamotu 0");
        startGame();
    }
}