package com.shripathi.projects.website.elpais.pages.Opinion;

import com.shripathi.driver.DriverManager;
import com.shripathi.keywords.WebUI;
import com.shripathi.report.AllureManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.*;

public class OpinionPageElpais {
    public String pageUrl = "/opinion";
    public List opinionTitles = new ArrayList<String>();
    public void openOpinionPageElpais() {

        for(Integer i = 1; i <= 5; i++){
            AllureManager.saveTextLog("Title "+ i + " " + WebUI.getTextElement(By.xpath("//div[1]/section[1]/div[4]//article[x]//h2/a".replace("x",i.toString()))));
            opinionTitles.add(WebUI.getTextElement(By.xpath("//div[1]/section[1]/div[4]//article[x]//h2/a".replace("x",i.toString()))));
            WebUI.clickElement(By.xpath("//div[1]/section[1]/div[4]//article[x]//h2/a".replace("x",i.toString())));
            //AllureManager.saveTextLog(i + WebUI.getTextElement(By.xpath("//div[1]/section[1]/div[4]//article[x]//h2/a".replace("x",i.toString()))));
            readOpinionTopics(i);
            DriverManager.getDriver().navigate().back();
        }
        AllureManager.saveTextLog(translateTitle(opinionTitles));
    }

    public void readOpinionTopics(Integer i) {
        AllureManager.saveTextLog("Content " + i + " " + WebUI.getTextElement(By.xpath("//header/div[1]/h2")));
        downloadImages(i);
    }

    public void downloadImages(Integer i){
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//article//div[2]//span/img")));

        String src = null;
        List<WebElement> images = DriverManager.getDriver().findElements(By.xpath("//article//div[2]//span/img"));
        try {
            src = images.get(0).getAttribute("src");
            System.out.println(src);

        } catch (StaleElementReferenceException e) {
            // Handle stale element exceptions and continue
            System.out.println("Stale element exception");
            DriverManager.getDriver().quit();
            return;
        }


        // Directory to save images
        Path imageDirectory = Paths.get(".\\reports");


        try {
            WebElement image = images.get(0);
            String imageSrc = image.getAttribute("src");

            if (imageSrc != null && !imageSrc.isEmpty()) {
                // Check if the image source is base64 encoded
                if (imageSrc.startsWith("data:image")) {
                    // Decode base64 image
                    String[] parts = imageSrc.split(",");
                    String base64String = parts[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64String);

                    // Define file path
                    Path outputPath = imageDirectory.resolve("image_" + i + ".jpg");

                    // Save the image
                    try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
                        fos.write(imageBytes);
                        System.out.println("Downloaded image " + i + " to " + outputPath.toString());
                    } catch (Exception e) {
                        System.out.println("Failed to download image " + i + ": " + e.getMessage());
                    }
                } else {
                    // If not base64, handle as URL (for completeness)
                    URL imageUrl = new URL(imageSrc);
                    Path outputPath = imageDirectory.resolve("image_" + i + ".jpg");

                    try (InputStream in = imageUrl.openStream();
                         FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }

                        System.out.println("Downloaded image " + 1 + " to " + outputPath.toString());
                    } catch (Exception e) {
                        System.out.println("Failed to download image " + i + ": " + e.getMessage());
                    }

                    Allure.addAttachment("Image", imageUrl.openStream());
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing image " + (i) + ": " + e.getMessage());
        }

    }
    public String translateTitle(List content){
        AllureManager.saveTextLog("Translated to English");
        String translatedText = new String();
        Translate t = null;
        try {
            t = new Translate.Builder(
                    GoogleNetHttpTransport.newTrustedTransport()
                    , GsonFactory.getDefaultInstance(), null)
                    // Set your application name
                    .setApplicationName("Stackoverflow-Example")
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Translate.Translations.List list = null;
        try {
            list = t.new Translations().list(
                    content,
                    "EN");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO: Set your API-Key from https://console.developers.google.com/
        list.setKey("AIzaSyAtn_OQNk9yUYpPtW7pyuzPUY_Ocwgt2Lg");
        TranslationsListResponse response = null;
        try {
            response = list.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (TranslationsResource translationsResource : response.getTranslations())
        {
            //System.out.println(translationsResource.getTranslatedText());
            translatedText += translationsResource.getTranslatedText()+"\n";
        }
        identifyRepeatedWordsInTitles(translatedText);
        return translatedText;
    }

    public void identifyRepeatedWordsInTitles(String content){

        String[] myStringArray = content.split("\\s"); //Split the sentence by space.

        Map<String, Integer> wordOccurrences = new HashMap<String, Integer>(myStringArray.length);

        for (String word : myStringArray)
            if (wordOccurrences.containsKey(word))
                wordOccurrences.put(word, wordOccurrences.get(word) + 1);
            else wordOccurrences.put(word, 1);

        for (String word : wordOccurrences.keySet())
            if (wordOccurrences.get(word) > 1)
                AllureManager.saveTextLog("Words that occurs more than once: " + word + "\n");

    }
}
