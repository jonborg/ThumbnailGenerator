package ui.controller;

import ui.factory.alert.AlertFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Player2Controller extends Player1Controller {

    @Override
    protected void setDefaultFlip (String urlName){
        boolean skip = false;
        if (urlName == null){
            flip.setSelected(false);
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(flipFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (urlName == "falco" && !skip && line.contains(urlName)){
                    skip = true;
                    continue;
                }
                if (line.contains(urlName)){
                    String[] words = line.split(" ");
                    if (words.length>1)
                        flip.setSelected(!Boolean.parseBoolean(words[1]));
                    break;
                }
            }
        }catch(IOException e){
            AlertFactory.displayError("Could not find flip file");
        }
    }
}
