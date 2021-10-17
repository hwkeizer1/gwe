package nl.gwe.view;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import nl.gwe.controllers.RootController;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

	private final FxWeaver fxWeaver;
	
	public PrimaryStageInitializer(FxWeaver fxWeaver) {
		this.fxWeaver = fxWeaver;
	}

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		Stage stage = event.stage;
		
		Scene scene = new Scene(fxWeaver.loadView(RootController.class), 1200, 800);
		scene.getStylesheets().clear();
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("GWE 1.0");
		stage.show();
	}
}
