package com.dyn.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.dyn.parser.logging.LogWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class FXMLViewController {
	@FXML
	private TextField regex_text;
	@FXML
	private TextField dir_text;
	@FXML
	private Button parse_btn;

	@FXML
	private Button dir_btn;

	@FXML
	private Button file_btn;

	@FXML
	private ProgressBar prog_bar;

	@FXML
	private Label status_lbl;

	@FXML
	private Label error_lbl;

	private File file_dir_path;

	@FXML
	protected void initialize() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Open Logs Dir");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Logs File");

		prog_bar.progressProperty().bind(Parser.monitor.getBarProperty());
		prog_bar.getStylesheets().add("assets/dyn/parser/striped-progress.css");
		prog_bar.setVisible(false);
		
		status_lbl.textProperty().bind(Parser.monitor.getStatusProperty());
		
		error_lbl.textProperty().bind(Parser.monitor.getErrorProperty());

		dir_btn.setOnMouseClicked(btn -> {
			file_dir_path = dirChooser.showDialog(Parser.primStage);
			if (file_dir_path != null) {
				dir_text.setText(file_dir_path.getAbsolutePath());
			}

		});

		file_btn.setOnMouseClicked(btn -> {
			file_dir_path = fileChooser.showOpenDialog(Parser.primStage);
			if (file_dir_path != null) {
				dir_text.setText(file_dir_path.getAbsolutePath());
			}

		});

		parse_btn.setOnMouseClicked(btn -> {
			Parser.monitor.setErrorStatus("");
			if (!regex_text.getText().isEmpty() && !dir_text.getText().isEmpty()) {
				if (!file_dir_path.getAbsolutePath().equals(dir_text.getText())) {
					file_dir_path = new File(dir_text.getText());
				}
				ArrayList<String> matchedLines = new ArrayList<String>();
				Pattern regexPat;
				try{
					regexPat = Pattern.compile(regex_text.getText());
				} catch(PatternSyntaxException pse){
					LogWriter.error(pse);
					Parser.monitor.setErrorStatus("Regex pattern is invalid");
					return;
				}
				prog_bar.setVisible(true);
				if (file_dir_path.exists()) {
					if (!file_dir_path.isDirectory()) {
						Parser.monitor.SetStatusPropertyText("Parsing File: " + file_dir_path.getName() + " with Pattern: " + regex_text.getText());
						try (BufferedReader br = new BufferedReader(new FileReader(file_dir_path))) {
							Parser.monitor.setProgress(0);
							Object[] lines = br.lines().toArray();
							Parser.monitor.setMax(lines.length);
							for(Object line : lines){
								String sLine = (String) line;
					            if (regexPat.matcher(sLine).find( )) {
					            	matchedLines.add(sLine);
					            }
								Parser.monitor.incrementProgress(1);
							}
						} catch (FileNotFoundException e) {
							LogWriter.error(e);
						} catch (IOException e) {
							LogWriter.error(e);
						}
					} else {
						for (File file : file_dir_path.listFiles()) {
							if (!file.isDirectory()) {
								Parser.monitor.SetStatusPropertyText("Parsing File: " + file_dir_path.getName() + " with Pattern: " + regex_text.getText());
								try (BufferedReader br = new BufferedReader(new FileReader(file))) {
									matchedLines.add(file.getName());
									Parser.monitor.setProgress(0);
									Object[] lines = br.lines().toArray();
									Parser.monitor.setMax(lines.length);
									for(Object line : lines){
										String sLine = (String) line;
							            if (regexPat.matcher(sLine).find( )) {
							            	matchedLines.add(sLine);
							            }
										Parser.monitor.incrementProgress(1);
									}
								} catch (FileNotFoundException e) {
									LogWriter.error(e);
								} catch (IOException e) {
									LogWriter.error(e);
								}
								matchedLines.add("------- END FILE -------");
							}
						}
					}
					try{
					    PrintWriter writer = new PrintWriter("ParsedLogs.txt", "UTF-8");
					    for(String line : matchedLines){
					    	 writer.println(line);
					    }
					    writer.close();
					} catch (IOException e) {
					   // do something
					}
					prog_bar.setVisible(false);
				}
			} else {
				if (regex_text.getText().isEmpty()) {
					Parser.monitor.setErrorStatus("Regex pattern is Empty");
				} else if (dir_text.getText().isEmpty()){
					Parser.monitor.setErrorStatus("File doesnt exist");
				}
				
			}
		});

	}
}
