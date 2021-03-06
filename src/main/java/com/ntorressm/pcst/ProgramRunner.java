package com.ntorressm.pcst;

import java.io.File;

public class ProgramRunner {

    private InputHandler inputHandler;
    private OutputHandler outputHandler;

    private File classFile;
    private File inputFile;
    private File outputFile;

    private ProcessBuilder processBuilder;
    private Process process;

    private long startTime;
    private long endTime;

    public ProgramRunner(File classFile, File inputFile, File outputFile) {
        this.classFile = classFile;
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        inputHandler = new InputHandler(inputFile);
        outputHandler = new OutputHandler(outputFile);
    }

    public void start() throws Exception {
        startProcess();

        handleInput();

        handleOutput();

        endProcess();

        checkSolution();
    }

    private void startProcess() throws Exception {
        processBuilder = new ProcessBuilder("java", classFile.getPath().substring(0, classFile.getPath().length()-6));
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        inputHandler.setStdin(process.getOutputStream());
        outputHandler.setStdout(process.getInputStream());
    }

    private void handleInput() throws Exception {
        startTime = System.currentTimeMillis();
        inputHandler.update();
        endTime = System.currentTimeMillis();
    }

    private void handleOutput() throws Exception {
        outputHandler.update();
    }

    private void endProcess() {
        process.destroy();
    }

    private void checkSolution() {
        System.out.printf("Solution took %.3fs\n", (endTime-startTime)/1000.0);

        switch (outputHandler.getResult()) {
            case CORRECT:
                System.out.println("The output is correct");
                break;
            case INCORRECT:
                System.out.println("The output is incorrect on line " + outputHandler.getIncorrectLineNumber());
                System.out.println("Correct output: " + outputHandler.getCorrectLine());
                System.out.println("Program's output: " + outputHandler.getJavaOutputLine());
                break;
            case LONGINPUT:
                System.out.println("The program has too much output");
                break;
            case SHORTINPUT:
                System.out.println("The program has too little output");
                break;
                default:
                    System.out.println("I don't know what you did, but you must have really messed up");
        }
    }
}

enum Result {
    CORRECT, INCORRECT, SHORTINPUT, LONGINPUT;
}
