import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;


;

public class UIController implements Initializable {

	AVLTree avl;
	VisualizationHelper vlHelper;
	
	private Deque<Transition> animeDeque;
	
	private enum Mode {STEP, NORMAL};
	private enum Result {NONE, FOUND, NOTFOUND, ADDED, NOTADDED, REMOVED, NOTREMOVED};
	
	private Mode mode;
	private Result result;
	
	@FXML
	private Button insertButton;
	
	@FXML
	private Button removeButton;
	
	@FXML
	private Button clearButton;
	
	@FXML
	private Button nextButton;
	
	@FXML
	private Button findButton;
	
	@FXML
	private Button pauseButton;

	@FXML
	private TextField insertField;
	
	@FXML
	private TextField removeField;
	
	@FXML
	private TextField clearField;
	
	@FXML
	private TextField findField;
	
	@FXML
	private Label messageLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		mode = Mode.NORMAL;
		result = Result.NONE;
		nextButton.setDisable(true);
		animeDeque = new ArrayDeque<Transition>();
		
	}
	
	public void setCanvas(Pane canvas) {
		vlHelper = new VisualizationHelper(canvas);
		avl = new AVLTree(vlHelper);
	}
	
	public void insertEvent(ActionEvent event) {
		disableButtons();
		
		String item = insertField.getText();
		if(!Validator.isNumeric(item)) {
			messageLabel.setText("You must enter an integer!");
			enableButtons();
	        clearInputs();
		} else if (Integer.parseInt(item) >= 100 || Integer.parseInt(item) < 0) {
			messageLabel.setText("You must enter an integer between 0 and 99");
			enableButtons();
	        clearInputs();
			
		} else{
			messageLabel.setText("Running...");
			Integer added = Integer.parseInt(item);
			boolean inserted = avl.add(added, animeDeque);
			
			if (inserted) {
				result = Result.ADDED;
			} else {
				result = Result.NOTADDED;
			}
			
			if (mode == Mode.NORMAL) {
				sequentialPlay(animeDeque);
			}
		}
		
	}
	
	public void removeEvent(ActionEvent event) {
		disableButtons();
		
		String item = removeField.getText();
		if(!Validator.isNumeric(item)) {
			messageLabel.setText("You must enter an integer!");
			enableButtons();
	        clearInputs();
		} else if (Integer.parseInt(item) >= 100 || Integer.parseInt(item) < 0) {
			messageLabel.setText("You must enter an integer between 0 and 99");
			enableButtons();
	        clearInputs();
		} else{
			messageLabel.setText("Running...");
			Integer removed = Integer.parseInt(item);
			boolean deleted = avl.remove(removed, animeDeque);
			
			if (deleted) {
				result = Result.REMOVED;
			} else {
				result = Result.NOTREMOVED;
			}
			
			if (mode == Mode.NORMAL) {
				sequentialPlay(animeDeque);
			}
		}
		
	}
	
	public void findEvent(ActionEvent event) {
		disableButtons();
		
		String item = findField.getText();
		if(!Validator.isNumeric(item)) {
			messageLabel.setText("You must enter an integer!");
			enableButtons();
	        clearInputs();
		} else if (Integer.parseInt(item) >= 100 || Integer.parseInt(item) < 0) {
			messageLabel.setText("You must enter an integer between 0 and 99");
			enableButtons();
	        clearInputs();
		} else {
			messageLabel.setText("Running...");
			Integer toFound = Integer.parseInt(item);
			boolean found = avl.contains(toFound, animeDeque);
			
			if (found) {
				result = Result.FOUND;
			} else {
				result = Result.NOTFOUND;
			}
									
			if (mode == Mode.NORMAL) {
				sequentialPlay(animeDeque);
			}
		}
		
	}
	
	public void clearEvent(ActionEvent event) {
		avl.clear();		
	}
	
	public void pauseEvent(ActionEvent event) {
		switch (mode) {
		case NORMAL:
			pauseButton.setText("Go");
			mode = Mode.STEP;
			break;
		case STEP:
			pauseButton.setText("Pause");
			nextButton.setDisable(true);
			mode = Mode.NORMAL;
			break;
		}
		
	}
	
	public void nextEvent(ActionEvent event) {
		if (!animeDeque.isEmpty()) {
			Transition transition = animeDeque.pop();
			nextButton.setDisable(true);
			EventHandler<ActionEvent> preHandler = transition.getOnFinished();
			transition.setOnFinished(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	if (preHandler != null) {
			    		preHandler.handle(event);
			    	}
			    	
			    	if(animeDeque.isEmpty()) {
			    		pauseButton.setText("Pause");
						nextButton.setDisable(true);
						mode = Mode.NORMAL;
						clearInputs();
						enableButtons();
						setTextOnResult();
			    	} else {
			    		nextButton.setDisable(false);
			    	}
			    }
			});
			transition.play();
		} else {
			pauseButton.setText("Pause");
			nextButton.setDisable(true);
			mode = Mode.NORMAL;
			enableButtons();
			clearInputs();
			setTextOnResult();
		}
		
	}
	
	private void disableButtons() {
		insertButton.setDisable(true);
		findButton.setDisable(true);
		removeButton.setDisable(true);
		pauseButton.setDisable(true);
		clearButton.setDisable(true);
		if (mode == Mode.NORMAL) {
			nextButton.setDisable(true);
		} else {
			nextButton.setDisable(false);
		}
		
	}
	
	private void enableButtons() {
		insertButton.setDisable(false);
		findButton.setDisable(false);
		removeButton.setDisable(false);
		pauseButton.setDisable(false);
		clearButton.setDisable(false);	
	}
	
	private void clearInputs() {
		insertField.clear();
		findField.clear();
		removeField.clear();
	}
	
	public void setTextOnResult() {
		switch (result) {
		case NONE:
			messageLabel.setText("Waiting...");
			break;
		case FOUND:
			messageLabel.setText("Number found! Waiting...");
			break;
		case NOTFOUND:
			messageLabel.setText("Number not found! Waiting...");
			break;
		case ADDED:
			messageLabel.setText("Number added! Waiting...");
			break;
		case NOTADDED:
			messageLabel.setText("Duplicate number, not added! Waiting...");
			break;
		case REMOVED:
			messageLabel.setText("Number removed! Waiting...");
			break;
		case NOTREMOVED:
			messageLabel.setText("Number to be removed not found! Waiting...");
			break;
		}
		
		result = Result.NONE;
		
	}
	
	private void sequentialPlay(Deque<Transition> animeDeque) {
		SequentialTransition st = new SequentialTransition();
		st.getChildren().addAll(animeDeque);
		st.setOnFinished(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	animeDeque.clear();
		        enableButtons();
		        clearInputs();
		        setTextOnResult();
		    }
		});
		st.play();
	}
}
