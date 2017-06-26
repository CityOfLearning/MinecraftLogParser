package com.dyn.parser.utils;

import com.dyn.parser.logging.LogWriter;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

public class ParserProgressMonitor implements IProgressMonitor {

	private int _progress;
	private DoubleProperty progressProperty;
	private StringProperty errorProperty;
	private StringProperty statusProperty;
	private BooleanProperty visibleProperty;
	private int max_length;

	public ParserProgressMonitor() {
		progressProperty = new SimpleDoubleProperty(0);
		errorProperty = new SimpleStringProperty("");
		statusProperty = new SimpleStringProperty("");
		visibleProperty = new SimpleBooleanProperty(false);
		_progress = 0;
		max_length = 1;
	}

	public DoubleProperty getBarProperty() {
		return progressProperty;
	}

	public StringProperty getErrorProperty() {
		return errorProperty;
	}

	public String getErrorStatus() {
		return errorProperty.getValue();
	}

	public int getProgress() {
		return _progress;
	}

	public final double getProgressPercent() {
		return Math.max(Math.min((double) _progress / (double) max_length, 1), 0);
	}

	@Override
	public final void incrementProgress(int amount) {
		_progress = Math.min(max_length, _progress + amount);
		// progressProperty.setValue(getProgressPercent());
		Platform.runLater(() -> progressProperty.setValue(ParserProgressMonitor.this.getProgressPercent()));
	}

	@Override
	public void setErrorStatus(String status) {
		Platform.runLater(() -> errorProperty.setValue(status));
	}

	@Override
	public void setMax(int len) {
		max_length = len;
	}

	@Override
	public final void setProgress(int progress) {
		_progress = Math.min(max_length, progress);
		Platform.runLater(() -> progressProperty.setValue(ParserProgressMonitor.this.getProgressPercent()));

	}

	public StringProperty getStatusProperty() {
		return statusProperty;
	}

	public String getStatusPropertyText(){
		return statusProperty.getValue();
	}
	
	public void SetStatusPropertyText(String text){
		Platform.runLater(() -> statusProperty.setValue(text));
	}

	public BooleanProperty getVisibleProperty() {
		return visibleProperty;
	}
	
	public final void setVisible(boolean state){
		Platform.runLater(() -> visibleProperty.setValue(state));

	}

	public void setErrorStatus(String status, Exception e) {
		LogWriter.error(status, e);
		Platform.runLater(() -> errorProperty.setValue(status));
	}

}
