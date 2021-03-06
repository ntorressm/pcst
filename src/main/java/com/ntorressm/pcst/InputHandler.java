package com.ntorressm.pcst;

import jdk.internal.util.xml.impl.Input;

import java.io.*;

public class InputHandler {

    private File file;
    private OutputStream stdin;

    private BufferedReader inputFileReader;
    private BufferedWriter javaInputWriter;

    public InputHandler(File file) {
        this.file = file;
    }

    public void update() throws Exception {
        inputFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        javaInputWriter = new BufferedWriter(new OutputStreamWriter(stdin));

        // Read lines from file and input them into the java process
        String line;
        while ((line = inputFileReader.readLine()) != null) {
            javaInputWriter.write(line);
            javaInputWriter.newLine();
        }

        // Close everything
        inputFileReader.close();
        javaInputWriter.flush();
        javaInputWriter.close();
    }

    public void setStdin(OutputStream stdin) {
        this.stdin = stdin;
    }
}
