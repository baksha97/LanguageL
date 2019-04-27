package fx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import language.LanguageLEnvironment;
import language.parse.numeric.DecodedProgram;
import language.parse.numeric.Encoder;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Controller implements Initializable {

    private static final String ABOUT_CONTEXT_MESSAGE =
            "You can find the latest source code on Github @baksha97."
                    + "\nBe sure to check out any branches for works in progress " +
                    "& please create an issue if a problem is found. " +
                    "\n\t\t-Travis";
    private static final String DEFAULT_PROGRAM_NAME = "current-L-program.txt";

    @FXML
    public TextField inputField;
    public TextArea programArea;
    public Label currentLabelLabel;
    public Label countLabel;
    public TextArea outputArea;
    public TextField stepByField;
    public Label nextInstructionLabel;
    public Label snapshotLabel;
    public TextArea variableHistoryArea;
    public Label prevInstructionLabel;
    public CheckBox noHistoryCheck;


    private LanguageLEnvironment env;
    private FileChooser fileChooser;

    private boolean needsReset;

    //Execution Buttons
    public void onSetClick() {
        outputArea.setText("");
        variableHistoryArea.setText("");
        if (!setupEnv()) return;
        onHistoryOffTick();
        updateInterface();
    }

    public void onRunClick() {

        if (env == null || needsReset) {
            println("Setup first.");
            return;
        }
        try {
            saveEditor();
            while (env.hasInstructions()) {
                env.executeNext();
            }
            updateInterface();
        } catch (Exception e) {
            updateInterface();
            e.printStackTrace();
            println(e.getLocalizedMessage());
            scrollDown();
            needsReset = true;
        }
    }

    public void onStepClick() {
        if (env == null || needsReset) {
            println("Setup first.");
            return;
        }

        try {
            saveEditor();
            int steps = Integer.valueOf(stepByField.getText().trim());
            for (int i = 0; i < steps && env.hasInstructions(); i++) {
                env.executeNext();
            }
            updateInterface();
        } catch (Exception e) {
            updateInterface();
            println("Unable to step.");
            println(e.getLocalizedMessage());
            e.printStackTrace();
            scrollDown();
            needsReset = true;
        }
    }

    public void onHistoryOffTick() {
        if (env == null) return;
        if (noHistoryCheck.isSelected()) {
            env.stopKeepingHistory();
        } else {
            env.startKeepingHistory();
        }
    }


    private boolean setupEnv() {
        try {
            saveEditor();
            env = new LanguageLEnvironment(programArea.getText().trim(), inputField.getText().trim());
            needsReset = false;
        } catch (Exception e) {
            println("Invalid input.");
            updateInterface();
            e.printStackTrace();
            println(e.getLocalizedMessage());
            needsReset = true;
            return false;
        }

        return true;
    }


    //Configure display
    private void updateInterface() {
        prevInstructionLabel.setText(env.vm.getPreviousInstruction());
        currentLabelLabel.setText(env.vm.getCurrentLabel());
        nextInstructionLabel.setText(env.vm.getNextInstruction());
        snapshotLabel.setText(env.vm.getSnapshot());
        countLabel.setText(env.vm.getExecutionCount());
        if (!noHistoryCheck.isSelected()) {
            variableHistoryArea.setText(env.getVariableHistory());
            outputArea.setText(env.getExeHistory());
        }
        scrollDown();
    }

    private void scrollDown() {
        variableHistoryArea.positionCaret(variableHistoryArea.getLength());
        outputArea.positionCaret(outputArea.getLength());
    }

    //Menu Buttons
    public void onOpenClick() {
        File picked = fileChooser.showOpenDialog(null);
        if (picked == null) return;
        load_file(picked.getAbsolutePath());
    }

    public void onSaveAsClick() {
        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;
        try {
            save_file(file.getAbsolutePath());
        } catch (IOException e) {
            println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void onInstructionEncodeClick(){
        try {
            //Temporary -- to quickly encode a program
            LanguageLEnvironment temp = new LanguageLEnvironment(programArea.getText().trim(), "Y=0");
            Encoder encoder = new Encoder(temp.getRuntime());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Encoded Instruction");
            alert.setHeaderText("Here are your instruction numbers...");
            alert.setContentText(String.valueOf(encoder.getInstructionNumbers()));
            alert.showAndWait();
        }catch (Exception e){
            println("Something went wrong trying to encode your program!");
            println("Make sure your program is made up of only basic instructions and conform only to the language syntax.");
        }
    }

    public void onProgramNumberDecodeClick(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Decode Program Number");
        dialog.setHeaderText("Let's use some magic to turn your number into a L program.");
        dialog.setContentText("Please enter the number");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try{
                DecodedProgram decodedProgram  = new DecodedProgram(new BigInteger(result.get().trim()));
                programArea.setText(decodedProgram.getDecodedCode());
            }catch (Exception e){
                e.printStackTrace();
                println("Error: couldn't numeric input into program number... " + result.get());
            }
        }
    }

    public void onInstructionNumbersDecodeClick(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Decode Instruction Numbers");
        dialog.setHeaderText("Let's use some magic to turn your numbers into an L program.");
        dialog.setContentText("Please enter the numbers using a comma as a separator.");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            try{
                String values = result.get().replace(" ", "");
                DecodedProgram decodedProgram  =
                        new DecodedProgram(
                                Arrays.stream(
                                        values.split(",")
                                ).map(BigInteger::new).toArray(BigInteger[]::new)
                        );
                programArea.setText(decodedProgram.getDecodedCode());
            }catch (Exception e){
                println("Error: couldn't numeric input into instruction numbers... " + result.get());
            }
        }
    }

    public void onAboutClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, ABOUT_CONTEXT_MESSAGE);
        alert.setHeaderText("About Editor");
        alert.show();
    }

    //Keyboard events
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                if (e.isShiftDown()) onSetClick();
                else onStepClick();
                break;
            default:
                break;
        }
    }

    //Output area printing
    private void println(Object o) {
        outputArea.appendText(o + "\n");
    }

    //File editor management
    private void saveEditor() throws IOException {
        save_file(DEFAULT_PROGRAM_NAME);
    }

    private void save_file(String location) throws IOException {
        List<String> lines = new ArrayList<>();
        Scanner prg = new Scanner(programArea.getText());
        while (prg.hasNextLine()) {
            String line = prg.nextLine();
            lines.add(line);
        }

        Path file = Paths.get(location);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    private void load_file(String fileLocation) {
        programArea.setText("");
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileLocation));
            for (String line : lines) {
                programArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            println("No current program found.");
            println(e.getLocalizedMessage());
        }
    }

    private void initializeFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("File Management");
        fileChooser.setInitialDirectory(Paths.get(".").toFile());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeFileChooser();
        load_file(DEFAULT_PROGRAM_NAME);
        needsReset = false;
    }
}

