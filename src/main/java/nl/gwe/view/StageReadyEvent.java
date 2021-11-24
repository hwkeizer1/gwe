package nl.gwe.view;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

public class StageReadyEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4632996047583013796L;

	public final Stage stage;
	
	public StageReadyEvent(Stage stage) {
		
		super(stage);
		this.stage = stage;
	}
}
